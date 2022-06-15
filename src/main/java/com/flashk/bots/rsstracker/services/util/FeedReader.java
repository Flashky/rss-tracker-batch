package com.flashk.bots.rsstracker.services.util;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Component
public class FeedReader {
	
	public Optional<SyndFeed> read(String feedUrl) {
		
		try {
			URL url = new URL(feedUrl);
			SyndFeedInput input = new SyndFeedInput();	
			return Optional.of(input.build(new XmlReader(url)));
		
		} catch (FeedException | IllegalArgumentException | IOException e) {
			return Optional.empty();
		}
		
	}
}
