package com.web.crypto;

public class TEAEncryption {
	private int _key[];  // The 128 bit key.
	   private byte _keyBytes[];  // original key as found
	   private int _padding;      // amount of padding added in byte --> integer conversion.

	   /**
	   * Accepts key for enciphering/deciphering.
	   *
	   * @throws ArrayIndexOutOfBoundsException if the key isn't the correct length.
	   *
	   * @param key 128 bit (16 byte) key.
	   */
		public TEAEncryption(byte key[]) {
			int klen = key.length;
			_key = new int[4];
			// Incorrect key length throws exception.
			if (klen != 16)
				throw new ArrayIndexOutOfBoundsException(this.getClass().getName()
						+ ": Key is not 16 bytes: " + klen);
			int j;
			int i;
			for (i = 0, j = 0; j < klen; j += 4, i++) {
				_key[i] = (key[j + 3] << 24)
				| (((key[j + 2]) & 0xff) << 16)
				| (((key[j + 1]) & 0xff) << 8) | ((key[j]) & 0xff);
			}
			_keyBytes = key;
			// save for toString.
		}

		public TEAEncryption(int key[]) {
			_key = key;
		}

		public TEAEncryption() {

		}

		/** * Representation of TEA class */
		public String toString() {
			String tea = this.getClass().getName();
			tea += ": Tiny Encryption Algorithm (TEA) key: " + getHex(_keyBytes);
			return tea;
		}

		/**
		 * * Encipher two ints. * Replaces the original contents of the parameters
		 * with the results. * The integers are usually created from 8 bytes. * The
		 * usual way to collect bytes to the int array is: * byte ba[] = { .... };
		 * int v[] = new int[2]; v[0] = (ba[j] << 24 ) | (((ba[j+1])&0xff) << 16) |
		 * (((ba[j+2])&0xff) << 8) | ((ba[j+3])&0xff); v[1] = (ba[j+4] << 24 ) |
		 * (((ba[j+5])&0xff) << 16) | (((ba[j+6])&0xff) << 8) | ((ba[j+7])&0xff); v
		 * = encipher(v);
		 * 
		 * * @param v two int array as input. * * @return array of two ints,
		 * enciphered.
		 */
		public int[] encipher(int v[]) {
			int y = v[0];
			int z = v[1];
			int sum = 0;
			int delta = 0x9E3779B9;
			int a = _key[0];
			int b = _key[1];
			int c = _key[2];
			int d = _key[3];
			int n = 32;
			while (n-- > 0) {
				sum += delta;
				y += (z << 4) + a ^ z + sum ^ (z >>> 5) + b;
				z += (y << 4) + c ^ y + sum ^ (y >>> 5) + d;
			}
			v[0] = y;
			v[1] = z;
			return v;
		}

		/**
		 * Decipher two ints. Replaces the original contents of the parameters with
		 * the results. The integers are usually decocted to 8 bytes. The decoction
		 * of the ints to bytes can be done this way.
		 * 
		 * int x[] = decipher(ins); outb[j] = (byte)(x[0] >>> 24); outb[j+1] =
		 * (byte)(x[0] >>> 16); outb[j+2] = (byte)(x[0] >>> 8); outb[j+3] =
		 * (byte)(x[0]); outb[j+4] = (byte)(x[1] >>> 24); outb[j+5] = (byte)(x[1]
		 * >>> 16); outb[j+6] = (byte)(x[1] >>> 8); outb[j+7] = (byte)(x[1]);
		 * 
		 * @param v
		 *            int array of 2
		 * 
		 * @return deciphered int array of 2
		 **/
		public int[] decipher(int v[]) {
			int y = v[0];
			int z = v[1];
			int sum = 0xC6EF3720;
			int delta = 0x9E3779B9;
			int a = _key[0];
			int b = _key[1];
			int c = _key[2];
			int d = _key[3];
			int n = 32;
			// sum = delta<<5, in general sum = delta * n
			while (n-- > 0) {
				z -= (y << 4) + c ^ y + sum ^ (y >>> 5) + d;
				y -= (z << 4) + a ^ z + sum ^ (z >>> 5) + b;
				sum -= delta;
			}
			v[0] = y;
			v[1] = z;
			return v;
		}

