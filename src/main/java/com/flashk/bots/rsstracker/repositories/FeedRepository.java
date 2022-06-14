package com.flashk.bots.rsstracker.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.flashk.bots.rsstracker.repositories.entities.FeedEntity;

public interface FeedRepository extends MongoRepository<FeedEntity, String> {

	/**
	 * Finds all feeds from the specified Telegram userId.
	 * @param userId unique telegram user identifier.
	 * @param pageable the pageable to request a paged result, can be Pageable.unpaged(), must not be null.
	 * @return
	 */
	Page<FeedEntity> findByTelegramUserId(Long userId, Pageable pageable);
}
