package com.flashk.bots.rsstracker.scheduler;

import static org.junit.Assert.fail;
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
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

@ExtendWith(MockitoExtension.class)
class JobSchedulerTest {

	@Spy
	@InjectMocks
	private JobScheduler jobScheduler;
	
	@Mock
	private JobLauncher jobLauncher;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testRunJob() {
		
		// Execute method
		jobScheduler.runJob();
		
		// Assertions
		try {
			Mockito.verify(jobLauncher).run(any(), any());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			fail("Unexpected exception: "+e.getMessage());
		}
	}
	
	@Test
	void testRunJobWithJobExecutionAlreadyRunningException() {
		
		try {
			
			Mockito.doThrow(new JobExecutionAlreadyRunningException("error")).when(jobLauncher).run(any(), any());
			
			// Execute method
			jobScheduler.runJob();
			
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			fail("Exception should have been handled: "+e.getMessage());
		}

	}
	
	@Test
	void testRunJobWithJobRestartException() {
		
		try {
			
			Mockito.doThrow(new JobRestartException("error")).when(jobLauncher).run(any(), any());
			
			// Execute method
			jobScheduler.runJob();
			
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			fail("Exception should have been handled: "+e.getMessage());
		}

	}
	
	@Test
	void testRunJobWithJobInstanceAlreadyCompleteException() {
		
		try {
			
			Mockito.doThrow(new JobInstanceAlreadyCompleteException("error")).when(jobLauncher).run(any(), any());
			
			// Execute method
			jobScheduler.runJob();
			
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			fail("Exception should have been handled: "+e.getMessage());
		}

	}
	
	@Test
	void testRunJobWithJobParametersInvalidException() {
		
		try {
			
			Mockito.doThrow(new JobParametersInvalidException("error")).when(jobLauncher).run(any(), any());
			
			// Execute method
			jobScheduler.runJob();
			
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			fail("Exception should have been handled: "+e.getMessage());
		}

	}

}
