package com.web.crypto;

public class CJmBase64 implements IJmBase64 {
	Base64EncoderDecoder base64EncoderDecoder = null;
	public CJmBase64() {
		base64EncoderDecoder = new Base64EncoderDecoder();
	}
	
	@Override
	public String encode(byte[] input) {
		return base64EncoderDecoder.encode(input);
	}
	
	@Override
	public byte[] decode(String input) {
		return base64EncoderDecoder.decode(input);
	}	
}
