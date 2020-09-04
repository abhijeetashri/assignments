package com.thoughtworks.enums;

import java.util.TreeMap;

public enum RomanNumber {

	// Roman numerals are based on seven symbols
	I(1), //
	V(5), //
	X(10), //
	L(50), //
	C(100), //
	D(500), //
	M(1000);

	private final int value;
	private final static TreeMap<Integer, String> map = new TreeMap<>();

	static {

		// Initialize some known values for easy calculation.
		// Can add others too, based on necessity.
		map.put(1000, "M");
		map.put(900, "CM");
		map.put(500, "D");
		map.put(400, "CD");
		map.put(100, "C");
		map.put(90, "XC");
		map.put(50, "L");
		map.put(40, "XL");
		map.put(10, "X");
		map.put(9, "IX");
		map.put(5, "V");
		map.put(4, "IV");
		map.put(1, "I");
	}

	private RomanNumber(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

	public static TreeMap<Integer, String> map() {
		return map;
	}
}
