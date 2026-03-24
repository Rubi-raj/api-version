package com.rubi.apiversion;

import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.core.env.Environment;

/**
 * This {@link MyBeanRegistrar} class used to register bean programmatically.
 * Check about this more in {@link BeanRegistrar}
 * */

public class MyBeanRegistrar implements BeanRegistrar {
	@Override
	public void register(BeanRegistry registry, Environment env) {
		System.out.println("Register Bean of MyClass.class");
		registry.registerBean(MyClass.class);
	}
}

class MyClass {

}
