package com.flashk.bots.rsstracker.batch.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.data.mongodb.MongoTransactionManager;

import com.flashk.bots.rsstracker.batch.FeedEntityItemProcessor;
import com.flashk.bots.rsstracker.batch.FeedEntityItemWriter;
import com.flashk.bots.rsstracker.repositories.entities.FeedEntity;

@ExtendWith(MockitoExtension.class)
class NotificationJobConfigurationTest {

	@Spy
	@InjectMocks
	private NotificationJobConfiguration notificationJobConfiguration = new NotificationJobConfiguration();
	
	@Mock
	public StepBuilderFactory stepBuilderFactory;
	
	@Mock
	private JobBuilderFactory jobBuilderFactory;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testGetBot() {
		
		assertNotNull(notificationJobConfiguration.getBot("1234"));
	}

	@Test
	void testReader() {
		
		assertNotNull(notificationJobConfiguration.reader());
		
	}

	@Test
	void testProcessor() {
		assertNotNull(notificationJobConfiguration.processor());
	}

	@Test
	void testWriter() {
		assertNotNull(notificationJobConfiguration.writer());
	}

	@Test
	void testJob() {

		// Support classes
		JobBuilder jobBuilder = manufactureJobBuilderPojo();

		// Prepare mocks
		Mockito.doReturn(jobBuilder).when(jobBuilderFactory).get(any());
		
		// Execute method
		assertNotNull(notificationJobConfiguration.job(new TaskletStep()));
		
		
	}

	@Test
	void testStep() {
		
		// Support classes
		StepBuilder stepBuilder = manufactureStepBuilderPojo();
		
		// Prepare method arguments
		MongoItemReader<FeedEntity> reader = new MongoItemReader<FeedEntity>();
		FeedEntityItemProcessor processor = new FeedEntityItemProcessor();
		FeedEntityItemWriter writer = new FeedEntityItemWriter();
		
		// Prepare mocks
		Mockito.doReturn(stepBuilder).when(stepBuilderFactory).get(any());
		
		// Execute method
		assertNotNull(notificationJobConfiguration.step(reader, processor, writer));
	}
	
	private JobBuilder manufactureJobBuilderPojo() {
		return new JobBuilder("1234")
				.repository(manufactureJobRepository());
	}
	
	private StepBuilder manufactureStepBuilderPojo() {
		return new StepBuilder("1234")
				.repository(manufactureJobRepository())
				.transactionManager(new MongoTransactionManager());
	}
	
	private JobRepository manufactureJobRepository() {
		return new SimpleJobRepository(null, null, null, null);
	}

}
