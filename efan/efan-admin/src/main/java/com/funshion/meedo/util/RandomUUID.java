package com.funshion.meedo.util;

public class RandomUUID {
	/** 产生一个不会重复的UUID */
	public static String genUUID() {
		return (NumberUtils.c10to32(System.currentTimeMillis()) + NumberUtils.c10to32(ThreadLocalRandom.current().nextInt(1000000)))
				.toUpperCase();
	}

	public static void main(String[] args) {
		// System.out.println("4:" + RandomUUID.randomUUID(4));
		// System.out.println("8:" + RandomUUID.randomUUID(8));
		// System.out.println("10:" + RandomUUID.randomUUID(10));
		// System.out.println("16:" + RandomUUID.randomUUID(16));
		// System.out.println(RandomUUID.randomUUID());
		//
		// String str = MD5Util.getStringMD5String(String.valueOf(new
		// Date().getTime()) + RandomUUID.randomUUID(16));
		// System.out.println("str:" + str + ", len:" + str.length());

		for (int i = 0; i < 2000; i++) {
			String str = RandomUUID.genUUID();
			System.out.println("str:" + str + ", len:" + str.length());
		}
	}
}
