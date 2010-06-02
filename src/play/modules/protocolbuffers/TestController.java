package play.modules.protocolbuffers;

import play.Logger;
import play.mvc.Controller;
import playmessages.Playmessages.IncomingMessage;
import playmessages.Playmessages.OutgoingMessage;

public class TestController extends Controller {
	
	public static OutgoingMessage process(IncomingMessage incomingMessage) {
		Logger.error("Received :"+incomingMessage.getName());
		System.err.println("Received :"+incomingMessage.getName());
		return OutgoingMessage.newBuilder().setName("test").build();
	}

}
