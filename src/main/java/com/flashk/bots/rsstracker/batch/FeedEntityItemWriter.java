package com.flashk.bots.rsstracker.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.flashk.bots.rsstracker.repositories.FeedRepository;
import com.flashk.bots.rsstracker.repositories.entities.FeedEntity;

public class FeedEntityItemWriter implements ItemWriter<FeedEntity> {

	// See: https://gustavopeiretti.com/como-crear-una-aplicacion-con-spring-batch/
	
	@Autowired
	private FeedRepository feedRepository;
	
	@Override
	public void write(List<? extends FeedEntity> items) throws Exception {
		
		for(FeedEntity item : items) {
			
			// Only save if exists.
			// Just in case the user deleted the feed
			// while the batch job was still processing it.
			if(feedRepository.existsById(item.getId())) {
				feedRepository.save(item);
			}
			
		}
		
	}

}
