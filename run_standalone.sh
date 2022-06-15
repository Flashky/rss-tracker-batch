#!/bin/bash

# Telegram Bot Token
export TELEGRAM_BOT_TOKEN=YOUR_BOT_TOKEN

# MongoDB configuration
export MONGODB_HOST=localhost
export MONGODB_PORT=27017
export MONGODB_USERNAME=admin
export MONGODB_PASSWORD=admin
export MONGODB_DATABASE=rss_tracker_db
export JOB_FIXED_DELAY_SECONDS=60

# Variables
JAR_PATH=target
JAR=rss-tracker-batch-0.0.1-SNAPSHOT.jar

# Service execution
java -jar "${JAR_PATH}/${JAR}"
