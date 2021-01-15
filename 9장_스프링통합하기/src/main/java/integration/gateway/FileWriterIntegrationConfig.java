package integration.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.transformer.GenericTransformer;

import java.io.File;

@Configuration
@ImportResource("classpath:gatewayconfig/filewriter-config.xml")
public class FileWriterIntegrationConfig {
	@Bean
	@Transformer(inputChannel = "textInChannel",
			outputChannel = "fileWriterChannel")
	public GenericTransformer<String, String> upperCaseTransformer() {
		return String::toUpperCase;
	}

	@Bean
	@ServiceActivator(inputChannel = "fileWriterChannel")
	public FileWritingMessageHandler fileWriter() {
		FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("/temp/sia/files"));
		handler.setExpectReply(false);
		handler.setFileExistsMode(FileExistsMode.APPEND);
		handler.setAppendNewLine(true);
		return handler;
	}

	@Bean
	public IntegrationFlow fileWriterFlow(){
//		return IntegrationFlows
//				.from(MessageChannels.direct("textInChannel"))
//				.<String,String>transform(String::toUpperCase)
//				.handle(
//					Files.outboundAdapter(new File("/temp/sia/files"))
//					.fileExistsMode(FileExistsMode.APPEND)
//					.appendNewLine(true))
//				.get();

		return IntegrationFlows
				.from(MessageChannels.direct("textInChannel"))
				.<String,String>transform(String::toUpperCase)
				.channel(MessageChannels.direct("fileWriteChannel"))    //별도의 outbound channel 명을 설정한다.
				.handle(Files
					.outboundAdapter(new File("/tmp/sia/files"))
					.fileExistsMode(FileExistsMode.APPEND)
					.appendNewLine(true))
				.get();

	}
}
