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
package org.spicyurl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.spicyurl.UrlErrors.HOST_IS_MISSING;
import static org.spicyurl.UrlErrors.INVALID_PORT_VALUE;
import static org.spicyurl.UrlErrors.SCHEME_IS_MISSING;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.annotations.Test;

@Test
public class UrlTest {

	private void assertUrlParts(Url url, String scheme, String user, String pass, String host, int port, String path,
			String query, String freg) {
		assertThat("Scheme good", url.getScheme(), equalTo(scheme));
		assertThat("Host good", url.getHost(), equalTo(host));
		assertThat("Port good", url.getPort(), equalTo(port));
		assertThat("Path good", url.getPath(), equalTo(path));
		assertThat("Query good", url.getQuery(), equalTo(query));
		assertThat("Fregment good", url.getFragment(), equalTo(freg));
		assertThat("User good", url.getUsername(), equalTo(user));
		assertThat("Password good", url.getPassword(), equalTo(pass));
		assertThat("Url is valid", url.isValid());
	}

	private void assertInvalidUrl(Url url, UrlErrors... errors) {
		assertThat("Url is invalid", url.isValid(), equalTo(false));
		assertThat("Url has the right validation error", url.getValiationErrors(), hasItems(errors));
		assertThat("No more, nor less amount of errors has found", url.getValiationErrors().size(),
				equalTo(errors.length));
	}

	public void shouldCreateUrl() {
		Url url = new Url("http://abc.com");
		assertUrlParts(url, "http", null, null, "abc.com", -1, null, null, null);
	}

	public void shouldFailOnEmptyScheme() {
		Url url = new Url("://host.com");
		assertInvalidUrl(url, SCHEME_IS_MISSING);
	}

