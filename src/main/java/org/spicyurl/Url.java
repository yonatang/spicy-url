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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

@Getter
public class Url {

	public Url(String url) {
		Preconditions.checkArgument(StringUtils.isNotBlank(url));
		raw = url;
		UrlParser p = new UrlParser(this);
		p.parse();
	}

	public Url(URL url) {
		this(Preconditions.checkNotNull(url).toString());
	}

	public Url(URI uri) {
		this(Preconditions.checkNotNull(uri).toString());
	}

	public URI asURI() throws URISyntaxException {
		return new URI(getRaw());
	}

	public URL asURL() throws MalformedURLException {
		return new URL(getRaw());
	}

	private String raw;
	private String host;
	private String scheme;
	private String path;
	private int port = -1;
	private String username;
	private String password;
	private String query;
	private String fragment;

	@Getter(AccessLevel.PROTECTED)
	private Set<UrlErrors> validationErrorsModifiable = new HashSet<UrlErrors>();

	private Set<UrlErrors> valiationErrors = Collections.unmodifiableSet(validationErrorsModifiable);

	public boolean isValid() {
		return validationErrorsModifiable.isEmpty();
	}

	protected void setHost(String host) {
		if (StringUtils.isEmpty(host)) {
			validationErrorsModifiable.add(UrlErrors.HOST_IS_MISSING);
			return;
		}
		this.host = host;
	}

	protected void setScheme(String scheme) {
		if (StringUtils.isEmpty(scheme)) {
			validationErrorsModifiable.add(UrlErrors.SCHEME_IS_MISSING);
			return;
		}
		this.scheme = scheme;
	}

	protected void setPath(String path) {
		this.path = path;
	}

	protected void setPort(int port) {
		if (port < UrlParser.MIN_PORT_VALUE || port > UrlParser.MAX_PORT_VALUE) {
			validationErrorsModifiable.add(UrlErrors.INVALID_PORT_VALUE);
			return;
		}

		this.port = port;
	}

	protected void setUsername(String username) {
		this.username = username;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected void setQuery(String query) {
		this.query = query;
	}

	protected void setFragment(String fragment) {
		this.fragment = fragment;
	}

}
