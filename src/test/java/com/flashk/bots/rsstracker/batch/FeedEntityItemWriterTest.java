package com.flashk.bots.rsstracker.batch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flashk.bots.rsstracker.repositories.FeedRepository;
import com.flashk.bots.rsstracker.repositories.entities.FeedEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class FeedEntityItemWriterTest {

	private static final int NO_ITEMS = 0;
	private static final int TOTAL_ITEMS = 2;
	
	@Spy
	@InjectMocks
	private FeedEntityItemWriter feedEntityItemWriter = new FeedEntityItemWriter();
	
	@Mock
	private FeedRepository feedRepository;
	
	private static PodamFactory podamFactory;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	    podamFactory = new PodamFactoryImpl();
	    podamFactory.getStrategy().setDefaultNumberOfCollectionElements(TOTAL_ITEMS);
	    
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testWrite() throws Exception {

		// Prepare POJOs
		List<FeedEntity> feedEntities = podamFactory.manufacturePojo(ArrayList.class, FeedEntity.class);
		
		// Prepare mocks
		Mockito.doReturn(true).when(feedRepository).existsById(any());
		
		// Execute method
		feedEntityItemWriter.write(feedEntities);
		
		// Assertions
		
		// Execute as many times as items are to be saved
		Mockito.verify(feedRepository, times(TOTAL_ITEMS)).save(any());
	}
	
	@Test
	void testWriteNonExistingItemsMustNotBeSaved() throws Exception {

		// Prepare POJOs
		List<FeedEntity> feedEntities = podamFactory.manufacturePojo(ArrayList.class, FeedEntity.class);
		
		// Prepare mocks
		Mockito.doReturn(false).when(feedRepository).existsById(any());
		
		// Execute method
		feedEntityItemWriter.write(feedEntities);
		
		// Assertions
		
		// No items should be saved
		Mockito.verify(feedRepository, times(NO_ITEMS)).save(any());
	}
	
	@Test
	void testWriteEmptyItemListMustNotBeSaved() throws Exception {

		// Prepare POJOs
		List<FeedEntity> feedEntities = new ArrayList<>();
		
		// Execute method
		feedEntityItemWriter.write(feedEntities);
		
		// Assertions
		
		// No items should be saved
		Mockito.verify(feedRepository, times(NO_ITEMS)).save(any());
	}

}
