package com.flashk.bots.rsstracker.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flashk.bots.rsstracker.repositories.entities.FeedEntity;
import com.flashk.bots.rsstracker.repositories.entities.ItemEntity;
import com.flashk.bots.rsstracker.services.util.FeedReader;
import com.flashk.bots.rsstracker.test.utils.Util;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.response.SendResponse;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(MockitoExtension.class)
class FeedEntityItemProcessorTest {

	private static final int NO_ITEMS = 0;
	private static final int TOTAL_ITEMS = 2;
	
	@Spy
	@InjectMocks
	private FeedEntityItemProcessor feedEntityItemProcessor = new FeedEntityItemProcessor();
	
	@Mock
	private FeedReader feedReader;
	
	@Mock
	private TelegramBot bot;
	
	@Mock
	private SendResponse sendResponse;
	
	private static PodamFactory podamFactory;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		Util.disablePodamLogs();
		
	    podamFactory = new PodamFactoryImpl();
	    podamFactory.getStrategy().setDefaultNumberOfCollectionElements(TOTAL_ITEMS);
	    
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testProcess() {
		
		// Prepare POJOs
		FeedEntity feed = podamFactory.manufacturePojo(FeedEntity.class);
		Optional<SyndFeed> syndFeed = Optional.of(manufactureSyndFeedPojo(TOTAL_ITEMS));
		int totalExpectedItems = feed.getNotifiedItems().size() + TOTAL_ITEMS;
		
		// Prepare mocks
		Mockito.doReturn(syndFeed).when(feedReader).read(any());
		Mockito.doReturn(sendResponse).when(bot).execute(any());
		Mockito.doReturn(true).when(sendResponse).isOk();
		
		// Execute method
		FeedEntity result = feedEntityItemProcessor.process(feed);
		
		// Assertions
		assertNotNull(result);
		assertEquals(totalExpectedItems, result.getNotifiedItems().size());
	}
	
	@Test
	void testProcessNoFeedReturnsNull() {
		
		// Prepare POJOs
		FeedEntity feed = podamFactory.manufacturePojo(FeedEntity.class);
		Optional<SyndFeed> syndFeed = Optional.empty();
		
		// Prepare mocks
		Mockito.doReturn(syndFeed).when(feedReader).read(any());
		
		// Execute method
		FeedEntity result = feedEntityItemProcessor.process(feed);
		
		// Assertions
		assertNull(result);
	}
	
	@Test
	void testProcessNoItemsReturnsNull() {
		
		// Prepare POJOs
		FeedEntity feed = podamFactory.manufacturePojo(FeedEntity.class);
		Optional<SyndFeed> syndFeed = Optional.of(manufactureSyndFeedPojo(NO_ITEMS));
		
		// Prepare mocks
		Mockito.doReturn(syndFeed).when(feedReader).read(any());
		
		// Execute method
		FeedEntity result = feedEntityItemProcessor.process(feed);
		
		// Assertions
		assertNull(result);
	}
	
	@Test
	void testProcessNoNewItemsReturnsNull() {
		
		// Prepare POJOs
		FeedEntity feed = podamFactory.manufacturePojo(FeedEntity.class);
		Optional<SyndFeed> syndFeed = Optional.of(manufactureSyndFeedPojo(feed));
		
		// Prepare mocks
		Mockito.doReturn(syndFeed).when(feedReader).read(any());
		
		// Execute method
		FeedEntity result = feedEntityItemProcessor.process(feed);
		
		// Assertions
		assertNull(result);
	}
	
	@Test
	void testProcessOnTelegramNotificationFailureReturnsNull() {
		
		// Prepare POJOs
		FeedEntity feed = podamFactory.manufacturePojo(FeedEntity.class);
		Optional<SyndFeed> syndFeed = Optional.of(manufactureSyndFeedPojo(TOTAL_ITEMS));
		
		// Prepare mocks
		Mockito.doReturn(syndFeed).when(feedReader).read(any());
		Mockito.doReturn(sendResponse).when(bot).execute(any());
		Mockito.doReturn(false).when(sendResponse).isOk();
		
		// Execute method
		FeedEntity result = feedEntityItemProcessor.process(feed);
		
		// Assertions
		assertNull(result);
	}
	
	/**
	 * Builds a SyndFeed using a FeedEntity as example.
	 * @param feedEntity the feed entity to copy from.
	 * @return
	 */
	private SyndFeed manufactureSyndFeedPojo(FeedEntity feedEntity) {
		SyndFeed syndFeed = new SyndFeedImpl();
		
		List<SyndEntry> entries = new ArrayList<>();
		
		for(ItemEntity itemEntity : feedEntity.getNotifiedItems()) {
			entries.add(manufactureSyndEntryPojo(itemEntity));
		}
		
		if(!entries.isEmpty()) {
			syndFeed.setEntries(entries);
		}
		
		return syndFeed;
	}
	
	/**
	 * Builds a SyndEntry using an ItemEntity as example
	 * @param itemEntity the item entity to copy from.
	 * @return
	 */
	private SyndEntry manufactureSyndEntryPojo(ItemEntity itemEntity) {
		
		SyndEntry entry = new SyndEntryImpl();
		entry.setTitle(itemEntity.getTitle());
		entry.setLink(itemEntity.getLink());
		entry.setDescription(manufactureSyndContentPojo(itemEntity.getDescription()));
		
		return entry;
	}

	/**
	 * Builds a SyndContent using the input description as example.
	 * @param description the description to copy from.
	 * @return
	 */
	private SyndContent manufactureSyndContentPojo(String description) {
		SyndContent content = new SyndContentImpl();
		content.setValue(description);
		
		return content;
	}

	private SyndFeed manufactureSyndFeedPojo(int itemsNumber) {
		
		SyndFeed syndFeed = new SyndFeedImpl();
		
		List<SyndEntry> entries = new ArrayList<>();
		
		for(int i = 0; i < itemsNumber; i++) {
			entries.add(manufactureSyndEntryPojo());
		}
		
		if(!entries.isEmpty()) {
			syndFeed.setEntries(entries);
		}
		
		return syndFeed;
	}
	
	private SyndEntry manufactureSyndEntryPojo() {
		
		SyndEntry entry = new SyndEntryImpl();
		entry.setTitle(podamFactory.manufacturePojo(String.class));
		entry.setLink(podamFactory.manufacturePojo(String.class));
		entry.setDescription(manufactureSyndContentPojo());
		
		return entry;
	}

	private SyndContent manufactureSyndContentPojo() {
		SyndContent content = new SyndContentImpl();
		content.setValue(podamFactory.manufacturePojo(String.class));
		
		return content;
	}

}
