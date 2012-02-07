package com.activequant.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Crypter {

    private final String strkey;

    public Crypter() {
        this.strkey = "ABCD";
    }

    public String encryptBlowfish(String to_encrypt) {
        try {
            SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            BASE64Encoder enc = new BASE64Encoder();
            return enc.encode(cipher.doFinal(to_encrypt.getBytes()));
        } catch (Exception e) {
            return null;
        }
    }

    public String decryptBlowfish(String to_decrypt) {
        try {
            SecretKeySpec key = new SecretKeySpec(strkey.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, key);

            BASE64Decoder dec = new BASE64Decoder();
            byte[] bytes = dec.decodeBuffer(to_decrypt);

            byte[] decrypted = cipher.doFinal(bytes);

            return new String(decrypted);
        } catch (Exception e) {
            return null;
        }
    }
}
