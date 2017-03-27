package com.trust.clientCrypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*******************************************************************************
 * AES encryption & decryption algorithm
 * 
 * @ʹ��byte2hex��ת��
 * 
 */

public class AES{

    // encryption
    public static String Encrypt(String plaintext, String sKey) throws Exception {
        if (sKey == null) {
            System.out.println("Key cannot be null during Encryption");
            return null;
        }
        
        if (sKey.length() != 16) {
            System.out.println("Key's length must be 16 during Encryption");
            return null;
        }
        
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());

        return byte2hex(encrypted).toLowerCase();
    }

    // decryption
    public static String Decrypt(String myCipher, String sKey) throws Exception {
        try {
            
            if (sKey == null) {
                System.out.println("Key cannot be null during Decryption");
                return null;
            }
            
            if (sKey.length() != 16) {
                System.out.println("Key's length must be 16 during Decryption");
                return null;
            }
            
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = hex2byte(myCipher);
            
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
            
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

   
}

