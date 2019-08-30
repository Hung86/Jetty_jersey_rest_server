package com.web.crypto;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class Base64EncoderDecoder {
	 private static final char S_BASE64CHAR[] = {
		        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
		        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
		        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
		        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
		        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
		        'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
		        '8', '9', '+', '/'
		    };
	 private static final char S_BASE64PAD = 61;
     private static final byte S_DECODETABLE[];

     static{
	        S_DECODETABLE = new byte[128];
	        for(int i = 0; i < S_DECODETABLE.length; i++) {
	        	S_DECODETABLE[i] = 127;
	        }
	        for(int j = 0; j < S_BASE64CHAR.length; j++) {
	        	S_DECODETABLE[S_BASE64CHAR[j]] = (byte)j;
	        }
	    }

	    public Base64EncoderDecoder()
	    {
	    }

	    private static int decode_i(char ac[], byte abyte0[], int i)
	    {
	        byte byte0 = 3;
	        if(ac[3] == '=')
	            byte0 = 2;
	        if(ac[2] == '=')
	            byte0 = 1;
	        byte byte1 = S_DECODETABLE[ac[0]];
	        byte byte2 = S_DECODETABLE[ac[1]];
	        byte byte3 = S_DECODETABLE[ac[2]];
	        byte byte4 = S_DECODETABLE[ac[3]];
	        switch(byte0)
	        {
	        case 1: // '\001'
	            abyte0[i] = (byte)(byte1 << 2 & 0xfc | byte2 >> 4 & 3);
	            return 1;

	        case 2: // '\002'
	            abyte0[i++] = (byte)(byte1 << 2 & 0xfc | byte2 >> 4 & 3);
	            abyte0[i] = (byte)(byte2 << 4 & 0xf0 | byte3 >> 2 & 0xf);
	            return 2;

	        case 3: // '\003'
	            abyte0[i++] = (byte)(byte1 << 2 & 0xfc | byte2 >> 4 & 3);
	            abyte0[i++] = (byte)(byte2 << 4 & 0xf0 | byte3 >> 2 & 0xf);
	            abyte0[i] = (byte)(byte3 << 6 & 0xc0 | byte4 & 0x3f);
	            return 3;
	        }
	        throw new RuntimeException("Internal Errror");
	    }

	    public static byte[] decode(char ac[], int i, int j)
	    {
	        char ac1[] = new char[4];
	        int k = 0;
	        byte abyte0[] = new byte[(j / 4) * 3 + 3];
	        int l = 0;
	        for(int i1 = i; i1 < i + j; i1++)
	        {
	            char c = ac[i1];
	            if(c == '=' || c < S_DECODETABLE.length && S_DECODETABLE[c] != 127)
	            {
	                ac1[k++] = c;
	                if(k == ac1.length)
	                {
	                    k = 0;
	                    l += decode_i(ac1, abyte0, l);
	                }
	            }
	        }

	        if(l == abyte0.length)
	        {
	            return abyte0;
	        } else
	        {
	            byte abyte1[] = new byte[l];
	            System.arraycopy(abyte0, 0, abyte1, 0, l);
	            return abyte1;
	        }
	    }

	    public static byte[] decode(String s)
	    {
	        char ac[] = new char[4];
	        int i = 0;
	        byte abyte0[] = new byte[(s.length() / 4) * 3 + 3];
	        int j = 0;
	        for(int k = 0; k < s.length(); k++)
	        {
	            char c = s.charAt(k);
	            if(c == '=' || c < S_DECODETABLE.length && S_DECODETABLE[c] != 127)
	            {
	                ac[i++] = c;
	                if(i == ac.length)
	                {
	                    i = 0;
	                    j += decode_i(ac, abyte0, j);
	                }
	            }
	        }

	        if(j == abyte0.length)
	        {
	            return abyte0;
	        } else
	        {
	            byte abyte1[] = new byte[j];
	            System.arraycopy(abyte0, 0, abyte1, 0, j);
	            return abyte1;
	        }
	    }

	    public static void decode(char ac[], int i, int j, OutputStream outputstream)
	        throws IOException
	    {
	        char ac1[] = new char[4];
	        int k = 0;
	        byte abyte0[] = new byte[3];
	        for(int l = i; l < i + j; l++)
	        {
	            char c = ac[l];
	            if(c == '=' || c < S_DECODETABLE.length && S_DECODETABLE[c] != 127)
	            {
	                ac1[k++] = c;
	                if(k == ac1.length)
	                {
	                    k = 0;
	                    int i1 = decode_i(ac1, abyte0, 0);
	                    outputstream.write(abyte0, 0, i1);
	                }
	            }
	        }

	    }

	    public static void decode(String s, OutputStream outputstream)
	        throws IOException
	    {
	        char ac[] = new char[4];
	        int i = 0;
	        byte abyte0[] = new byte[3];
	        for(int j = 0; j < s.length(); j++)
	        {
	            char c = s.charAt(j);
	            if(c == '=' || c < S_DECODETABLE.length && S_DECODETABLE[c] != 127)
	            {
	                ac[i++] = c;
	                if(i == ac.length)
	                {
	                    i = 0;
	                    int k = decode_i(ac, abyte0, 0);
	                    outputstream.write(abyte0, 0, k);
	                }
	            }
	        }

	    }

	    public static String encode(byte abyte0[])
	    {
	        return encode(abyte0, 0, abyte0.length);
	    }

	    public static String encode(byte abyte0[], int i, int j)
	    {
	        if(j <= 0)
	            return "";
	        char ac[] = new char[(j / 3) * 4 + 4];
	        int k = i;
	        int l = 0;
	        int i1;
	        for(i1 = j; i1 >= 3; i1 -= 3)
	        {
	            int j1 = ((abyte0[k] & 0xff) << 16) + ((abyte0[k + 1] & 0xff) << 8) + (abyte0[k + 2] & 0xff);
	            ac[l++] = S_BASE64CHAR[j1 >> 18];
	            ac[l++] = S_BASE64CHAR[j1 >> 12 & 0x3f];
	            ac[l++] = S_BASE64CHAR[j1 >> 6 & 0x3f];
	            ac[l++] = S_BASE64CHAR[j1 & 0x3f];
	            k += 3;
	        }

	        if(i1 == 1)
	        {
	            int k1 = abyte0[k] & 0xff;
	            ac[l++] = S_BASE64CHAR[k1 >> 2];
	            ac[l++] = S_BASE64CHAR[k1 << 4 & 0x3f];
	            ac[l++] = '=';
	            ac[l++] = '=';
	        } else
	        if(i1 == 2)
	        {
	            int l1 = ((abyte0[k] & 0xff) << 8) + (abyte0[k + 1] & 0xff);
	            ac[l++] = S_BASE64CHAR[l1 >> 10];
	            ac[l++] = S_BASE64CHAR[l1 >> 4 & 0x3f];
	            ac[l++] = S_BASE64CHAR[l1 << 2 & 0x3f];
	            ac[l++] = '=';
	        }
	        return new String(ac, 0, l);
	    }

	    public static void encode(byte abyte0[], int i, int j, OutputStream outputstream)
	        throws IOException
	    {
	        if(j <= 0)
	            return;
	        byte abyte1[] = new byte[4];
	        int k = i;
	        int l;
	        for(l = j; l >= 3; l -= 3)
	        {
	            int i1 = ((abyte0[k] & 0xff) << 16) + ((abyte0[k + 1] & 0xff) << 8) + (abyte0[k + 2] & 0xff);
	            abyte1[0] = (byte)S_BASE64CHAR[i1 >> 18];
	            abyte1[1] = (byte)S_BASE64CHAR[i1 >> 12 & 0x3f];
	            abyte1[2] = (byte)S_BASE64CHAR[i1 >> 6 & 0x3f];
	            abyte1[3] = (byte)S_BASE64CHAR[i1 & 0x3f];
	            outputstream.write(abyte1, 0, 4);
	            k += 3;
	        }

	        if(l == 1)
	        {
	            int j1 = abyte0[k] & 0xff;
	            abyte1[0] = (byte)S_BASE64CHAR[j1 >> 2];
	            abyte1[1] = (byte)S_BASE64CHAR[j1 << 4 & 0x3f];
	            abyte1[2] = 61;
	            abyte1[3] = 61;
	            outputstream.write(abyte1, 0, 4);
	        } else
	        if(l == 2)
	        {
	            int k1 = ((abyte0[k] & 0xff) << 8) + (abyte0[k + 1] & 0xff);
	            abyte1[0] = (byte)S_BASE64CHAR[k1 >> 10];
	            abyte1[1] = (byte)S_BASE64CHAR[k1 >> 4 & 0x3f];
	            abyte1[2] = (byte)S_BASE64CHAR[k1 << 2 & 0x3f];
	            abyte1[3] = 61;
	            outputstream.write(abyte1, 0, 4);
	        }
	    }

	    public static void encode(byte abyte0[], int i, int j, Writer writer)
	        throws IOException
	    {
	        if(j <= 0)
	            return;
	        char ac[] = new char[4];
	        int k = i;
	        int l = j;
	        int i1 = 0;
	        while(l >= 3) 
	        {
	            int j1 = ((abyte0[k] & 0xff) << 16) + ((abyte0[k + 1] & 0xff) << 8) + (abyte0[k + 2] & 0xff);
	            ac[0] = S_BASE64CHAR[j1 >> 18];
	            ac[1] = S_BASE64CHAR[j1 >> 12 & 0x3f];
	            ac[2] = S_BASE64CHAR[j1 >> 6 & 0x3f];
	            ac[3] = S_BASE64CHAR[j1 & 0x3f];
	            writer.write(ac, 0, 4);
	            k += 3;
	            l -= 3;
	            if((i1 += 4) % 76 == 0)
	                writer.write("\n");
	        }
	        if(l == 1)
	        {
	            int k1 = abyte0[k] & 0xff;
	            ac[0] = S_BASE64CHAR[k1 >> 2];
	            ac[1] = S_BASE64CHAR[k1 << 4 & 0x3f];
	            ac[2] = '=';
	            ac[3] = '=';
	            writer.write(ac, 0, 4);
	        } else
	        if(l == 2)
	        {
	            int l1 = ((abyte0[k] & 0xff) << 8) + (abyte0[k + 1] & 0xff);
	            ac[0] = S_BASE64CHAR[l1 >> 10];
	            ac[1] = S_BASE64CHAR[l1 >> 4 & 0x3f];
	            ac[2] = S_BASE64CHAR[l1 << 2 & 0x3f];
	            ac[3] = '=';
	            writer.write(ac, 0, 4);
	        }
	    }
}
