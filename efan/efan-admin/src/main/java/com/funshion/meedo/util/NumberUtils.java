package com.funshion.meedo.util;

import java.math.BigDecimal;

/**
 * 
 * @author kevin
 * 
 */
public class NumberUtils {

	/**
	 * 10进制转64进制
	 * 
	 * @param num
	 * @return
	 */
	public static String c10to64(long num) {
		return enode(num, 6);
	}

	/**
	 * 10进制转32进制
	 * 
	 * @param num
	 * @return
	 */
	public static String c10to32(long num) {
		return enode(num, 5);
	}

	/**
	 * 10进制转16进制
	 * 
	 * @param num
	 * @return
	 */
	public static String c10to16(long num) {
		return Long.toHexString(num).toUpperCase();
	}

	/**
	 * 10进制转8进制
	 * 
	 * @param num
	 * @return
	 */
	public static String c10to8(long num) {
		return Long.toOctalString(num);
	}

	/**
	 * 10进制转4进制
	 * 
	 * @param num
	 * @return
	 */
	public static String c10to4(long num) {
		return enode(num, 2);
	}

	/**
	 * 10进制转2进制
	 * 
	 * @param num
	 * @return
	 */
	public static String c10to2(long num) {
		return Long.toBinaryString(num);
	}

	/**
	 * 64进制转10进制
	 * 
	 * @param num
	 * @return
	 */
	public static long c64to10(String num) {
		return decode(num, 6);
	}

	/**
	 * 32进制转10进制
	 * 
	 * @param num
	 * @return
	 */
	public static long c32to10(String num) {
		return decode(num, 5);
	}

	/**
	 * 16进制转10进制
	 * 
	 * @param num
	 * @return
	 */
	public static long c16to10(String num) {
		return decode(num, 4);
	}

	/**
	 * 8进制转10进制
	 * 
	 * @param num
	 * @return
	 */
	public static long c8to10(String num) {
		return decode(num, 3);
	}

	/**
	 * 4进制转10进制
	 * 
	 * @param num
	 * @return
	 */
	public static long c4to10(String num) {
		return decode(num, 2);
	}

	/**
	 * 2进制转10进制
	 * 
	 * @param num
	 * @return
	 */
	public static long c2to10(String num) {
		return decode(num, 1);
	}

	/**
	 * 把10进制的数字转换成 {@code 1<<shift }进制字符窜形式
	 * 
	 * @param number
	 * @param shift
	 * @return
	 */
	private static String enode(long number, int shift) {
		int bit = 1 << shift;
		char[] buf = new char[bit];
		int charPos = bit;
		int radix = 1 << shift;
		long mask = radix - 1;
		do {
			buf[--charPos] = DIGITS[(int) (number & mask)];
			number >>>= shift;
		} while (number != 0);
		return new String(buf, charPos, (bit - charPos));
	}

	/**
	 * 把X进制的字符串转换成{@code 1<<shift }进制
	 * 
	 * @param decompStr
	 * @return
	 */
	private static long decode(String decompStr, int shift) {
		long result = 0;
		for (int i = decompStr.length() - 1; i >= 0; i--) {
			if (i == decompStr.length() - 1) {
				result += getCharIndexNum(decompStr.charAt(i));
				continue;
			}
			for (int j = 0; j < DIGITS.length; j++) {
				if (decompStr.charAt(i) == DIGITS[j]) {
					result += ((long) j) << (shift * (decompStr.length() - 1 - i));
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param ch
	 * @return
	 */
	private static long getCharIndexNum(char ch) {
		int num = ((int) ch);
		if (num >= 48 && num <= 57) {
			return num - 48;
		} else if (num >= 97 && num <= 122) {
			return num - 87;
		} else if (num >= 65 && num <= 90) {
			return num - 29;
		} else if (num == 45) {
			return 62;
		} else if (num == 95) {
			return 63;
		}
		return 0;
	}

	public static void long2Byte(byte[] bb, long x) {
		bb[0] = (byte) (x >> 56);
		bb[1] = (byte) (x >> 48);
		bb[2] = (byte) (x >> 40);
		bb[3] = (byte) (x >> 32);
		bb[4] = (byte) (x >> 24);
		bb[5] = (byte) (x >> 16);
		bb[6] = (byte) (x >> 8);
		bb[7] = (byte) (x >> 0);
	}

	public static byte[] long2Bytes(long x) {
		byte[] result = new byte[8];
		result[0] = (byte) (x >> 56);
		result[1] = (byte) (x >> 48);
		result[2] = (byte) (x >> 40);
		result[3] = (byte) (x >> 32);
		result[4] = (byte) (x >> 24);
		result[5] = (byte) (x >> 16);
		result[6] = (byte) (x >> 8);
		result[7] = (byte) (x >> 0);
		return result;
	}

	public static long getLong(byte[] bb) {
		return ((((long) bb[0] & 0xff) << 56) | (((long) bb[1] & 0xff) << 48) | (((long) bb[2] & 0xff) << 40)
				| (((long) bb[3] & 0xff) << 32) | (((long) bb[4] & 0xff) << 24) | (((long) bb[5] & 0xff) << 16)
				| (((long) bb[6] & 0xff) << 8) | (((long) bb[7] & 0xff) << 0));
	}

	public static byte[] intToBytes(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}
	
	/**
	 * 精确double小数点后第几位
	 * @param d 
	 * @param scale
	 * 			第几位
	 * @return
	 */
	public static double decimalRound(double d, int scale) {
		BigDecimal bg = new BigDecimal(d);
        return bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	private final static char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', '-', '_', };

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			c10to64(time);
		}
		System.out.println(System.currentTimeMillis() - time);
	}
}
