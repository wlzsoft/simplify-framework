package com.meizu.simplify.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IdGeneratorTest {
	
	static final Map<Integer, String> map = new HashMap<Integer, String>();
	
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Map<String, Integer> m = new HashMap<String, Integer>();
		int total = 0;
		for (int i = 0; i < 10000001; i++) {
			String id = genereteId();
			int j = 1;
			while (null != m.get(id)) {
				id = genereteId();
				System.out.println("发生重复"+j+"次");
				j++;
				total++;
			} 
			m.put(id, i);
		}
		System.out.println(m.size());
		long end = System.currentTimeMillis();
		System.out.println("耗时"+(end-start)/1000);
		System.out.println("总重复次数："+total);
	}
	
	public static String genereteId() {
		Random r = new Random();
		int ri1 = r.nextInt(61);
		int ri2 = r.nextInt(61);
		int ri3 = r.nextInt(61);
		int ri4 = r.nextInt(61);
		int ri5 = r.nextInt(61);
		int ri6 = r.nextInt(61);
		StringBuffer bf = new StringBuffer();
		bf.append(map.get(ri1)).append(map.get(ri2)).append(map.get(ri3)).append(map.get(ri4)).append(map.get(ri5)).append(map.get(ri6));
		return bf.toString();
	}

	static {
		map.put(0, "q");
		map.put(1, "w");
		map.put(2, "e");
		map.put(3, "r");
		map.put(4, "t");
		map.put(5, "y");
		map.put(6, "u");
		map.put(7, "i");
		map.put(8, "o");
		map.put(9, "p");
		map.put(10, "a");
		map.put(11, "s");
		map.put(12, "d");
		map.put(13, "f");
		map.put(14, "g");
		map.put(15, "h");
		map.put(16, "j");
		map.put(17, "k");
		map.put(18, "l");
		map.put(19, "z");
		map.put(20, "x");
		map.put(21, "c");
		map.put(22, "v");
		map.put(23, "b");
		map.put(24, "n");
		map.put(25, "m");
		map.put(26, "1");
		map.put(27, "2");
		map.put(28, "3");
		map.put(29, "4");
		map.put(30, "5");
		map.put(31, "6");
		map.put(32, "7");
		map.put(33, "8");
		map.put(34, "9");
		map.put(35, "0");
		map.put(36, "Q");
		map.put(37, "W");
		map.put(38, "E");
		map.put(39, "R");
		map.put(40, "T");
		map.put(41, "Y");
		map.put(42, "U");
		map.put(43, "I");
		map.put(44, "O");
		map.put(45, "P");
		map.put(46, "A");
		map.put(47, "S");
		map.put(48, "D");
		map.put(49, "F");
		map.put(50, "G");
		map.put(51, "H");
		map.put(52, "J");
		map.put(53, "K");
		map.put(54, "L");
		map.put(55, "Z");
		map.put(56, "X");
		map.put(57, "C");
		map.put(58, "V");
		map.put(59, "B");
		map.put(60, "N");
		map.put(61, "M");
	}
	
}
