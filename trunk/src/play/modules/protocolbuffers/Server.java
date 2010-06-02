package play.modules.protocolbuffers;

import java.io.IOException;

import org.quickserver.net.AppException;
import org.quickserver.net.server.DataMode;
import org.quickserver.net.server.DataType;
import org.quickserver.net.server.QuickServer;

import play.Play;
import play.PlayPlugin;

public class Server extends PlayPlugin {
	
	@Override
	public void onConfigurationRead() {
		try {
			QuickServer server = new QuickServer();
			final int port = Integer.parseInt((String)Play.configuration.get("gpb.port"));
			server.setPort(port);
			server.setDefaultDataMode(DataMode.BINARY, DataType.IN);
			server.setDefaultDataMode(DataMode.BINARY, DataType.OUT);
			server.setClientBinaryHandler("play.modules.protocolbuffers.PlayBinaryHandler");
			server.setClientCommandHandler("play.modules.protocolbuffers.PlayCommandHandler");
			server.setClientEventHandler("play.modules.protocolbuffers.GPBEventHandler");
			server.startServer();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	
}
