package play.modules.protocolbuffers;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import playmessages.Playmessages;
import playmessages.Playmessages.OutgoingMessage;
import playmessages.Playmessages.IncomingMessage.Builder;

public class Client {

	public static void main(String[] args) throws Exception {
		final Builder incomingMessageBuilder = Playmessages.IncomingMessage.newBuilder();
		incomingMessageBuilder.setName("Test");
		final playmessages.Playmessages.PlayRequestWrapper.Builder playRequestWrapperBuilder = Playmessages.PlayRequestWrapper.newBuilder();
		playRequestWrapperBuilder.setIncomingMessage(incomingMessageBuilder);
		final Socket socket = new Socket("127.0.0.1",12345);
		final OutputStream outputStream = socket.getOutputStream();
		final InputStream inputStream = socket.getInputStream();
		
		playRequestWrapperBuilder.build().writeTo(outputStream);
		
		System.out.println("Parsing");
		final OutgoingMessage parseFrom = OutgoingMessage.parseFrom(inputStream);
		System.out.println(parseFrom.getName());
	}

}
