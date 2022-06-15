package com.flashk.bots.rsstracker.test.utils;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class Util {

	public static final void setTestingLogLevel(Level level) {
		setTestingLogLevel("uk.co.jemos.podam.api", level);
		setTestingLogLevel("com.flashk.bots.rsstracker", level);
		setTestingLogLevel("com.openpojo", level);
	}
	
	private static void setTestingLogLevel(String name, Level level) {
		Logger root = (Logger) LoggerFactory.getLogger(name);
		root.setLevel(level);
	}
	private Util() {}
	
}
