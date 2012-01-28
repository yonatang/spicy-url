/*******************************************************************************
 * Copyright 2012 Yonatan Graber
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package me.yonatan.spicyurl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.Test;

//TODO - decide how to treat multiple colons in username/password part

@Test
public class UrlTest {

	private void assertUrlParts(Url url, String scheme, String user,
			String pass, String host, int port, String path, String query,
			String freg) {
		assertThat("Scheme good", url.getScheme(), equalTo(scheme));
		assertThat("Host good", url.getHost(), equalTo(host));
		assertThat("Port good", url.getPort(), equalTo(port));
		assertThat("Path good", url.getPath(), equalTo(path));
		assertThat("Query good", url.getQuery(), equalTo(query));
		assertThat("Fregment good", url.getFragment(), equalTo(freg));
		assertThat("User good", url.getUsername(), equalTo(user));
		assertThat("Password good", url.getPassword(), equalTo(pass));

	}

	public void shouldCreateUrl() {
		Url url = new Url("http://abc.com");
		assertUrlParts(url, "http", null, null, "abc.com", -1, null, null, null);
	}

	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyScheme() {
		new Url("://host.com");
	}

	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHost() {
		new Url("http://");
	}

	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHostWithPort() {
		new Url("http://:90");
	}

	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHostWithPath() {
		new Url("http:///path");
	}
	
	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHostWithPathAndQuery() {
		new Url("http:///path?query");
	}
	
	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHostWithPathAndQueryAndFrag() {
		new Url("http:///path?query#fragment");
	}
	
	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHostWithQuery() {
		new Url("http://?query");
	}
	
	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHostWithQueryAndFrag() {
		new Url("http://?query#fragment");
	}
	
	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHostWithFrag() {
		new Url("http://#fragment");
	}
	
	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHostPort() {
		new Url("http://:90");
	}
	
	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHostPortAndEmptyPath() {
		new Url("http://:90/");
	}
	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyHostPortAndPath() {
		new Url("http://:90/path");
	}


	public void shouldCreateUrlWithPort() {
		Url url = new Url("http://abc.com:90");
		assertUrlParts(url, "http", null, null, "abc.com", 90, null, null, null);
	}

	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnNonNumberPort() {
		new Url("http://abc.com:a90");
	}

	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnNegativePort() {
		new Url("http://abc.com:-90");
	}

	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnZeroPort() {
		new Url("http://abc.com:0");
	}

	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnEmptyPort() {
		new Url("http://abc.com:");
	}

	@Test(expectedExceptions = MalformedUrlException.class)
	public void shouldFailOnLargePort() {
		new Url("http://abc.com:65536");
	}

	public void shouldParseLogin() {
		Url url = new Url("http://user:pass@host.com");
		assertUrlParts(url, "http", "user", "pass", "host.com", -1, null, null,
				null);
	}

	public void shouldParseLoginWithPort() {
		Url url = new Url("http://user:pass@host.com:90");
		assertUrlParts(url, "http", "user", "pass", "host.com", 90, null, null,
				null);
	}

	public void shouldParseLoginWithAtInUser() {
		Url url = new Url("http://us@er:pass@host.com");
		assertUrlParts(url, "http", "us@er", "pass", "host.com", -1, null,
				null, null);
	}

	public void shouldParseLoginWithAtInPass() {
		Url url = new Url("http://user:p@ss@host.com");
		assertUrlParts(url, "http", "user", "p@ss", "host.com", -1, null, null,
				null);
	}

	public void shouldParseLoginUserOnly() {
		Url url = new Url("http://user@host.com");
		assertUrlParts(url, "http", "user", null, "host.com", -1, null, null,
				null);
	}

	public void shouldParseLoginEmptyPass() {
		Url url = new Url("http://user:@host.com");
		assertUrlParts(url, "http", "user", "", "host.com", -1, null, null,
				null);
	}

	public void shouldParseLoginEmptyUser() {
		Url url = new Url("http://:pass@host.com");
		assertUrlParts(url, "http", "", "pass", "host.com", -1, null, null,
				null);
	}

	public void shouldParsePath() {
		Url url = new Url("http://host.com/a/b/c");
		assertUrlParts(url, "http", null, null, "host.com", -1, "a/b/c", null,
				null);
	}
	
	public void shouldParseEmptyPath() {
		Url url = new Url("http://host.com/");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", null,
				null);
	}
	
	public void shouldParseEmptyPathWithQuery() {
		Url url = new Url("http://host.com/?query=2");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", "query=2",
				null);
	}
	
	public void shouldParseEmptyPathWithQueryAndFrag() {
		Url url = new Url("http://host.com/?query=2#fragment");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", "query=2",
				"fragment");
	}
	
	public void shouldParseEmptyPathWithQueryAndEmptyFrag() {
		Url url = new Url("http://host.com/?query=2#");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", "query=2",
				"");
	}
	
	public void shouldParseEmptyPathWithQuery2() {
		Url url = new Url("http://host.com?query=2");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "query=2",
				null);
	}
	
	public void shouldParseEmptyPathWithQuery2AndFrag() {
		Url url = new Url("http://host.com?query=2#fragment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "query=2",
				"fragment");
	}
	
	public void shouldParseEmptyPathWithQuery2AndEmptyFrag() {
		Url url = new Url("http://host.com?query=2#");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "query=2",
				"");
	}
	
	public void shouldParseEmptyPathWithQuery3() {
		Url url = new Url("http://host.com?que/ry=2");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "que/ry=2",
				null);
	}
	
	public void shouldParseEmptyPathWithQuery3AndFrag() {
		Url url = new Url("http://host.com?que/ry=2#fragment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "que/ry=2",
				"fragment");
	}
	
	public void shouldParseEmptyPathWithQuery4AndFrag() {
		Url url = new Url("http://host.com?query=2#fr/agment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "query=2",
				"fr/agment");
	}
	
	public void shouldParseEmptyPathWithQuery3AndEmptyFrag() {
		Url url = new Url("http://host.com?que/ry=2#");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "que/ry=2",
				"");
	}
	
	public void shouldParseEmptyPathWithFrag() {
		Url url = new Url("http://host.com/#fragment");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", null,
				"fragment");
	}
	
	public void shouldParseEmptyPathWithFrag2() {
		Url url = new Url("http://host.com#fragment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, null,
				"fragment");
	}
	
	public void shouldParseEmptyPathWithFrag3() {
		Url url = new Url("http://host.com#frag/ment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, null,
				"frag/ment");
	}
	
	public void shouldParseEmptyPathWithFrag4() {
		Url url = new Url("http://host.com#frag?ment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, null,
				"frag?ment");
	}
}
