package org.spicyurl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import lombok.AllArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class IpC14nTest {

	public void shouldParseIpV4OctatProperly() {
		IpC14n c14n = new IpC14n();
		@AllArgsConstructor
		class TestObj {
			String octat;
			long ip;
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

	public void shouldFailOnIpV4OctatGracefully() {
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

	public void shouldParseIpV4Properly() throws IOException {
		IpC14n c14n = new IpC14n();

		Properties p = new Properties();
		p.load(this.getClass().getResourceAsStream("ip.c14n.cannonicalization.properties"));

		for (Entry<Object, Object> entry : p.entrySet()) {
			String in = entry.getKey().toString();
			String exp = entry.getValue().toString();
			byte[] octats = null;
			if (StringUtils.isNotBlank(exp)) {
				octats = new byte[4];
				int i = 0;
				for (String octat : StringUtils.split(exp, '.')) {
					octats[i++] = (byte) Integer.parseInt(octat);
				}
			}
			try {
				assertThat("Assert correct answer for " + in, c14n.c14nIpV4(in), is(octats));
			} catch (MalformedIpException e) {
				if (octats != null) {
					Assert.fail("Should get for " + in + " - " + exp + ", but got " + e);
				}
			}
		}
		// @AllArgsConstructor
		// class TestObj {
		// String in;
		// byte[] exp;
		// }
//		//@formatter:off
//		TestObj[] tests=new TestObj[]{
//			new TestObj("10.138",new byte[]{10,0,0,(byte)138}),
//			new TestObj("10.0.138",new byte[]{10,0,0,(byte)138}),
//			new TestObj("10.256",new byte[]{10,0,1,0}),
//			new TestObj("10.0.256",new byte[]{10,0,1,0}),
//			
//		};
//		//@formatter:on
		//
		// for (TestObj test : tests) {
		// assertThat("Parse ip " + test.in, c14n.c14nIpV4(test.in),
		// is(test.exp));
		// }
	}

	public void t() {
		IpC14n c = new IpC14n();
		c.c14nIpV4("3279880203");
	}
}
