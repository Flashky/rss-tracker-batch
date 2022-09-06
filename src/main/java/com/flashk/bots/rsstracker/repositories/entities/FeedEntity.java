package com.flashk.bots.rsstracker.repositories.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "rssFeeds", collation = "{ 'locale' :  'es' }")
public class FeedEntity {

	/**
	 * Unique feed identifier.
	 */
	@Id
	private String id;
	
	/**
	 * Telegram feed owner.
	 */
	private TelegramEntity telegram;
	
	/** 
	 * The base link where the feed is located.
	 */
	private String sourceLink;
	
	/**
	 * RSS feed title field
	 */
	private String title;
	
	/**
	 * RSS feed link field.
	 */
	private String link;
	

	/**
	 * Defines if the RSS feed has Telegram notifications enabled.
	 */
	private Boolean isEnabled = true;
	
	/**
	 * Already notified items from the feed.
	 */
	private List<ItemEntity> notifiedItems = new ArrayList<>();
	
	// Auditing fields
	
	@CreatedDate
	@JsonFormat(timezone = "GMT+02:00")
	private Date createdDate;
	
	@LastModifiedDate
	@JsonFormat(timezone = "GMT+02:00")
	private Date lastModifiedDate;

	
}
