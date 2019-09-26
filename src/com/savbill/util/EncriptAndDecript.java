package com.savbill.util;

import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncriptAndDecript {

	private static final String ALGORITHM = "AES";
    private static final byte[] keyValue  = new byte[] { '1', '2', '2', '3', '4', '1', 'A', 'S', 'e', 'c', 'r', 'e',  'K', 'e', 'y','t' };

     
    public static String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes()); 
        String encryptedValue = new BASE64Encoder().encode(encValue);  
        return encryptedValue;    
    }  
 
    public static String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);  
        byte[] decValue = c.doFinal(decordedValue);   
        String decryptedValue = new String(decValue);
        return decryptedValue;  
    }    

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        return key;
    }  
    
    public static void main(String[] args) throws Exception {
    	
    	String enc_value = EncriptAndDecript.encrypt("26/09/2019");
    	System.out.println(enc_value+"\n"+EncriptAndDecript.decrypt(enc_value));
	}
}
  