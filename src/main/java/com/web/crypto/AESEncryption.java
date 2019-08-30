package com.web.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class AESEncryption {
	
	static Map<String,List<byte[]>> keyIvMap;
	private String key;
	IJmBase64 base64 = null;
	public AESEncryption(String key) {
		keyIvMap = new HashMap<>();
		this.key = key;
		initializeKeyIvMap();
		base64 = new CJmBase64();
	}
	//
	public void initializeKeyIvMap() {
		List<byte[]> keyIvList = new ArrayList<>();
		byte[] key = new byte[]{(byte)101,(byte)68,(byte)255,(byte)100,(byte)85,(byte)18,(byte)143,(byte)154,(byte)81,(byte)64,(byte)128,(byte)58,(byte)248,(byte)238,(byte)158,(byte)140,(byte)0,(byte)18,(byte)183,(byte)17,(byte)42,(byte)175,(byte)183,(byte)53,(byte)167,(byte)176,(byte)143,(byte)2,(byte)252,(byte)56,(byte)182,(byte)116};
		byte[] iv = new byte[] {(byte)102,(byte)202,(byte)56,(byte)239,(byte)45,(byte)221,(byte)61,(byte)224,(byte)4,(byte)174,(byte)163,(byte)66,(byte)25,(byte)251,(byte)30,(byte)60};
		keyIvList.add(key);
		keyIvList.add(iv);
		String secretKey = "YQ*Mnq2a~y@@6Dy[";
		keyIvMap.put(secretKey, keyIvList);
	}
	
	public String encrypt(String inputString) {
		List<byte[]> keyIv = AESEncryption.keyIvMap.get(key);
		SecretKeySpec secret = new SecretKeySpec(keyIv.get(0), "AES");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(keyIv.get(1));
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
			byte[] result = cipher.doFinal(inputString.getBytes());
			//System.out.println("Java util base64 encode..."+Base64.getEncoder().encodeToString(result));
			
			System.out.println("Java implementation base64 encode..."+base64.encode(result));
			return base64.encode(result);
		}catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	   return "";
	}
	
	public String decrypt(String inputString) {
		List<byte[]> keyIv = AESEncryption.keyIvMap.get(key);
		SecretKeySpec secret = new SecretKeySpec(keyIv.get(0), "AES");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(keyIv.get(1));
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
			byte[] decoded = base64.decode(inputString);
			byte[] result = cipher.doFinal(decoded);
			return new String(result,"UTF-8");
		}catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	   return "";
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		
		AESEncryption aes = new AESEncryption("YQ*Mnq2a~y@@6Dy[");
		System.out.println(aes.encrypt("7740c942295dfd1f105dfb0137b077fcdb21a16a4630247688be438564876fe4"));
		System.out.println(aes.decrypt("g0o5qZ333sbDWa3rI6LYgKvMA0Pzv6Z+qxAvSdIL3rEsBBRa2OZME3/em0FPzRZum5iFksHKzEWkHJqiZ87eiNVxA6LyKm5BDt+gC9CQXEE="));
		
				
		
		/*String originalString = "7740c942295dfd1f105dfb0137b077fcdb21a16a4630247688be438564876fe4";
		byte[] bytesToBeEncrypted = originalString.getBytes();
		String secretKey = "YQ*Mnq2a~y@@6Dy[";
		String sha256 = sha256(secretKey);
		byte[] sha = sha2561(secretKey);
		char[] sha_char = new String(sha).toCharArray();
		//char[] sha256 = sha256_1(secretKey);
		//String unicodeString = "\\x8D\\x0C\\xEA\\xBC\\xA8\\xDC\\x7D\\x5E\\x13\\x3E\\xAA\\xFD\\x76\\x96\\x68\\xE9\\x3F\\x39\\x23\\x63\\xCC\\xBC\\x68\\x59\\x61\\x99\\x28\\x53\\x33\\xD5\\xEF\\xA6";
		//String unicodeString = "\u008D\u000C\u00EA\u00BC\u00A8\u00DC\u007D\u005E\u0013\u003E\u00AA\u00FD\u0076\u0096\u0068\u00E9\u003F\u0039\u0023\u0063\u00CC\u00BC\u0068\u0059\u0061\u0099\u0028\u0053\u0033\u00D5\u00EF\u00A6";
		char[] unicodeString = {0x8D,0x0C,0xEA,0xBC,0xA8,0xDC,0x7D,0x5E,0x13,0x3E,0xAA,0xFD,0x76,0x96,0x68,0xE9,0x3F,0x39,0x23,0x63,0xCC,0xBC,0x68,0x59,0x61,0x99,0x28,0x53,0x33,0xD5,0xEF,0xA6};
		//String convertedHetoString = convertHexToString(sha256);

		//System.out.println("sasa..."+convertedHetoString);
		
		//String tt = "\\x01\\x02\\x03\\x04\\x05\\x06\\x07\\x08";
		char[] tt = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};
		byte[] byteTable = new byte[]{1,2,3,4,5,6,7,8};//{0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};
		System.out.println("salt..."+new String(byteTable));
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		System.out.println(factory.getAlgorithm());
		PBEKeySpec pbeKeySpec = new PBEKeySpec(sha_char,byteTable, 1000, 384);
		 SecretKey secretKey2 = factory.generateSecret(pbeKeySpec);      
       
		   byte[] key1 = new byte[32];
		   byte[] iv1 = new byte[16];
		   byte[] key = new byte[32];
		   byte[] iv = new byte[16];
		   key[0] = (byte)101;
		   key[1] = (byte)68;
		   key[2] = (byte)255;
		   key[3] = (byte)100;
		   key[4] = (byte)85;
		   key[5] = (byte)18;
		   key[6] = (byte)143;
		   key[7] = (byte)154;
		   key[8] = (byte)81;
		   key[9] = (byte)64;
		   key[10] = (byte)128;
		   key[11] = (byte)58;
		   key[12] = (byte)248;
		   key[13] = (byte)238;
		   key[14] = (byte)158;
		   key[15] = (byte)140;
		   key[16] = (byte)0;
		   key[17] = (byte)18;
		   key[18] = (byte)183;
		   key[19] = (byte)17;
		   key[20] = (byte)42;
		   key[21] = (byte)175;
		   key[22] = (byte)183;
		   key[23] = (byte)53;
		   key[24] = (byte)167;
		   key[25] = (byte)176;
		   key[26] = (byte)143;
		   key[27] = (byte)2;
		   key[28] = (byte)252;
		   key[29] = (byte)56;
		   key[30] = (byte)182;
		   key[31] = (byte)116;
		   iv[0] = (byte)102;
		   iv[1] = (byte)202;
		   iv[2] = (byte)56;
		   iv[3] = (byte)239;
		   iv[4] = (byte)45;
		   iv[5] = (byte)221;
		   iv[6] = (byte)61;
		   iv[7] = (byte)224;
		   iv[8] = (byte)4;
		   iv[9] = (byte)174;
		   iv[10] = (byte)163;
		   iv[11] = (byte)66;
		   iv[12] = (byte)25;
		   iv[13] = (byte)251;
		   iv[14] = (byte)30;
		   iv[15] = (byte)60;
		   System.arraycopy(secretKey2.getEncoded(), 0, key1, 0, 32);
		   System.arraycopy(secretKey2.getEncoded(), 32, iv1, 0, 16);
			AESEncryption encryption = new AESEncryption();
			List<byte[]> keyIv = encryption.keyIvMap.get("YQ*Mnq2a~y@@6Dy[");
		   SecretKeySpec secret = new SecretKeySpec(keyIv.get(0), "AES");
		   AlgorithmParameterSpec ivSpec = new IvParameterSpec(keyIv.get(1));
		   Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		   cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
		   byte[] result = cipher.doFinal(bytesToBeEncrypted);
		   System.out.println(Base64.getEncoder().encodeToString(result));
		   String encryptedString = "g0o5qZ333sbDWa3rI6LYgKvMA0Pzv6Z+qxAvSdIL3rEsBBRa2OZME3/em0FPzRZum5iFksHKzEWkHJqiZ87eiNVxA6LyKm5BDt+gC9CQXEE=";
		 //  Cipher cipher2 = Cipher.getInstance("AES/CBC/PKCS5Padding");
	         cipher.init(Cipher.DECRYPT_MODE, secret, ivSpec);
	         byte[] decoded = Base64.getDecoder().decode(encryptedString.getBytes());
	         result = cipher.doFinal(decoded);
	         
	         System.out.println(new String(result,"UTF-8"));*/
	        
		   
	}
	
	/*public static String sha256(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }
	        	System.out.println("hexa cal.."+hexString.toString());
	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
	
	public static byte[] sha2561(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        return hash;
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
	
	public static char[] sha256_1(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        String hexString = new String(hash,"UTF-8");
        	System.out.println("hexa cal.."+hexString);
	        return hexString.toCharArray();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}
	 public static String convertHexToString(String hex){

		 StringBuilder output = new StringBuilder();
		    for (int i = 0; i < hex.length()-1; i+=2) {
		        String str = hex.substring(i, i+2);
		        output.append((char) Integer.parseInt(str, 16));
		    }
		    System.out.println("converted hex string..."+output);
		    return output.toString();
	  }*/
}
