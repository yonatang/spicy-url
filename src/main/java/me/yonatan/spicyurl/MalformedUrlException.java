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

public class MalformedUrlException extends RuntimeException {
	private static final long serialVersionUID = 2341793276271284540L;

	public MalformedUrlException() {
		super();
	}

	public MalformedUrlException(String message, Throwable cause) {
		super(message, cause);
	}

	public MalformedUrlException(String message) {
		super(message);
	}

	public MalformedUrlException(Throwable cause) {
		super(cause);
	}

}
