package org.spicyurl;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.testng.annotations.Test;

@Test
public class MalformedUrlExceptionTest {

	public void shouldGenerateSimpleExceptionWithCause() {
		NullPointerException npe = new NullPointerException("NPE");
		MalformedUrlException m = new MalformedUrlException("http://url.com", npe);
		assertThat("Message is correct", m.getMessage(),
				allOf(containsString("http://url.com"), containsString("Unknown")));
		assertThat("Cause is correct", m.getCause(), equalTo((Throwable) npe));
	}

	public void shouldGenerateSimpleException() {
		MalformedUrlException m = new MalformedUrlException("http://url.com");
		assertThat("Message is correct", m.getMessage(),
				allOf(containsString("http://url.com"), containsString("Unknown")));
	}

	public void shouldGenerateFromErrWithNoParam() {
		MalformedUrlException m = new MalformedUrlException("http://url.com", UrlErrors.HOST_IS_MISSING);
		assertThat("Message is correct", m.getMessage(),
				allOf(containsString("http://url.com"), containsString("Host is missing")));
	}

	public void shouldGenerateFromErrWithCauseAndNoParam() {
		NullPointerException npe = new NullPointerException("NPE");
		MalformedUrlException m = new MalformedUrlException("http://url.com", UrlErrors.HOST_IS_MISSING, npe);
		assertThat("Message is correct", m.getMessage(),
				allOf(containsString("http://url.com"), containsString("Host is missing")));
		assertThat("Cause is correct", m.getCause(), equalTo((Throwable) npe));
	}

	public void shouldGenerateFromErrWithParam() {
		NullPointerException npe = new NullPointerException("NPE");
		MalformedUrlException m = new MalformedUrlException("http://url.com", UrlErrors.INVALID_PORT_VALUE, npe, 65536);
		assertThat(
				"Message is correct",
				m.getMessage(),
				allOf(containsString("http://url.com"), containsString("Port " + MessageFormat.format("{0}", 65536)
						+ " is invalid")));
		assertThat("Cause is correct", m.getCause(), equalTo((Throwable) npe));
	}

	public void shouldGenerateFromString() {
		MalformedUrlException m = new MalformedUrlException("http://url.com", "Message!");
		assertThat("Message is correct", m.getMessage(),
				allOf(containsString("http://url.com"), containsString("Message!")));
	}

	public void shouldGenerateFromStringWithCause() {
		NullPointerException npe = new NullPointerException("NPE");
		MalformedUrlException m = new MalformedUrlException("http://url.com", "Message!", npe);
		assertThat("Message is correct", m.getMessage(),
				allOf(containsString("http://url.com"), containsString("Message!")));
		assertThat("Cause is correct", m.getCause(), equalTo((Throwable) npe));
	}

	public void shouldHaveResourcesForAllErrors() {
		ResourceBundle rb = ResourceBundle.getBundle("org/spicyurl/ErrorMessages");
		for (UrlErrors e : UrlErrors.values()) {
			assertThat("ResourceBundle contains key", rb.getString(e.name()), notNullValue());
			UrlErrors.valueOf(e.name());
		}
	}

}
