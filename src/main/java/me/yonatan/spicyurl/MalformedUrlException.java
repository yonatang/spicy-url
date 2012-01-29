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

import java.text.MessageFormat;
import java.util.ResourceBundle;

import me.yonatan.spicyurl.Url.Errors;

public class MalformedUrlException extends RuntimeException {
	private static final long serialVersionUID = 2341793276271284540L;

	private static final ResourceBundle messages = ResourceBundle
			.getBundle("me/yonatan/spicyurl/ErrorMessages");

	public MalformedUrlException(String url, Throwable cause) {
		super(MessageFormat.format(messages.getString("GENERAL_MESSAGE"), url,
				messages.getString(Errors.UNKNOWN.name())), cause);
	}

	public MalformedUrlException(String url) {
		super(MessageFormat.format(messages.getString("GENERAL_MESSAGE"), url,
				messages.getString(Errors.UNKNOWN.name())));
	}

	public MalformedUrlException(String url, Errors err, Throwable cause,
			Object... msgArgs) {
		super(MessageFormat.format(messages.getString("GENERAL_MESSAGE"), url,
				MessageFormat.format(messages.getString(err.name()), msgArgs)),
				cause);
	}

	public MalformedUrlException(String url, Errors err, Object... msgArgs) {
		super(MessageFormat.format(messages.getString("GENERAL_MESSAGE"), url,
				MessageFormat.format(messages.getString(err.name()), msgArgs)));
	}

	public MalformedUrlException(String url, String errorMessage,
			Throwable cause) {
		super(MessageFormat.format(messages.getString("GENERAL_MESSAGE"), url,
				errorMessage), cause);
	}

	public MalformedUrlException(String url, String errorMessage) {
		super(MessageFormat.format(messages.getString("GENERAL_MESSAGE"), url,
				errorMessage));
	}

}