		/**
		 * Byte wrapper for encoding. Converts bytes to ints. Padding will be added
		 * if required.
		 * 
		 * @param b
		 *            incoming byte array
		 * @param byte count
		 * 
		 * @return integer conversion array, possibly with padding.
		 * 
		 * @see #padding
		 **/
		public int[] encode(byte b[], int count) {
			int j;
			int i;
			int bLen = count;
			byte bp[] = b;
			_padding = bLen % 8;
			if (_padding != 0) // Add some padding, if necessary.
			{
				_padding = 8 - (bLen % 8);
				bp = new byte[bLen + _padding];
				System.arraycopy(b, 0, bp, 0, bLen);
				bLen = bp.length;
			}
			int intCount = bLen / 4;
			int r[] = new int[2];
			int out[] = new int[intCount];
			for (i = 0, j = 0; j < bLen; j += 8, i += 2) {
				// Java's unforgivable lack of unsigneds causes more bit twiddling
				// than this language really needs.
				/* ORGCODE */// r[0] = (bp[j] << 24 ) | (((bp[j+1])&0xff) << 16) |
				// (((bp[j+2])&0xff) << 8) | ((bp[j+3])&0xff);
				/* FORPS */r[0] = (bp[j + 3] << 24) | (((bp[j + 2]) & 0xff) << 16)
						| (((bp[j + 1]) & 0xff) << 8) | ((bp[j + 0]) & 0xff);
				/* ORGCODE */// r[1] = (bp[j+4] << 24 ) | (((bp[j+5])&0xff) << 16) |
				// (((bp[j+6])&0xff) << 8) | ((bp[j+7])&0xff);
				/* FORPS */r[1] = (bp[j + 7] << 24) | (((bp[j + 6]) & 0xff) << 16)
						| (((bp[j + 5]) & 0xff) << 8) | ((bp[j + 4]) & 0xff);
				encipher(r);
				out[i] = r[0];
				out[i + 1] = r[1];
			}

			return out;
		}

		/**
		 * Report how much padding was done in the last encode.
		 * 
		 * @return bytes of padding added
		 **/
		public int padding() {
			return _padding;
		}

		/**
		 * Convert a byte array to ints and then decode. There may be some padding
		 * at the end of the byte array from the previous encode operation.
		 * 
		 * @param b
		 *            bytes to decode
		 * @param count
		 *            number of bytes in the array to decode
		 * 
		 * @return byte array of decoded bytes.
		 **/
		public byte[] decode(byte b[], int count) {
			int i;
			int j;
			int intCount = count / 4;
			int ini[] = new int[intCount];
			for (i = 0, j = 0; i < intCount/*-1*/; i += 2, j += 8) {
				/* ORGCODE */// ini[i] = (b[j] << 24 ) | (((b[j+1])&0xff) << 16) |
				// (((b[j+2])&0xff) << 8) | ((b[j+3])&0xff);
				/* FORPS */ini[i] = ((b[j + 3] << 24) | (((b[j + 2]) & 0xff) << 16)
						| (((b[j + 1]) & 0xff) << 8) | ((b[j + 0]) & 0xff));
				/* ORGCODE */// ini[i+1] = (b[j+4] << 24 ) | (((b[j+5])&0xff) << 16)
				// | (((b[j+6])&0xff) << 8) | ((b[j+7])&0xff);
				/* FORPS */ini[i + 1] = ((b[j + 7] << 24)
						| (((b[j + 6]) & 0xff) << 16) | (((b[j + 5]) & 0xff) << 8) | ((b[j + 4]) & 0xff));
			}
			return decode(ini);
		}

		/**
		 * Decode an integer array. * There may be some padding at the end of the
		 * byte array from * the previous encode operation.
		 * 
		 * @param b
		 *            bytes to decode
		 * @param count
		 *            number of bytes in the array to decode
		 * 
		 * @return byte array of decoded bytes.
		 **/
		public byte[] decode(int b[]) {
			// create the large number and start stripping ints out, two at a time.
			int intCount = b.length;
			byte outb[] = new byte[intCount * 4];
			int tmp[] = new int[2];
			// decipher all the ints.
			int i;
			int j;
			for (j = 0, i = 0; i < intCount/*-1*/; i += 2, j += 8) {
				tmp[0] = b[i];
				tmp[1] = b[i + 1];
				decipher(tmp);
				/* ORGCODE */// outb[j] = (byte)(tmp[0] >>> 24);
				/* ORGCODE */// outb[j+1] = (byte)(tmp[0] >>> 16);
				/* ORGCODE */// outb[j+2] = (byte)(tmp[0] >>> 8);
				/* ORGCODE */// outb[j+3] = (byte)(tmp[0]);
				/* ORGCODE */// outb[j+4] = (byte)(tmp[1] >>> 24);
				/* ORGCODE */// outb[j+5] = (byte)(tmp[1] >>> 16);
				/* ORGCODE */// outb[j+6] = (byte)(tmp[1] >>> 8);
				/* ORGCODE */// outb[j+7] = (byte)(tmp[1]);
				/* FORPS */outb[j + 3] = (byte) (tmp[0] >>> 24);
				/* FORPS */outb[j + 2] = (byte) (tmp[0] >>> 16);
				/* FORPS */outb[j + 1] = (byte) (tmp[0] >>> 8);
				/* FORPS */outb[j + 0] = (byte) (tmp[0]);
				/* FORPS */outb[j + 7] = (byte) (tmp[1] >>> 24);
				/* FORPS */outb[j + 6] = (byte) (tmp[1] >>> 16);
				/* FORPS */outb[j + 5] = (byte) (tmp[1] >>> 8);
				/* FORPS */outb[j + 4] = (byte) (tmp[1]);
			}
			return outb;
		}

