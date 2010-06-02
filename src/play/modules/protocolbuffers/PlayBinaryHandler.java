package play.modules.protocolbuffers;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Map.Entry;

import org.quickserver.net.server.ClientBinaryHandler;
import org.quickserver.net.server.ClientHandler;
import org.quickserver.net.server.DataMode;
import org.quickserver.net.server.DataType;

import play.Play;
import playmessages.Playmessages.PlayRequestWrapper;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Descriptors.FieldDescriptor;

public class PlayBinaryHandler implements ClientBinaryHandler {

	@Override
	public void handleBinary(ClientHandler clientHandler, byte[] bytesFromClient)
			throws SocketTimeoutException, IOException {
		System.out.println("Processing");
		final PlayRequestWrapper wrapper = PlayRequestWrapper.parseFrom(bytesFromClient);
		final Map<FieldDescriptor, Object> allFields = wrapper.getAllFields();
		for (Entry<FieldDescriptor, Object> entry : allFields.entrySet()) {
			final Object messageToHandle = entry.getValue();
			//find a suitable method in the controller
			try {
				final Method methodToInvoke = findMethodForMessage(messageToHandle);
				final GeneratedMessage result = (GeneratedMessage)methodToInvoke.invoke(null, messageToHandle);
				System.out.println("ClientHandler is:"+clientHandler.getClass().getName());
				clientHandler.setDataMode(DataMode.BINARY, DataType.OUT);
				clientHandler.sendClientBinary(result.toByteArray());
				clientHandler.closeConnection();
				System.out.println("Response sent");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}

	private Method findMethodForMessage(Object messageToHandle) throws Exception {
		System.out.println("Message:"+messageToHandle.getClass().getName());
		final String controller = (String) Play.configuration.get("gpb.controller");
		final Method[] methods = Class.forName(controller).getMethods();
		for (Method method : methods) {
			final Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length == 1 && parameterTypes[0].equals(messageToHandle.getClass()) && Modifier.isStatic(method.getModifiers())) {
				return method;
			}
		}
		throw new Exception("Unable to find method for class "+messageToHandle.getClass());
		
	}

}
