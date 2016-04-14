package com.surrey.com3014.group5.security;

import com.surrey.com3014.group5.exceptions.HttpStatusException;
import org.springframework.http.HttpStatus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * A utility class to generate a random strings.
 *
 * @author Aung Thu Moe
 */
public final class RandomUtils {
    /**
     * SHA1 RNG algorithm
     */
    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    /**
     * SHA1 algorithm
     */
    private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-1";

    /**
     * Just to prevent instantiation
     */
    private RandomUtils() {
    }

    /**
     * This method get a random string using {@link RandomUtils#SECURE_RANDOM_ALGORITHM}
     * and {@link RandomUtils#MESSAGE_DIGEST_ALGORITHM}
     *
     * @return a SHA1 random string
     */
    public static String getRandom() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
            //generate a random number
            String randomNum = Integer.toString(secureRandom.nextInt());
            MessageDigest sha1 = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
            byte[] result = sha1.digest(randomNum.getBytes());
            return toHexString(result);
        } catch (NoSuchAlgorithmException ex) {
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error with generating random number", ex);
        }
    }

    /**
     * This method converts the byte array to string.
     *
     * @param bytes to be converted
     * @return The converted String
     * @see <a href="http://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-l">Get the code from here.</a>
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
