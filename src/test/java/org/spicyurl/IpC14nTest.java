package org.spicyurl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import lombok.AllArgsConstructor;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class IpC14nTest {

	public void shouldParseIpV4Properly() {
		IpC14n c14n = new IpC14n();
		@AllArgsConstructor
		class TestObj {
			String octat;
			int ip;
		}
		//@formatter:off
		TestObj[] tests = new TestObj[] { 
			new TestObj("123", 123),
			new TestObj("07",7),
			new TestObj("010", 8),
			new TestObj("0000000000010", 8),
			new TestObj("0x10", 16),
			new TestObj("0x00000000010", 16),
			new TestObj("0xa", 10),
			new TestObj("0xA", 10),
		};

		//@formatter:on
		for (TestObj test : tests) {
			System.out.println(test.octat);
			assertThat(c14n.parseIpv4Octat(test.octat), is(test.ip));
		}
	}

	public void shouldFailOnIpV4Gracefully() {
		IpC14n c14n = new IpC14n();

		//@formatter:off
		String[] tests=new String[]{
			"00x123",
			"0138",
			"0x123g",
			String.valueOf((long)Integer.MAX_VALUE+1),
			"-1",
		};
		//@formatter:on

		for (String test : tests) {
			try {
				c14n.parseIpv4Octat(test);
				Assert.fail("Should throw IllegalArgumentException on " + test);
			} catch (IllegalArgumentException e) {

			}
		}
	}
}