		/**
		 * Convert an array of ints into a hex string.
		 * 
		 * @param enc
		 *            Array of integers.
		 * 
		 * @return String hexadecimal representation of the integer array.
		 * 
		 * @throws ArrayIndexOutOfBoundsException
		 *             if the array doesn't contain pairs of integers.
		 **/
		public String binToHex(int enc[]) throws ArrayIndexOutOfBoundsException {
			// The number of ints should always be a multiple of two as required by
			// TEA (64 bits).
			if ((enc.length % 2) == 1)
				throw new ArrayIndexOutOfBoundsException(
						"Odd number of ints found: " + enc.length);
			StringBuffer sb = new StringBuffer();
			byte outb[] = new byte[8];
			for (int i = 0; i < enc.length; i += 2) {
				outb[0] = (byte) (enc[i] >>> 24);
				outb[1] = (byte) (enc[i] >>> 16);
				outb[2] = (byte) (enc[i] >>> 8);
				outb[3] = (byte) (enc[i]);
				outb[4] = (byte) (enc[i + 1] >>> 24);
				outb[5] = (byte) (enc[i + 1] >>> 16);
				outb[6] = (byte) (enc[i + 1] >>> 8);
				outb[7] = (byte) (enc[i + 1]);
				sb.append(getHex(outb));
			}
			return sb.toString();
		}

		/**
		 * Display bytes in HEX.
		 * 
		 * @param b
		 *            bytes to display.
		 * 
		 * @return string representation of the bytes.
		 **/
		public String getHex(byte b[]) {
			StringBuffer r = new StringBuffer();
			final char hex[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'A', 'B', 'C', 'D', 'E', 'F' };
			for (int i = 0; i < b.length; i++) {
				int c = ((b[i]) >>> 4) & 0xf;
				r.append(hex[c]);
				c = ((int) b[i] & 0xf);
				r.append(hex[c]);
			}
			return r.toString();
		}

		/**
		 * Pad a string out to the proper length with the given character.
		 * 
		 * @param str
		 *            Plain text string.
		 * 
		 * @param pc
		 *            Padding character.
		 **/
		public String padPlaintext(String str, char pc) {
			StringBuffer sb = new StringBuffer(str);
			int padding = sb.length() % 8;
			for (int i = 0; i < padding; i++)
				sb.append(pc);
			return sb.toString();
		}

		/**
		 * Pad a string out to the proper length with spaces.
		 * 
		 * @param str
		 *            Plain text string.
		 **/
		public String padPlaintext(String str) {
			return padPlaintext(str, ' ');
		}

		public byte[] intToByte(int[] intIntArray) {
			byte[] byteByteArray = new byte[intIntArray.length * 4];
			for (int intIndxArray = 0; intIndxArray < intIntArray.length; intIndxArray += 1) {
				byteByteArray[intIndxArray * 4 + 0] = (byte) ((intIntArray[intIndxArray]) & 0xFF);
				byteByteArray[intIndxArray * 4 + 1] = (byte) ((intIntArray[intIndxArray] >> 8) & 0xFF);
				byteByteArray[intIndxArray * 4 + 2] = (byte) ((intIntArray[intIndxArray] >> 16) & 0xFF);
				byteByteArray[intIndxArray * 4 + 3] = (byte) ((intIntArray[intIndxArray] >> 24) & 0xFF);
			}
			return byteByteArray;
		}

}
