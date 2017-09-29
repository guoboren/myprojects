package servertest.util;

import java.security.MessageDigest;

import Decoder.BASE64Encoder;


public class UserUtil {
	private UserUtil(){}
	/**
	 * MD5   ¼ÓÃÜ×Ö·û´®
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String convertStringToMD5(String str) throws Exception{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] data = md5.digest(str.getBytes());
		BASE64Encoder base64 = new BASE64Encoder();
		return base64.encode(data);
		
	}
}
