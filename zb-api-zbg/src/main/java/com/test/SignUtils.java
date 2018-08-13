package com.test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SignUtils {

    /**
     * 自行根据需要设置,一般认为一个服务里使用的只有一个
     */
    public static String ID_NAME = "Apiid";

    /**
     * 不为空、不为空字符串、不为双引号、不为空{}
     *
     * @param source
     * @return
     */
    public static boolean isEmpty(String source) {
        return source == null || source.isEmpty() || source.equals("\"\"") || source.trim().equals("{}");
    }

    /**
     * 参数是formdata key value，或者get参数 形式的情况下获取签名header
     *
     * @param parameters
     * @return
     */
    public static Map getHeaderOfKeyValue(String id, String secret,Map<String, Object> parameters) {
        long time = System.currentTimeMillis();
        Map header = new HashMap();
        StringBuffer contentSb = new StringBuffer();
        if (parameters != null) {
            parameters.entrySet().stream()
                    .filter(a -> a != null && (!isEmpty(a.getKey()) || !isEmpty((String) a.getValue()))).
                    sorted(Map.Entry.<String, Object>comparingByKey()).forEachOrdered(e -> contentSb.append(e.getKey() + e.getValue()));
        }

        header.put(ID_NAME,id);
        header.put("Timestamp", String.valueOf(time));
        header.put("Sign", encryptMD5(id + time + contentSb.toString() + secret));
        return header;

    }

    /**
     * 参数为空情况下获取签名header
     *
     * @return
     */
    public static Map getHeaderOfNoParams(String id, String secret) {
        Map header = new HashMap();
        long time = System.currentTimeMillis();
        header.put(ID_NAME, id);
        header.put("Timestamp", String.valueOf(time));
        header.put("Sign", encryptMD5(id + time + secret));
        return header;

    }

    /**
     * 参数是body json形式的情况下获取签名header
     *
     * @param JsonStr
     * @return
     */
    public static Map getHeaderOfBodyJson(String id, String secret, String JsonStr) {
        Map header = new HashMap();
        long time = System.currentTimeMillis();
        header.put(ID_NAME, id);
        header.put("Timestamp", String.valueOf(time));
        header.put("Sign", encryptMD5(id + time + JsonStr + secret));
        return header;
    }

    public static String encryptMD5(String str) {
        return digest("MD5", str);
    }

    public static String encryptSHA(String str) {
        return digest("SHA", str);
    }

    public static String digest(String code, String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(code);
            messageDigest.reset();
            messageDigest.update(str.getBytes());
            byte[] byteArray = messageDigest.digest();
            StringBuffer md5StrBuff = new StringBuffer();

            for(int i = 0; i < byteArray.length; ++i) {
                if(Integer.toHexString(255 & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
                }
            }

            return md5StrBuff.toString();
        } catch (NoSuchAlgorithmException var6) {
            return null;
        }
    }

    public static int hash_time33(String source) {
        int hash = 0;

        for(int i = 0; i < source.length(); ++i) {
            hash = hash * 33 + Integer.valueOf(source.charAt(i)).intValue();
        }

        return hash;
    }
}
