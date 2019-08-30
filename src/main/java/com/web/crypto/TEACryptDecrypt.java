package com.web.crypto;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class TEACryptDecrypt {
	private static byte[] byteHexaTable = { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35,
			0x36, 0x37, 0x38, 0x39, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x00 };

	protected static final byte[] byteTable = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0,
			0, 0, 0, 0, 10, 11, 12, 13, 14, 15 };

/*	byte[] key = { (byte) '1', (byte) 's', (byte) 'j',
			(byte) '1', (byte) 'j', (byte) '-', (byte) 'e', (byte) '-',
			(byte) 't', (byte) '-', (byte) '2', (byte) '0', (byte) '0',
			(byte) '7', (byte) '-', (byte) '@' };*/

	
	TEAEncryption teaObj;
	
	public TEACryptDecrypt(byte[] key){
		teaObj = new TEAEncryption(key);
	}

	public byte[] enCrypt(String stringIn) {
		int intUnicodeSize = 0;
		int[] intEncipher = null;
		byte[] byteUnicode = null;
		byte[] byteEncipher = null;
		byte[] byteUniHexa = null;

		// Convert From Ascii String to Unicode Array of bytes
		intUnicodeSize = (stringIn.length() + 1) * 2;
		byteUnicode = psAsciiToUnicodeArray(stringIn);
		intUnicodeSize = byteUnicode.length;
		
		// Encipher the Unicode Bytes
		intEncipher = teaObj.encode(byteUnicode, intUnicodeSize);
		byteEncipher = teaObj.intToByte(intEncipher);
		
		// Convert the Encoded Bytes Into Unicode Hexabytes
		// Hexa Bytes is Format which is used to send Data
		byteUniHexa = bytes2UniHexa(byteEncipher);
		
		return byteUniHexa;
	}
	
	public byte[] LEncrypt(String stringIn) {
		int intUnicodeSize = 0;
		int[] intEncipher = null;
		byte[] byteUnicode = null;
		byte[] byteEncipher = null;
		byte[] byteUniHexa = null;

		// Convert From Ascii String to Unicode Array of bytes
		intUnicodeSize = (stringIn.length() + 1) * 2;
		byteUnicode = psAsciiToUnicodeArray(stringIn);

		// Encipher the Unicode Bytes
		intEncipher = teaObj.encode(byteUnicode, intUnicodeSize);
		byteEncipher = teaObj.intToByte(intEncipher);

		// Convert the Encoded Bytes Into Unicode Hexabytes
		// Hexa Bytes is Format which is used to send Data
		byteUniHexa = Lbytes2UniHexa(byteEncipher);
		return byteUniHexa;
	}
	
	
	

	/**
	 * Converts Ascii bytes into Hexa bytes
	 * 
	 * @param byteByte
	 * 
	 * @return
	 */
	public byte[] bytes2UniHexa(byte[] byteByte) {
		int intByteSize = byteByte.length;
		int intUniHexaSize = intByteSize * 4;
		byte[] byteUniHexa = new byte[intUniHexaSize];
		for (int intIndex = 0; intIndex < intByteSize; intIndex += 1) {
			byteUniHexa[intIndex * 4 + 0] = byteHexaTable[(byteByte[intIndex] >> 4) & 0x0000000F];

			byteUniHexa[intIndex * 4 + 1] = 0x00;
			byteUniHexa[intIndex * 4 + 2] = byteHexaTable[(byteByte[intIndex]) & 0x0000000F];
			byteUniHexa[intIndex * 4 + 3] = 0x00;

		}
		// byteUniHexa[intUniHexaSize] = 0x00;
		// byteUniHexa[intUniHexaSize + 1] = 0x00;
		return byteUniHexa;
	}
	
	public byte[] Lbytes2UniHexa(byte[] byteByte) {
		int intByteSize = byteByte.length;
		int intUniHexaSize = intByteSize * 4;
		//byte[] byteUniHexa = new byte[intUniHexaSize];
		byte[] byteUniHexa = new byte[intUniHexaSize+2];
		
		for (int intIndex = 0; intIndex < intByteSize; intIndex += 1) {
			byteUniHexa[intIndex * 4 + 0] = byteHexaTable[(byteByte[intIndex] >> 4) & 0x0000000F];

			byteUniHexa[intIndex * 4 + 1] = 0x00;
			byteUniHexa[intIndex * 4 + 2] = byteHexaTable[(byteByte[intIndex]) & 0x0000000F];
			byteUniHexa[intIndex * 4 + 3] = 0x00;

		}
	 byteUniHexa[intUniHexaSize] = 0x00;
	 byteUniHexa[intUniHexaSize + 1] = 0x00;
	 return byteUniHexa;
	}
	
	

	public  byte[] psHexa2Bytes(String str, int intByteSize) // OK
	{
		byte[] byteByte = new byte[intByteSize + 1];
		for (int intIndex = 0; intIndex < intByteSize / 2; intIndex += 1) {

			byteByte[intIndex] = (byte) ((byteTable[str.charAt(2 * intIndex)
					- (byte) 0x30] * (byte) 0x10) + (byteTable[str
					.charAt(2 * intIndex + 1)
					- (byte) 0x30]));

			int x = (int) byteByte[intIndex];
			if (x < 0) {
				x = x + 256;
			}
		}
		byteByte[intByteSize] = 0x00;
		return byteByte;
	}

	/**
	 * converts list of Byte into array of bytes
	 * 
	 * @param b
	 *            list of Byte array
	 * 
	 * @return array of bytes
	 */
	public static byte[] listToArray(ArrayList b) {
		byte[] bytearr = new byte[b.size()];
		Object[] arrObj = b.toArray();
		for (int i = 0; i < arrObj.length; i++)
			bytearr[i] = ((Byte) arrObj[i]).byteValue();

		return bytearr;
	}

	/**
	 * Converts UnicodeHexabytes into Hexa Bytes
	 * 
	 * @param byteByte
	 * 
	 * @return
	 */
	public byte[] decrypt(byte[] byteByte) {
		byte[] unicodeBytes = new byte[byteByte.length / 2];
		byte[] hexaBytes = null;
		byte[] hexaBytes2 = null;
		byte[] byteDecipher = null;
		byte[] asciibytes = null;
		
		// Skip off the alternate '0' in byteByte (Note : we received bytes in
		// HexaFormat)
		for (int i = 0; i < byteByte.length; i += 2)
			unicodeBytes[i / 2] = byteByte[i];

		// Convert Each Byte into corresponding Hexa Byte
		// We Map Each byte into byte table
		hexaBytes = new byte[unicodeBytes.length];
		for (int i = 0; i < unicodeBytes.length; i++)
			hexaBytes[i] = byteTable[(byte) (unicodeBytes[i] - (48))];

		// Convert Two Byte into one Hexa Byte
		hexaBytes2 = new byte[hexaBytes.length / 2];
		for (int i = 0; i < hexaBytes.length; i += 2)
			hexaBytes2[i / 2] = (byte) (((hexaBytes[i] & 0x0000000F) << 4) | (hexaBytes[i + 1] & 0x0000000F));

		byteDecipher = teaObj.decode(hexaBytes2, hexaBytes2.length);
		try {
			asciibytes= psUnicodeToAsciiArrayNew(byteDecipher, byteDecipher.length);
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		return asciibytes;

	}
	public byte [] enCryptAscii(byte []asciiByteArray){
		//Convert to unicode
		byte [] unicodeBytes=new byte[asciiByteArray.length*2];
		for(int i=0;i<asciiByteArray.length;i++){
			unicodeBytes[i*2]=asciiByteArray[i];
			unicodeBytes[i*2+1]='0';
		}
		return enCrypt(new String(unicodeBytes));
	}
	
	public byte [] decryptAscii(byte []asciiByteArray){
		//Convert to unicode
		byte [] unicodeBytes=new byte[asciiByteArray.length*2];
		for(int i=0;i<asciiByteArray.length;i++){
			unicodeBytes[i*2]=asciiByteArray[i];
			unicodeBytes[i*2+1]='0';
		}
		return decrypt(unicodeBytes);
	}
	
	public byte[] psUnicodeToAsciiArrayNew(byte[] byteUnicode, int intUnicodeSize) throws UnsupportedEncodingException {
		int intAsciiSize = intUnicodeSize / 2;
		byte[] byteInAscii = new byte[intUnicodeSize];
		for (int intIndex = 0, intTempIndex = 0; intIndex < intAsciiSize; intIndex += 1) {
			if (byteUnicode[intIndex * 2] == 0
					&& byteUnicode[(intIndex * 2) + 1] == 0)
			{
				intAsciiSize=intTempIndex;
				break;
			}
			if (!(byteUnicode[intIndex * 2] == 0)) 
				byteInAscii[intTempIndex] = byteUnicode[intIndex * 2];
			if(byteUnicode[intIndex*2]<0 || byteUnicode[(intIndex *2)+1]<0 || (byteUnicode[intIndex*2]==0 && byteUnicode[(intIndex*2)+1]!=0) || 
					(byteUnicode[intIndex*2]!=0 && byteUnicode[(intIndex*2)+1]!=0))
			{
				byte[] temp= new byte[2];
				temp[0]=byteUnicode[intIndex*2];
				temp[1]=byteUnicode[(intIndex*2)+1];
				byte[] uniBytes= new String(temp,"UTF-16le").getBytes("UTF-8");
				int len=0;
				while(len<uniBytes.length)
				{
					byteInAscii[intTempIndex+len]=uniBytes[len];
					len++;
				}
				intTempIndex=intTempIndex+len-1;
			}
			intTempIndex++;
		}
		byte[] finalByte = new byte[intAsciiSize];
		for (int i = 0; i < (intAsciiSize); i++) {
			finalByte[i] = byteInAscii[i];
		}
		return finalByte;
	}
	/**
	 * Converts Ascii string into unicode array
	 * 
	 * @param stringAscii
	 * 
	 * @return unicode array
	 */

	public byte[] psAsciiToUnicodeArray(String stringAscii) {
		//int intAsciiSize = stringAscii.length();
		byte[] byteAscii = new byte[stringAscii.length()];
		try {
			byteAscii = stringAscii.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		int intAsciiSize = byteAscii.length;
		byte[] byteUnicode = new byte[(intAsciiSize + 1) * 2];

		for (int intIndex = 0; intIndex < intAsciiSize; intIndex += 1) {
			byteUnicode[intIndex * 2] = byteAscii[intIndex];
			byteUnicode[intIndex * 2 + 1] = 0x00;
		}
		byteUnicode[intAsciiSize * 2] = 0x00;
		byteUnicode[intAsciiSize * 2 + 1] = 0x00;
		return byteUnicode;
	}
	
	public byte[] psUnicodeToAsciiArray(byte[] byteUnicode, int intUnicodeSize) {
		int intAsciiSize = intUnicodeSize / 2;
		byte[] byteInAscii = new byte[intAsciiSize + 1];
		for (int intIndex = 0; intIndex < intAsciiSize; intIndex += 1)
			byteInAscii[intIndex] = byteUnicode[intIndex * 2];
		byteInAscii[intAsciiSize] = 0x00;
		return byteInAscii;
	}
	
	public byte[] unicodeToASCIIArray(byte[] byteUnicode) {
		int intAsciiSize = byteUnicode.length / 2;
		byte[] byteInAscii = new byte[intAsciiSize + 1];
		for (int intIndex = 0; intIndex < intAsciiSize; intIndex += 1)
			byteInAscii[intIndex] = byteUnicode[intIndex * 2];
		byteInAscii[intAsciiSize] = 0x00;
		return byteInAscii;
	}

	public byte[] decode(byte[] b, int count) {
		byte[] result = teaObj.decode(b,count);
		return result;
	}
}
