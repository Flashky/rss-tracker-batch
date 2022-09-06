package com.flashk.bots.rsstracker.batch;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.flashk.bots.rsstracker.repositories.entities.FeedEntity;
import com.flashk.bots.rsstracker.repositories.entities.ItemEntity;
import com.flashk.bots.rsstracker.services.util.FeedReader;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public class FeedEntityItemProcessor implements ItemProcessor<FeedEntity, FeedEntity> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeedEntityItemProcessor.class);
	
	@Autowired
	private FeedReader feedReader;
	
	@Autowired
	private TelegramBot bot;
	
	@Override
	public FeedEntity process(FeedEntity feed) {
		
		// Processed item will be stored here
		FeedEntity result = null;
		
		// Read the real RSS feed
		Optional<SyndFeed> syndFeed = feedReader.read(feed.getSourceLink());
		
		// If no feed data could be obtained, don't process this feed.
		if(syndFeed.isEmpty()) {
			return result;
		}
	
		// Obtain item lists to compare
		List<SyndEntry> items = syndFeed.get().getEntries();
		List<ItemEntity> notifiedItems = feed.getNotifiedItems();
		
		// Iterate through real RSS feed entries to find new items
		Iterator<SyndEntry> entries = items.iterator();
	
		// Compare online RSS feed entires against already notified entries to find and notify new items
		while(entries.hasNext()) {
			
			SyndEntry entry = entries.next();
			
			ItemEntity item = new ItemEntity();
			item.setTitle(entry.getTitle());
			item.setLink(entry.getLink());
			
			// Stop processing the feed when finding the first already notified item
			if(notifiedItems.contains(item)) {
				break;
			}
			
			// Send Telegram notification
			SendResponse notificationResponse = sendNotification(feed, item);
			
			if(notificationResponse.isOk()) {
				LOGGER.info("New feed item: {}", item.getTitle());
				result = feed;
				notifiedItems.add(item);
			} else {
				LOGGER.warn("Couldn't send telegram notification to user {} and feed id {} (error={}) ", feed.getTelegram().getChatId(), feed.getId(), notificationResponse.errorCode());
			}
			
			
		}
		
		return result;
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
	
}
