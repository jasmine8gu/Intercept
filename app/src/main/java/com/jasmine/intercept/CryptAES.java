package com.jasmine.intercept;

import android.util.Base64;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptAES {

    private static final String AESTYPE ="AES/ECB/PKCS5Padding";
    public static String keyStr = "UITN25LMUQC436IM";

    public static String AES_Encrypt(String keyStr, String plainText) {
        byte[] encrypt = null;
        try{
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypt = cipher.doFinal(plainText.getBytes());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return Base64.encodeToString(encrypt, Base64.DEFAULT);
    }

    public static String AES_Decrypt(String keyStr, String encryptData) {
        byte[] decrypt = null;
        try{
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypt = cipher.doFinal(Base64.encode(encryptData.getBytes(), Base64.DEFAULT));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new String(decrypt).trim();
    }

    private static Key generateKey(String key)throws Exception{
        try{
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            return keySpec;
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}