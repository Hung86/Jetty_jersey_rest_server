/**
 * 
 */
package com.web.crypto;

/**
 * @author aswanth
 *
 */
public interface IJmBase64 {
	//************************************
	// Method:    IJmBase64
	// FullName:  IJmBase64::compressGZIP
	// Access:    public 
	// Returns:   boolean
	// Qualifier:
	// Parameter: File
	//************************************
	String encode(byte[] input);
	
	//************************************
	// Method:    DeCompressGZIP
	// FullName:  IJmGZip::DeCompressGZIP
	// Access:    public 
	// Returns:   boolean
	// Qualifier:
	// Parameter: File
	//************************************
	byte[] decode(String input);
}
