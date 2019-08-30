/**
 * 
 */
package com.web.crypto;

/**
 * @author aswanth
 *
 */
public interface ICrypto {
	/**
	 * 
	 * This method encodes
	 * 
	 * @param asciiString
	 *            - String to be encoded
	 * 
	 * @return encoded bytes
	 */
	
	public byte[] encrypt(String input);
	/**
	 * This method decodes
	 * 
	 * @param byteByte
	 *            - array of bytes to be decoded
	 * 
	 * @return decoded array of bytes
	 */
	public byte[] decrypt(byte[] encodedVal);
	
	
	/**
	 * Decrypt ascii byte array.
	 * @param asciiBytes
	 * @return
	 */
	public byte[] decryptAscii(byte[] asciiBytes);
	
	/**
	 * Encript ascii byte array.
	 * @param asciiBytes
	 * @return
	 */
	public byte[] encryptAscii(byte[] asciiBytes);
	
	/**
	 * Encript ascii byte array.
	 * @param asciiBytes
	 * @return
	 */
	public byte[] hexa2Bytes(String str, int intByteSize);
	
	/**
	 * Encript ascii byte array.
	 * @param asciiBytes
	 * @return
	 */
	public byte[] decode(byte b[], int count);
}
