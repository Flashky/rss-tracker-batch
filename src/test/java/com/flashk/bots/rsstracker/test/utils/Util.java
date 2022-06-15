package com.flashk.bots.rsstracker.test.utils;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class Util {

	public static final void setTestingLogLevel(Level level) {
		
		 Logger root = (Logger) LoggerFactory.getLogger("uk.co.jemos.podam.api");
	     root.setLevel(level);
	     
	     root = (Logger) LoggerFactory.getLogger("com.flashk.bots.rsstracker");
	     root.setLevel(level);
	}
	
	private Util() {}
	
}
