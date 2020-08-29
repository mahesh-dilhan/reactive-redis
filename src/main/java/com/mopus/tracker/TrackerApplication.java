package com.mopus.tracker;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static reactor.core.publisher.Flux.concat;

@SpringBootApplication
public class TrackerApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(TrackerApplication.class, args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {

	}
}

@Log4j2
@Component
class DataGenerator{

	private static final String KEY="trakers";
	private final ReactiveRedisTemplate<String, Traker> trakerReactiveRedisTemplate;

	DataGenerator(ReactiveRedisTemplate<String, Traker> trakerReactiveRedisTemplate) {
		this.trakerReactiveRedisTemplate = trakerReactiveRedisTemplate;
	}


	private ReactiveHashOperations<String, String, Traker> reactiveTrakerHashOperations() {
		return trakerReactiveRedisTemplate.<String, Traker>opsForHash();
	}

	@EventListener(ApplicationReadyEvent.class)
	public void writeData(){

		List<Traker> lst= new ArrayList<>();

		for(int i=0;i<10;i++) {


			MutableData m = MutableData.builder()
					.dl(UUID.randomUUID().toString())
					.url(UUID.randomUUID().toString())
					.build();
			Message msg = Message.builder()
					.body(UUID.randomUUID().toString())
					.title(UUID.randomUUID().toString())
					.isMutable(true)
					.build();
			Receiver receiver = Receiver.builder()
					.emailId(UUID.randomUUID().toString())
					.recepientId(UUID.randomUUID().toString())
					.userId(UUID.randomUUID().toString()).build();
			Payload payload = Payload.builder()
					.data(m)
					.message(msg)
					.receiver(receiver)
					.build();

			Traker t = Traker.builder()
					.trakerId(UUID.randomUUID().toString())
					.subscriptionId(UUID.randomUUID().toString())
					.appId(UUID.randomUUID().toString())
					.deviceId(UUID.randomUUID().toString())
					.messageIdentifer(UUID.randomUUID().toString())
					.status("NEW")
					.payload(payload)
					.createTimeStamp(new Date())
					.lastUpdatedTimestamp(new Date())
					.build();
			lst.add(t);
		}

		log.info("{}",lst.size());
		Flux.fromIterable(lst)
				.flatMap(traker-> reactiveTrakerHashOperations().put(KEY, traker .getTrakerId().toString(), traker))
				.subscribe(log::info);
	}


}


