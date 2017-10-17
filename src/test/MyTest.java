package test;

import org.junit.Test;

public class MyTest {
	@Test
	public void testLong(){
		Long long1 = 127L;
		Long long2 = 127L;
		System.out.println(long1 == long2);
	}
}
