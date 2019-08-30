 /**
 * 
 */
package com.web.crypto;


/**
 * @author aswanth
 *
 */
public class CCrypto implements ICrypto{
	
	private String cryptoType;
	private static byte[] key = { (byte) '<', (byte) 'S', (byte) 'J',
			(byte) '-', (byte) 'P', (byte) 'S', (byte) '>', (byte) 'a',
			(byte) 'n', (byte) 't', (byte) 'i', (byte) 'c', (byte) 'o',
			(byte) 'p', (byte) 'y', (byte) '>' };
	TEACryptDecrypt mTEACryptDecrypt = null;
	AESEncryption mAesEncryption = null;
	public CCrypto(String crypto) {
		cryptoType = crypto;
		mTEACryptDecrypt = new TEACryptDecrypt(key);
	}
	
	public CCrypto(String crypto,byte[] key) {
		cryptoType = crypto;
		if(crypto.equalsIgnoreCase("TEA")) {
			mTEACryptDecrypt = new TEACryptDecrypt(key);
		}else if(crypto.equalsIgnoreCase("AES")) {
			mAesEncryption = new AESEncryption(new String(key));
		}
		
	}
	
	@Override
	public byte[] encrypt(String input) {
		byte[] result = null;
		if(cryptoType.equalsIgnoreCase("TEA")) {
			result = mTEACryptDecrypt.enCrypt(input);
		}else if(cryptoType.equalsIgnoreCase("AES")){
			String res = mAesEncryption.encrypt(input);
			result = res.getBytes();
		}
		else {
			//print sth	
		}	
		return result;
	}

	@Override
	public byte[] decrypt(byte[] encodedVal) {
		byte[] result = null;
		if(cryptoType.equalsIgnoreCase("TEA")) {
			result = mTEACryptDecrypt.decrypt(encodedVal);
		}else if(cryptoType.equalsIgnoreCase("AES")){
			String res = mAesEncryption.decrypt(new String(encodedVal));
			result = res.getBytes();
		}else {
			//print sth
			
		}
		return result;
	}

	@Override
	public byte[] decryptAscii(byte[] asciiBytes) {
		byte[] result = null;
		if(cryptoType.equalsIgnoreCase("TEA")) {
			result = mTEACryptDecrypt.decryptAscii(asciiBytes);
		}else {
			//print sth
			
		}
		return result;
	}

	@Override
	public byte[] encryptAscii(byte[] asciiBytes) {
		byte[] result = null;
		if(cryptoType.equalsIgnoreCase("TEA")) {
			result = mTEACryptDecrypt.unicodeToASCIIArray(asciiBytes);
		}else {
			//print sth
			
		}
		return result;
	}
	
	@Override
	public byte[] hexa2Bytes(String str, int intByteSize) {

		byte[] result = null;
		if (cryptoType.equalsIgnoreCase("TEA")) {
			result = mTEACryptDecrypt.psHexa2Bytes(str, intByteSize);
		} else {
			//print sth
		}
		return result;
	}

	@Override
	public byte[] decode(byte[] b, int count) {

		byte[] result = null;
		if (cryptoType.equalsIgnoreCase("TEA")) {
			result = mTEACryptDecrypt.decode(b, count);
		} else {
			//print sth
		}
		return result;
	}

}
