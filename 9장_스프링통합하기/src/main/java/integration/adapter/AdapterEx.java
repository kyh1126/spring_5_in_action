package integration.adapter;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.support.GenericMessage;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class AdapterEx {
	@Bean
	@InboundChannelAdapter(channel = "numberChannel", poller = @Poller(fixedRate = "1000"))
	public MessageSource<Integer> numberSource (AtomicInteger source) {
		return () -> new GenericMessage<>(source.getAndIncrement());
	}

	//dsl 예제인데 인터페이스가 맞지 ㅇ낳는것 같은..

//	public IntegrationFlow someFlow(AtomicInteger source) {
//		return IntegrationFlows
//				.from(source, "getAndIncrement",
//						c -> c.poller(Pollers.fixedRate(1000)))
//	}

	//ex. 지정된 디렉토리를 모니터링 하면서, 해당 디엑터리에 파일 저장되면 file-channel에 메세지 전달하는 인바운드 채널 어댑터 생성
	@Bean
	@InboundChannelAdapter(channel = "file-channel", poller = @Poller(fixedRate = "1000"))
	public MessageSource<File> fileReadingMessageSource() {
		FileReadingMessageSource sourceReader = new FileReadingMessageSource();
		sourceReader.setDirectory(new File("INPUT"));
		sourceReader.setFilter(new SimplePatternFileListFilter("FILE_PATTERN"));
		return sourceReader;
	}

	//dsl 작성
	@Bean
	public IntegrationFlow fileReaderFlow(){
		return IntegrationFlows
				.from(Files.inboundAdapter(new File("INPUT_DIR")))
				.filter("FILE_PATTERN ")
				.get();
	}
}
