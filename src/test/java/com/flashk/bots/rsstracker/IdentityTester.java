package com.flashk.bots.rsstracker;

import com.openpojo.reflection.PojoClass;
import com.openpojo.validation.test.Tester;
import com.openpojo.validation.utils.ValidationHelper;

public class IdentityTester implements Tester {

	@Override
	public void run(PojoClass pojoClass) {
		Object firstPojoClassInstance;
		Object secondPojoClassInstance;
		firstPojoClassInstance = ValidationHelper.getMostCompleteInstance(pojoClass);
		secondPojoClassInstance = ValidationHelper.getMostCompleteInstance(pojoClass);
		
		firstPojoClassInstance.equals(secondPojoClassInstance);
		firstPojoClassInstance.equals(new Object());
		firstPojoClassInstance.equals(firstPojoClassInstance);
		firstPojoClassInstance.equals(null);
		firstPojoClassInstance.hashCode();
		firstPojoClassInstance.toString();
	}

}
