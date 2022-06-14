package com.flashk.bots.rsstracker.batch;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.flashk.bots.rsstracker.repositories.entities.FeedEntity;
import com.flashk.bots.rsstracker.repositories.entities.ItemEntity;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class FeedEntityItemProcessor implements ItemProcessor<FeedEntity, FeedEntity> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeedEntityItemProcessor.class);
	
	@Autowired
	private TelegramBot bot;
	
	// See: https://gustavopeiretti.com/como-crear-una-aplicacion-con-spring-batch/
	
	@Override
	public FeedEntity process(FeedEntity feed) throws Exception {
		
		// Read the real RSS feed
		Optional<SyndFeed> syndFeed = read(feed.getSourceLink());
		
		// If no feed data could be obtained, don't process this feed.
		if(syndFeed.isEmpty()) {
			return null;
		}
		
		// Compare real RSS feeds against already notified feeds to check
		// if there is any new item
		boolean hasProcessedItems = process(syndFeed.get(), feed);
		
		return (hasProcessedItems) ? feed : null;
	}

	private boolean process(SyndFeed syndFeed, FeedEntity feedEntity) {
		
		// Obtain item lists to compare
		List<SyndEntry> items = syndFeed.getEntries();
		List<ItemEntity> notifiedItems = feedEntity.getNotifiedItems();
		
		boolean hasNotifiedItems = false;
		
		// Iterate through real RSS feed entries to find new items
		Iterator<SyndEntry> entries = items.iterator();
		
		while(entries.hasNext()) {
			
			SyndEntry entry = entries.next();
			
			ItemEntity itemEntity = new ItemEntity();
			itemEntity.setTitle(entry.getTitle());
			itemEntity.setLink(entry.getLink());
			itemEntity.setDescription(entry.getDescription().getValue());
			//itemEntity.setNotificationDate(LocalDateTime.now(ZoneId.systemDefault()));
			
			// Stop processing the feed when finding the first already notified item
			if(notifiedItems.contains(itemEntity)) {
				break;
			}
			
			// Send Telegram notification
			SendResponse notificationResponse = sendNotification(feedEntity, itemEntity);
			
			if(notificationResponse.isOk()) {
				LOGGER.info("New feed item: {}", itemEntity.getTitle());
				hasNotifiedItems = true;
				notifiedItems.add(itemEntity);
			} else {
				LOGGER.warn("Couldn't send telegram notification to user {} and feed id {} (error={}) ", feedEntity.getTelegram().getChatId(), feedEntity.getId(), notificationResponse.errorCode());
			}
			
			
		}
		
		return hasNotifiedItems;
	}
	
	/**
	 * Attempts to send a Telegram notification using the feedEntity and itemEntity information.
	 * @param feedEntity
	 * @param itemEntity
	 * @return
	 */
	private SendResponse sendNotification(FeedEntity feedEntity, ItemEntity itemEntity) {
		
		String messageTemplate = "🆕 <strong>%s</strong>\n<a href='%s'>%s</a>";
		String formatedMessage = String.format(messageTemplate, feedEntity.getTitle(), itemEntity.getLink(), itemEntity.getTitle());

		SendMessage message = new SendMessage(feedEntity.getTelegram().getChatId(), formatedMessage).parseMode(ParseMode.HTML).disableNotification(false);

		return bot.execute(message);
	}
	
	private Optional<SyndFeed> read(String feedUrl) {
		
		try {
			
			URL url = new URL(feedUrl);
			SyndFeedInput input = new SyndFeedInput();	
			return Optional.of(input.build(new XmlReader(url)));
		
		} catch (FeedException | IllegalArgumentException | IOException e) {
			return Optional.empty();
		}
		
	}

}
