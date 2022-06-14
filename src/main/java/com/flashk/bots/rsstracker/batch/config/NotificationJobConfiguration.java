package com.flashk.bots.rsstracker.batch.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.flashk.bots.rsstracker.batch.FeedEntityItemProcessor;
import com.flashk.bots.rsstracker.batch.FeedEntityItemWriter;
import com.flashk.bots.rsstracker.repositories.entities.FeedEntity;
import com.pengrad.telegrambot.TelegramBot;

@Configuration
@EnableBatchProcessing
public class NotificationJobConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Bean
	public TelegramBot getBot(@Value("${bot.token}") String token) {
		return new TelegramBot(token);
	}
	
	@Bean
	@StepScope
	public ItemReader<FeedEntity> reader() {
	    MongoItemReader<FeedEntity> mongoItemReader = new MongoItemReader<FeedEntity>();
	    mongoItemReader.setTemplate(mongoTemplate);
	    mongoItemReader.setCollection("rssFeeds");
	    mongoItemReader.setQuery("{}");
	    mongoItemReader.setTargetType(FeedEntity.class);
	    Map<String, Sort.Direction> sort = new HashMap<String, Sort.Direction>();
	    sort.put("_id", Sort.Direction.ASC);
	    mongoItemReader.setSort(sort);

	    return mongoItemReader;
	}
	

	@Bean
	public FeedEntityItemProcessor processor() {
		return new FeedEntityItemProcessor();
	}

	@Bean
	public FeedEntityItemWriter writer() {
		return new FeedEntityItemWriter();
	}

	@Bean
	public NotificationJobConfiguration jobExecutionListener() {
		return new NotificationJobConfiguration();
	}

	@Bean
	public Job job(Step step, NotificationJobConfiguration jobExecutionListener) {
		return jobBuilderFactory.get("process_new_feed_items")
				.listener(jobExecutionListener)
				.flow(step)
				.end()
				.build();
	}

	@Bean
	public Step step(ItemReader<FeedEntity> reader,
			FeedEntityItemProcessor processor,
			FeedEntityItemWriter writer) {

		TaskletStep step = stepBuilderFactory.get("send_telegram_notifications")
				.<FeedEntity, FeedEntity>chunk(100)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.allowStartIfComplete(true)
				.build();

		return step;
	}
}
