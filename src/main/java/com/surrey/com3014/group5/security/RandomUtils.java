package com.surrey.com3014.group5.security;

import com.surrey.com3014.group5.exceptions.HttpStatusException;
import org.springframework.http.HttpStatus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Aung Thu Moe
 */
public final class RandomUtils {
    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-1";
    private RandomUtils() {
        // just to prevent instantiation
    }

    public static String getRandom() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
            //generate a random number
            String randomNum = Integer.toString(secureRandom.nextInt());
            MessageDigest sha1 = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
            byte[] result =  sha1.digest(randomNum.getBytes());
            return toHexString(result);
        }
        catch (NoSuchAlgorithmException ex) {
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error with generating random number", ex);
        }
    }

    /**
     * http://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l
     * @param bytes
     * @return
     */
    public static String toHexString(byte[] bytes) {
        final StringBuilder hexString = new StringBuilder();

        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
