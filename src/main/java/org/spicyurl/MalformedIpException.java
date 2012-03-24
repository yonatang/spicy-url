package org.spicyurl;

import static org.spicyurl.ErrorMessages.INVALID_IP;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MalformedIpException extends RuntimeException {

	private static final long serialVersionUID = 3172606741836538575L;
	private static final ResourceBundle messages = ResourceBundle.getBundle("org/spicyurl/ErrorMessages");

	public MalformedIpException(String ip, Throwable cause) {
		super(MessageFormat.format(messages.getString(INVALID_IP), ip, messages.getString(UrlErrors.UNKNOWN.name())),
				cause);
	}

	public MalformedIpException(String ip) {
		super(MessageFormat.format(messages.getString(INVALID_IP), ip, messages.getString(UrlErrors.UNKNOWN.name())));
	}

	public MalformedIpException(String ip, UrlErrors err, Throwable cause, Object... msgArgs) {
		super(MessageFormat.format(messages.getString(INVALID_IP), ip,
				MessageFormat.format(messages.getString(err.name()), msgArgs)), cause);
	}

	public MalformedIpException(String ip, UrlErrors err, Object... msgArgs) {
		super(MessageFormat.format(messages.getString(INVALID_IP), ip,
				MessageFormat.format(messages.getString(err.name()), msgArgs)));
	}

	public MalformedIpException(String ip, String errorMessage, Throwable cause) {
		super(MessageFormat.format(messages.getString(INVALID_IP), ip, errorMessage), cause);
	}

	public MalformedIpException(String ip, String errorMessage) {
		super(MessageFormat.format(messages.getString(INVALID_IP), ip, errorMessage));
	}

}
