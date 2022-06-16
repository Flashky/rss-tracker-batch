package com.flashk.bots.rsstracker;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoClassFilter;

public class FilterTestClasses implements PojoClassFilter {

	@Override
	public boolean include(PojoClass pojoClass) {

		return !pojoClass.getSourcePath().contains("/test-classes/");
	}

}
