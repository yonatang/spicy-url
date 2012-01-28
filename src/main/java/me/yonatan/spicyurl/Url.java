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

import java.net.URI;
import java.net.URL;

import lombok.Getter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

@Getter
@ToString
public class Url {

	public Url(String url) {
		Preconditions.checkArgument(StringUtils.isNotBlank(url));
		raw = url;
		Parser p = new Parser(this);
		p.parse();
	}
	
	public Url(URL url){
		this(Preconditions.checkNotNull(url).toString());
	}
	
	public Url(URI uri){
		this(Preconditions.checkNotNull(uri).toString());
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

	class Parser {
		private final static String SCHEME_SEP = "://";

		private final Url url;

		Parser(Url url) {
			this.url = url;
		}

		void parse() {
			String[] stage0 = StringUtils
					.splitByWholeSeparatorPreserveAllTokens(url.getRaw(),
							SCHEME_SEP, 2);
			if (stage0.length != 2)
				throw new MalformedUrlException();

			url.scheme = stage0[0];
			if (StringUtils.isEmpty(scheme))
				throw new MalformedUrlException();

			String[] stage1 = StringUtils
					.splitByWholeSeparatorPreserveAllTokens(stage0[1], "/", 2);
			if (stage1.length == 0)
				throw new MalformedUrlException();

			parseLoginHostPort(stage1[0]);
			if (stage1.length == 2)
				parsePathQueryFregment(stage1[1]);
		}

		private void parseLoginHostPort(String loginHostPort) {
			if (StringUtils.isEmpty(loginHostPort))
				throw new MalformedUrlException();
			if (loginHostPort.contains("@")) {
				parseLogin(StringUtils.substringBeforeLast(loginHostPort, "@"));
				parseHostPort(StringUtils.substringAfterLast(loginHostPort,
						"@"));
			} else {
				parseHostPort(loginHostPort);
			}
		}

		private void parseLogin(String login) {
			if (StringUtils.isEmpty(login))
				return;
			
			if (login.contains(":")){
				url.username=StringUtils.substringBefore(login, ":");
				url.password=StringUtils.substringAfter(login, ":");
			} else {
				url.username=login;
			}
		}

		private void parseHostPort(String hostPort) {
			String[] stage0 = StringUtils.splitPreserveAllTokens(hostPort, ':');
			if (stage0.length > 2)
				throw new MalformedUrlException();
			url.host = stage0[0];
			if (StringUtils.isEmpty(url.host))
				throw new MalformedUrlException();
			if (stage0.length == 2) {
				parsePort(stage0[1]);
			}
		}

		private void parsePort(String port) {
			if (!StringUtils.isNumeric(port)) {
				throw new MalformedUrlException();
			}
			try {
				url.port = Integer.parseInt(port);
			} catch (NumberFormatException e) {
				throw new MalformedUrlException();
			}
			if (url.port <= 0 || url.port > 65535)
				throw new MalformedUrlException();
		}

		private void parsePathQueryFregment(String pathQueryFregment) {
			//if (StringUtils.isEmpty(pathQueryFregment)) return;
			if (pathQueryFregment==null) return;
			String[] stage0=StringUtils.splitPreserveAllTokens(pathQueryFregment, "#", 2);
			if (stage0.length==2){
				url.fragment=stage0[1];
			}
			parsePathQuery(stage0[0]);
		}
		
		private void parsePathQuery(String pathQuery){
			//if (StringUtils.isEmpty(pathQuery)) return;
			String[] stage0=StringUtils.splitPreserveAllTokens(pathQuery, "?", 2);
			if (stage0.length==2){
				url.query=stage0[1];
			}
			url.path=stage0[0];
		}
	}

}
