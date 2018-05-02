package com.suma.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BUGTest {

	@Before
	public void setUp() throws Exception {
		System.out.println(111);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println(000);
	}

//	@Test
//	public void testCollectInfo() {
//	}

	@Test
	public void testDateForBug() {
		System.out.println(new BUG_HUNan().dateForBug());
	}

}
