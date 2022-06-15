package com.flashk.bots.rsstracker.test.utils;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class Util {

	public static final void disablePodamLogs() {
		 Logger root = (Logger) LoggerFactory.getLogger("uk.co.jemos.podam.api");
	     root.setLevel(Level.OFF);
	}
	
	private Util() {}
	
}
