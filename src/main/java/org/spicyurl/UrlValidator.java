package org.spicyurl;

import org.apache.commons.lang3.StringUtils;

public class UrlValidator {

	// TODO implement validator properly

	public static boolean validateHost(Url url) {
		String host = url.getHost();
		if (StringUtils.isEmpty(host)) {
			url.getValidationErrorsModifiable().add(UrlErrors.HOST_IS_MISSING);
			return false;
		}
		if (StringUtils.startsWith(host, "[")) {
			return validateIpV6(host);
		}

		return true;
	}

	private static boolean validateIpV6(String host) {
		// TODO
		return false;
	}
}
