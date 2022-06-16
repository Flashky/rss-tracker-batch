package com.flashk.bots.rsstracker.services.util;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rometools.rome.feed.synd.SyndFeed;

class FeedReaderTest {

	private static final String SAMPLE_FEED = "http://createfeed.fivefilters.org/extract.php?url=https%3A%2F%2Fwww.mediavida.com%2Fforo%2Fclub-hucha&in_id_or_class=hb&max=5&order=document&guid=0";
	private static final String SAMPLE_INVALID_FEED = "XXX";
	
	private FeedReader feedReader = new FeedReader();
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testRead() {
		
		// Execute method
		Optional<SyndFeed> result = feedReader.read(SAMPLE_FEED);
		
		// Assertions
		assertTrue(result.isPresent());
		
	}
	
	@Test
	void testReadInvalidFeed() {
		
		// Execute method
		Optional<SyndFeed> result = feedReader.read(SAMPLE_INVALID_FEED);
		
		// Assertions
		assertTrue(result.isEmpty());
		
	}

}
