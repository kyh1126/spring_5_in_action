package integration.gateway;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultReplyChannel = "textInChannel")
public interface FileWriterGateway {

	void writeToFile(@Header(FileHeaders.FILENAME) String filename, String data);
}
