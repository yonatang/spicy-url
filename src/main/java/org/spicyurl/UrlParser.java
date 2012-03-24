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

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;

class UrlParser {
	/**
	 * 
	 */
	private final static String SCHEME_SEP = "://";
	private final static char QUERY_SEP_CHAR = '?';
	private final static char FRAGMENT_SEP_CHAR = '#';
	private final static char PATH_SEP_CHAR = '/';
	private final static String LOGIN_SEP = "@";
	private final static String USER_PASS_SEP = ":";
	private final static String PORT_SEP = ":";
	final static int MIN_PORT_VALUE = 1;
	final static int MAX_PORT_VALUE = 65535;

	private final static String QUERY_SEP = "" + QUERY_SEP_CHAR;
	private final static String PATH_SEP = "" + PATH_SEP_CHAR;
	private final static String FRAGMENT_SEP = "" + FRAGMENT_SEP_CHAR;

	private final Url url;

	protected UrlParser(Url url) {
		this.url = url;
	}

	protected void parse() {
		String[] stage0 = StringUtils.splitByWholeSeparatorPreserveAllTokens(url.getRaw(), SCHEME_SEP, 2);

		url.setScheme(stage0[0]);
		if (stage0.length != 2) {
			url.getValidationErrorsModifiable().add(UrlErrors.HOST_IS_MISSING);
			return;
		}

		// Check for first separator, to split host from path/query/fragment
		int hostSeperatorIdx = StringUtils.indexOfAny(stage0[1], PATH_SEP, QUERY_SEP, FRAGMENT_SEP);
		if (hostSeperatorIdx == -1) {
			// Just host
			parseLoginHostPort(stage0[1]);
		} else {
			parseLoginHostPort(StringUtils.substring(stage0[1], 0, hostSeperatorIdx));
			parsePathQueryFregment(StringUtils.substring(stage0[1], hostSeperatorIdx));
		}

	}

	private void parseLoginHostPort(String loginHostPort) {
		if (StringUtils.isEmpty(loginHostPort)) {
			url.getValidationErrorsModifiable().add(UrlErrors.HOST_IS_MISSING);
			return;
		}
		if (loginHostPort.contains(LOGIN_SEP)) {
			parseLogin(StringUtils.substringBeforeLast(loginHostPort, LOGIN_SEP));
			parseHostPort(StringUtils.substringAfterLast(loginHostPort, LOGIN_SEP));
		} else {
			parseHostPort(loginHostPort);
		}
	}

	private void parseLogin(String login) {
		if (StringUtils.contains(login, USER_PASS_SEP)) {
			url.setUsername(StringUtils.substringBefore(login, USER_PASS_SEP));
			url.setPassword(StringUtils.substringAfter(login, USER_PASS_SEP));
		} else {
			url.setUsername(login);
		}
	}

	private void parseHostPort(String hostPort) {
		String[] stage0 = StringUtils.splitPreserveAllTokens(hostPort, PORT_SEP);
		if (stage0.length > 2) {
			url.getValidationErrorsModifiable().add(UrlErrors.INVALID_PORT_VALUE);
			return;
		}
		url.setHost(stage0[0]);
		if (stage0.length == 2) {
			parsePort(stage0[1]);
		}
	}

	private void parsePort(String port) {
		if (!StringUtils.isNumeric(port)) {
			url.getValidationErrorsModifiable().add(UrlErrors.INVALID_PORT_VALUE);
			return;
		}
		try {
			url.setPort(Integer.parseInt(port));
		} catch (NumberFormatException e) {
			url.getValidationErrorsModifiable().add(UrlErrors.INVALID_PORT_VALUE);
			return;
		}
	}

	/**
	 * 
	 * @param pathQueryFregment
	 *            - first char should be the seperator (PATH '/', QUERY '?' or
	 *            FRAGMENT '?')
	 */
	private void parsePathQueryFregment(String sepPathQueryFregment) {
		char firstSeperator = CharUtils.toChar(sepPathQueryFregment);
		String pathQueryFregment = StringUtils.substring(sepPathQueryFregment, 1);

		switch (firstSeperator) {
		case PATH_SEP_CHAR:
			int secondSeperatorIdx = StringUtils.indexOfAny(pathQueryFregment, QUERY_SEP, FRAGMENT_SEP);
			if (secondSeperatorIdx == -1) {
				url.setPath(pathQueryFregment);
			} else {
				url.setPath(StringUtils.substring(pathQueryFregment, 0, secondSeperatorIdx));
				if (pathQueryFregment.charAt(secondSeperatorIdx) == FRAGMENT_SEP_CHAR) {
					url.setFragment(StringUtils.substring(pathQueryFregment, secondSeperatorIdx + 1));
				} else {
					parseQueryFregmant(StringUtils.substring(pathQueryFregment, secondSeperatorIdx + 1));
				}
			}
			break;
		case QUERY_SEP_CHAR:
			parseQueryFregmant(pathQueryFregment);
			break;
		case FRAGMENT_SEP_CHAR:
			url.setFragment(pathQueryFregment);
			break;
		}
	}

	private void parseQueryFregmant(String queryFragment) {
		if (StringUtils.isEmpty(queryFragment)) {
			url.setQuery(queryFragment);
			return;
		}

		String[] parts = StringUtils.splitPreserveAllTokens(queryFragment, FRAGMENT_SEP, 2);
		url.setQuery(parts[0]);
		if (parts.length == 2) {
			url.setFragment(parts[1]);
		}
	}

}