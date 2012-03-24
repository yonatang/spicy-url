package org.spicyurl;

import org.apache.commons.lang3.StringUtils;

public class IpC14n {

	public byte[] c14nIpV4(String ip) throws MalformedIpException {
		// TODO - allow to check without exceptions if this ipv4 or not
		if (StringUtils.isBlank(ip))
			throw new MalformedIpException(ip, "IP can't be blank");

		String[] parts = StringUtils.split(ip, '.');
		if (parts.length > 4)
			throw new MalformedIpException(ip, "Too much octats");

		byte[] result = new byte[4];
		int[] octats = new int[4];
		try {
			for (int i = 0; i < parts.length; i++) {
				octats[i] = parseIpv4Octat(parts[i]);
				if (i + 1 < parts.length && octats[i] > 255) {
					throw new MalformedIpException(ip, String.format("Octat %s is too large", octats[i]));
				}
				if (i + 1 < parts.length)
					result[i] = (byte) octats[i];
			}
		} catch (IllegalArgumentException e) {
			throw new MalformedIpException(ip, e.getMessage());
		}
		int lastOctat = octats[parts.length - 1];
		if (lastOctat > (Integer.rotateLeft(256, parts.length - 1) - 1)) {
			throw new MalformedIpException(ip, String.format("Octat %s is too large", octats[parts.length - 1]));
		}

		int j = 3;
		while (lastOctat > 0) {
			result[j] = (byte) (lastOctat & 255);
			lastOctat = lastOctat >> 8;
			j--;
		}

		return result;
	}

	int parseIpv4Octat(String octat) throws IllegalArgumentException {
		if (StringUtils.isNumeric(octat)) {
			int radix = 10;
			if (StringUtils.startsWith(octat, "0")) {
				// It is an octal octat
				if (!StringUtils.containsOnly(octat, "01234567"))
					throw new IllegalArgumentException(String.format("Octat %s is ocal, but contains bad numbers",
							octat));
				radix = 8;

			}
			try {
				return Integer.parseInt(octat, radix);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(String.format("Octat %s is too large", octat));
			}
		}
		if (!StringUtils.startsWithIgnoreCase(octat, "0x"))
			throw new IllegalArgumentException("Octat %s is in unfamilier format");

		octat = StringUtils.substring(octat, 2);
		if (!StringUtils.containsOnly(octat, "0123456789abcdefABCDEF"))
			throw new IllegalArgumentException(
					String.format("Octat %s is hexadecimal, but contains bad numbers", octat));

		try {
			return Integer.parseInt(octat, 16);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(String.format("Octat %s is too large", octat));
		}
	}
}
