package lk.payhere.androidsdk.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lk.payhere.androidsdk.PHConstants;

/* loaded from: classes7.dex */
public class SecurityUtils {
    public static String generateMD5Value(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                stringBuffer.append(String.format("%02x", Byte.valueOf(digest[i])));
            }
            return stringBuffer.toString().toUpperCase(PHConstants.DEFAULT_LOCALE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
