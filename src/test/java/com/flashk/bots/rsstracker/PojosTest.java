package com.flashk.bots.rsstracker;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flashk.bots.rsstracker.test.utils.Util;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.EqualsAndHashCodeMatchRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

import ch.qos.logback.classic.Level;

class PojosTest {

	//private static final String POJO_PACKAGE = "com.flashk.bots.rsstracker.repositories.entities";
	
	private static Validator validator;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		Util.setTestingLogLevel(Level.OFF);
		
		validator = ValidatorBuilder.create()
        		// Rules
        		.with(new EqualsAndHashCodeMatchRule())
        		
        		// Testers
                .with(new SetterTester())
                .with(new GetterTester())
                .with(new IdentityTester())
                .build();
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void test() {
		validator.validateRecursively(this.getClass().getPackage().getName(),
				new FilterPackageInfo(),
				new FilterTestClasses());
		
	}

}
