package play.modules.protocolbuffers;

import playmessages.Playmessages.IncomingMessage;
import playmessages.Playmessages.OutgoingMessage;

public class GPBController {
	
	public static OutgoingMessage handle(IncomingMessage incomingMessage) {
		final String name = incomingMessage.getName();
		OutgoingMessage om = OutgoingMessage.newBuilder().setName(name+"processed").build();
		return om;
	}
	
	

}