	public void shouldFailOnEmptyHost() {
		Url url = new Url("http://");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostAndPort() {
		Url url = new Url("http://:");
		assertInvalidUrl(url, HOST_IS_MISSING, INVALID_PORT_VALUE);
	}

	public void shouldFailMissingSchemeSeperator() {
		Url url = new Url("http:/host.com");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailMissingSchemeSeperator2() {
		Url url = new Url("http//host.com");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailMissingSchemeSeperator3() {
		Url url = new Url("http/host.com");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailMissingSchemeSeperator4() {
		Url url = new Url("httphost.com");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostWithPort() {
		Url url = new Url("http://:90");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostWithPath() {
		Url url = new Url("http:///path");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostWithPathAndQuery() {
		Url url = new Url("http:///path?query");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostWithPathAndQueryAndFrag() {
		Url url = new Url("http:///path?query#fragment");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostWithQuery() {
		Url url = new Url("http://?query");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostWithQueryAndFrag() {
		Url url = new Url("http://?query#fragment");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostWithFrag() {
		Url url = new Url("http://#fragment");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostPort() {
		Url url = new Url("http://:90");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostPortAndEmptyPath() {
		Url url = new Url("http://:90/");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldFailOnEmptyHostPortAndPath() {
		Url url = new Url("http://:90/path");
		assertInvalidUrl(url, HOST_IS_MISSING);
	}

	public void shouldCreateUrlWithPort() {
		Url url = new Url("http://abc.com:90");
		assertUrlParts(url, "http", null, null, "abc.com", 90, null, null, null);
	}

	public void shouldFailOnNonNumberPort() {
		Url url = new Url("http://abc.com:a90");
		assertInvalidUrl(url, INVALID_PORT_VALUE);
	}

	public void shouldFailOnNegativePort() {
		Url url = new Url("http://abc.com:-90");
		assertInvalidUrl(url, INVALID_PORT_VALUE);
	}

	public void shouldFailOnZeroPort() {
		Url url = new Url("http://abc.com:0");
		assertInvalidUrl(url, INVALID_PORT_VALUE);
	}

	public void shouldFailOnEmptyPort() {
		Url url = new Url("http://abc.com:");
		assertInvalidUrl(url, INVALID_PORT_VALUE);
	}

	public void shouldFailOnLargePort() {
		Url url = new Url("http://abc.com:65536");
		assertInvalidUrl(url, INVALID_PORT_VALUE);
	}

	public void shouldFailOnMultipleColonsInHost() {
		Url url = new Url("http://abc.com:65:65536");
		assertInvalidUrl(url, INVALID_PORT_VALUE);
	}

	public void shouldFailOnVeryVeryLargePort() {
		Url url = new Url("http://abc.com:" + Long.MAX_VALUE);
		assertInvalidUrl(url, INVALID_PORT_VALUE);
	}

	public void shouldParseLogin() {
		Url url = new Url("http://user:pass@host.com");
		assertUrlParts(url, "http", "user", "pass", "host.com", -1, null, null, null);
	}

	public void shouldParseLoginWithPort() {
		Url url = new Url("http://user:pass@host.com:90");
		assertUrlParts(url, "http", "user", "pass", "host.com", 90, null, null, null);
	}

	public void shouldParseEmptyLogin() {
		Url url = new Url("http://@host.com");
		assertUrlParts(url, "http", "", null, "host.com", -1, null, null, null);
	}

	public void shouldParseLoginWithAtInUser() {
		Url url = new Url("http://us@er:pass@host.com");
		assertUrlParts(url, "http", "us@er", "pass", "host.com", -1, null, null, null);
	}

	public void shouldParseLoginWithAtInPass() {
		Url url = new Url("http://user:p@ss@host.com");
		assertUrlParts(url, "http", "user", "p@ss", "host.com", -1, null, null, null);
	}

	public void shouldParseLoginUserOnly() {
		Url url = new Url("http://user@host.com");
		assertUrlParts(url, "http", "user", null, "host.com", -1, null, null, null);
	}

	public void shouldParseLoginEmptyPass() {
		Url url = new Url("http://user:@host.com");
		assertUrlParts(url, "http", "user", "", "host.com", -1, null, null, null);
	}

	public void shouldParseLoginEmptyUser() {
		Url url = new Url("http://:pass@host.com");
		assertUrlParts(url, "http", "", "pass", "host.com", -1, null, null, null);
	}

	public void shouldParsePath() {
		Url url = new Url("http://host.com/a/b/c");
		assertUrlParts(url, "http", null, null, "host.com", -1, "a/b/c", null, null);
	}

	public void shouldParseEmptyPath() {
		Url url = new Url("http://host.com/");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", null, null);
	}

	public void shouldParseEmptyPathWithQuery() {
		Url url = new Url("http://host.com/?query=2");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", "query=2", null);
	}

	public void shouldParseEmptyPathWithQueryAndFrag() {
		Url url = new Url("http://host.com/?query=2#fragment");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", "query=2", "fragment");
	}

	public void shouldParseEmptyPathWithQueryAndEmptyFrag() {
		Url url = new Url("http://host.com/?query=2#");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", "query=2", "");
	}

	public void shouldParseEmptyPathWithQuery2() {
		Url url = new Url("http://host.com?query=2");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "query=2", null);
	}

	public void shouldParseEmptyPathWithQuery2AndFrag() {
		Url url = new Url("http://host.com?query=2#fragment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "query=2", "fragment");
	}

	public void shouldParseEmptyPathWithQuery2AndEmptyFrag() {
		Url url = new Url("http://host.com?query=2#");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "query=2", "");
	}

	public void shouldParseEmptyPathWithQuery3() {
		Url url = new Url("http://host.com?que/ry=2");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "que/ry=2", null);
	}

	public void shouldParseEmptyPathWithQuery3AndFrag() {
		Url url = new Url("http://host.com?que/ry=2#fragment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "que/ry=2", "fragment");
	}

	public void shouldParseEmptyPathWithQuery4AndFrag() {
		Url url = new Url("http://host.com?query=2#fr/agment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "query=2", "fr/agment");
	}

	public void shouldParseEmptyPathWithQuery3AndEmptyFrag() {
		Url url = new Url("http://host.com?que/ry=2#");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "que/ry=2", "");
	}

	public void shouldParseEmptyPathWithFrag() {
		Url url = new Url("http://host.com/#fragment");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", null, "fragment");
	}

	public void shouldParseEmptyPathWithEmptyFrag() {
		Url url = new Url("http://host.com/#");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", null, "");
	}

	public void shouldParseEmptyPathWithEmptyQuery() {
		Url url = new Url("http://host.com/?");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", "", null);
	}

	public void shouldParseEmptyPathWithEmptyQueryAndEmptyFrag() {
		Url url = new Url("http://host.com/?#");
		assertUrlParts(url, "http", null, null, "host.com", -1, "", "", "");
	}

	public void shouldParseEmptyFrag() {
		Url url = new Url("http://host.com#");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, null, "");
	}

	public void shouldParseEmptyQuery() {
		Url url = new Url("http://host.com?");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "", null);
	}

	public void shouldParseEmptyQueryAndEmptyFrag() {
		Url url = new Url("http://host.com?#");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, "", "");
	}

	public void shouldParseEmptyPathWithFrag2() {
		Url url = new Url("http://host.com#fragment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, null, "fragment");
	}

	public void shouldParseEmptyPathWithFrag3() {
		Url url = new Url("http://host.com#frag/ment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, null, "frag/ment");
	}

	public void shouldParseEmptyPathWithFrag4() {
		Url url = new Url("http://host.com#frag?ment");
		assertUrlParts(url, "http", null, null, "host.com", -1, null, null, "frag?ment");
	}

	public void shouldConvertToURI() throws URISyntaxException {
		String urlStr = "http://uri.com";
		Url url = new Url(urlStr);
		assertThat("URI convertion good", url.asURI(), equalTo(new URI(urlStr)));
	}

	public void shouldConvertToURL() throws MalformedURLException {
		String urlStr = "http://uri.com";
		Url url = new Url(urlStr);
		assertThat("URI convertion good", url.asURL(), equalTo(new URL(urlStr)));
	}

	public void shouldCreateFromURL() throws MalformedURLException {
		String urlStr = "http://uri.com";
		Url url = new Url(new URL(urlStr));
		assertThat("From URL conversion good", url.getRaw(), equalTo(urlStr));
	}

	public void shouldCreateFromURI() throws URISyntaxException {
		String urlStr = "http://uri.com";
		Url url = new Url(new URI(urlStr));
		assertThat("From URL conversion good", url.getRaw(), equalTo(urlStr));
	}
}
