package compressor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class StringCompressor {

    @Deprecated
    public static String getMd5(byte[] source) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            byte[] tmp = md.digest();
            char[] str = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte bytei = tmp[i];

            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    // e.g. algo = "MD5"
    public static String hash(String str, String algorithm) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(str.getBytes());
        byte[] digest = md.digest();
        return printHexBinary(digest);
    }

    public static void main(String[] args) {
        System.out.println(hash("https://www.104.com.tw/company/1a2x6bi4ov?jobsource=jolist_c_relevance", "MD5"));
    }


//    public static String hashByMd5(String str) {
//        final String algorithm = "MD5";
//        try {
//            MessageDigest md = MessageDigest.getInstance(algorithm);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
}
