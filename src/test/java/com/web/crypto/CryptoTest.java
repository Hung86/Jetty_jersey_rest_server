package com.web.crypto;

import org.junit.Test;

public class CryptoTest
{

   @Test
   public void testTextEncryptionAndDecryptionByAES()
   {
      String text = "This is unit test AES";
      ICrypto crypto = new CCrypto("AES",("YQ*Mnq2a~y@@6Dy[").getBytes());
      byte[] encryptedVal = crypto.encrypt(text);
      System.out.println("---testTextEncryptionByAES:" + new String(encryptedVal));
      
      String encyptedText = new String(encryptedVal);
      
      byte[] decryptedVal  = crypto.decrypt(encyptedText.getBytes());
      
      System.out.println("---testTextDecryptionByAES:" + new String(decryptedVal));
   }
   
   @Test
   public void testTextEncryptionAndDecryptionByTEA()
   {
      String text = "This is unit test TEA";
      ICrypto crypto = new CCrypto("TEA");
      byte[] encryptedVal = crypto.encrypt(text);
      System.out.println("---testTextEncryptionByTEA:" + new String(encryptedVal));
      
      String encyptedText = new String(encryptedVal);
      
      byte[] decryptedVal  = crypto.decrypt(encyptedText.getBytes());
      
      System.out.println("---testTextDecryptionByTEA:" + new String(decryptedVal));
      
   }
   
   
}
