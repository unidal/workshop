package com.site.kernel;

import com.site.kernel.common.BaseTestCase;

public class SystemRegistryTest extends BaseTestCase {
	public void testRegister() {
		SystemRegistry.register(BaseTestCase.class, "register subclass", SystemRegistryTest.class);
		SystemRegistry.register(BaseTestCase.class, "register instance", new SystemRegistryTest());

		try {
			SystemRegistry.register(BaseTestCase.class, "register non-subclass", Object.class);

			fail("Exception expected");
		} catch (RuntimeException e) {
			// should be thrown
		}

		try {
			SystemRegistry.register(BaseTestCase.class, "register non-instance", new Object());

			fail("Exception expected");
		} catch (RuntimeException e) {
			// should be thrown
		}
	}

	public void testLookup() {
		SystemRegistry.clear(BaseTestCase.class);
		SystemRegistry.register(BaseTestCase.class, "lookup subclass", SystemRegistryTest.class);
		SystemRegistry.register(BaseTestCase.class, "lookup instance", new SystemRegistryTest());

		SystemRegistry.lookup(BaseTestCase.class, "lookup subclass");
		SystemRegistry.lookup(BaseTestCase.class, "lookup instance");

		try {
			SystemRegistry.lookup(BaseTestCase.class, "lookup non-subclass");

			fail("Exception expected");
		} catch (RuntimeException e) {
			// should be thrown
		}

		try {
			SystemRegistry.lookup(BaseTestCase.class, "lookup non-instance");

			fail("Exception expected");
		} catch (RuntimeException e) {
			// should be thrown
		}
	}

	public void testNewInstance() {
		SystemRegistry.clear(BaseTestCase.class);
		SystemRegistry.register(BaseTestCase.class, "newinstance subclass", SystemRegistryTest.class);
		SystemRegistry.register(BaseTestCase.class, "newinstance non-subclass", BaseTestCase.class);

		SystemRegistry.newInstance(BaseTestCase.class, "newinstance subclass");

		try {
			SystemRegistry.newInstance(BaseTestCase.class, "newinstance non-subclass");

			fail("Exception expected");
		} catch (RuntimeException e) {
			// should be thrown
		}
	}
}
