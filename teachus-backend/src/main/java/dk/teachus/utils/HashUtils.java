package dk.teachus.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
	
	public static String hash(final String text) {
		try {
			// First hash the password
			final MessageDigest md = MessageDigest.getInstance("SHA-1");
			final byte[] hashedBytes = md.digest(text.getBytes());
			
			// Convert to HEX string
			final StringBuffer hexString = new StringBuffer();
			for (final byte element : hashedBytes) {
				hexString.append(Integer.toHexString(element & 0xFF | 0x100).toUpperCase().substring(1, 3));
			}
			return hexString.toString();
		} catch (final NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
}
