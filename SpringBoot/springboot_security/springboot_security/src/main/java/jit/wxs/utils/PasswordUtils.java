package jit.wxs.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * 密码工具类
 * 依赖commons-lang3、commons-codec
 * @className PasswordUtils.java
 * @author jitwxs
 * @version 创建时间：2017年10月18日 下午9:18:38
 */
public class PasswordUtils {
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;
	private static final String SHA1 = "SHA-1";

	private static SecureRandom random = new SecureRandom();

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 * @author jitwxs
	 * @version 创建时间：2017年10月18日 下午9:10:31
	 * @param plainPassword 明文
	 * @return 密文(56位)
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = generateSalt(SALT_SIZE);
		byte[] hashPassword = sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return encodeHex(salt)+encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * @author jitwxs
	 * @version 创建时间：2017年10月18日 下午9:11:18
	 * @param plainPassword 明文
	 * @param password 密文
	 * @return 验证结果
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = decodeHex(password.substring(0,16));
		byte[] hashPassword = sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(encodeHex(salt)+encodeHex(hashPassword));
	}

	/**
	 * 检查密码强度
	 * @author jitwxs
	 * @version 创建时间：2018年3月8日 上午10:50:06
	 * @param password
	 * @return 弱|中|强|未知
	 */
	public static String checkStrength(String password) {
		String regexZ = "\\d*", regexS = "[a-zA-Z]+", regexT = "\\W+$";
		String regexZT = "\\D*", regexST = "[\\d\\W]*", regexZS = "\\w*";
		String regexZST = "[\\w\\W]*";

		if(!StringUtils.isEmpty(password)) {
			if (password.matches(regexZ) || password.matches(regexS) || password.matches(regexT)) {
				return "弱";
			} else if (password.matches(regexZT) || password.matches(regexST) || password.matches(regexZS)) {
				return "中";
			} else if (password.matches(regexZST)) {
				return "强";
			}
		}
		return "未知";
	}

	private static byte[] generateSalt(int numBytes) {
		Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);
		byte[] bytes = new byte[numBytes];
		random.nextBytes(bytes);
		return bytes;
	}

	private static String encodeHex(byte[] input) {
		return new String(Hex.encodeHex(input));
	}

	private static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			return null;
		}
	}

	private static byte[] sha1(byte[] input, byte[] salt, int iterations) {
		return digest(input, SHA1, salt, iterations);
	}

	private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			if (salt != null) {
				digest.update(salt);
			}
			byte[] result = digest.digest(input);

			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return result;
		} catch (GeneralSecurityException e) {
			return null;
		}
	}

	public static void main(String[] args) {
		String entryptPassword = entryptPassword("123");
		System.out.println(entryptPassword);
		boolean flag = validatePassword("123", entryptPassword);
		System.out.println(flag);
		System.out.println(checkStrength("123"));
	}
}