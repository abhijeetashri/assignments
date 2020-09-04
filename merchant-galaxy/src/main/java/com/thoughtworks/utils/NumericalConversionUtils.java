package com.thoughtworks.utils;

import com.thoughtworks.enums.RomanNumber;
import com.thoughtworks.exception.MerchantApplicationException;

public final class NumericalConversionUtils {

	private static final NumericalConversionUtils instance = new NumericalConversionUtils();
	private static final char EMPTY = '\0';

	public static NumericalConversionUtils getInstance() {
		return instance;
	}

	public String toRomanicalValue(int numericalValue) {

		// convert to string so its easy to iterate over it
		String number = String.valueOf(numericalValue);
		StringBuilder romanValue = new StringBuilder();
		int powerOfTen = 0;

		for (int i = number.length() - 1; i >= 0; i--, ++powerOfTen) {

			// parse the value and calculate the place (units, tens, hundredth...)
			int val = Integer.parseInt(String.valueOf(number.charAt(i)));
			val *= Math.pow(10, powerOfTen);

			// insert Roman numeral in beginning
			romanValue.insert(0, calculateRomanicalValue(val));
		}
		return romanValue.toString();
	}

	public int toNumericalValue(String romanNumber) {

		// check for null and emptiness.
		if (romanNumber == null || romanNumber.trim().length() == 0) {
			return 0;
		}

		int count = 1;
		char lastChar = EMPTY;
		int result = 0;

		for (int i = 0; i < romanNumber.length(); i++) {
			char currentChar = romanNumber.charAt(i);

			// keep a check on char repetition
			if (currentChar == lastChar)
				// increment for char repetition
				++count;
			else
				// reset count for inequality
				count = 1;

			// check if char repetition is okay or not.
			if (isCharRepititionOk(currentChar, count)) {
				result = calculateResult(lastChar, result, currentChar);
				lastChar = currentChar;
			}

		}
		return result;
	}

	private static int calculateResult(char lastChar, int result, char currentChar) {

		// get the values of current & last character to calculate.
		int lastCharValue = 0;
		if (lastChar != EMPTY) {
			lastCharValue = RomanNumber.valueOf(String.valueOf(lastChar).toUpperCase()).getValue();
		}
		int currentCharValue = RomanNumber.valueOf(String.valueOf(currentChar).toUpperCase()).getValue();

		int operand = currentCharValue;

		// When smaller values precede larger values, the smaller values are subtracted
		// from the larger values, and the result is
		// added to the total

		if (lastCharValue < currentCharValue) {
			result -= lastCharValue;
			operand = currentCharValue - lastCharValue;
		}

		result += operand;
		return result;
	}

	private static boolean isCharRepititionOk(char currentChar, int count) {

		if (

		// D, L, V cannot be repeated more than 1 time
		(count > 1 && (Character.valueOf(currentChar).toString().equalsIgnoreCase(RomanNumber.D.toString())
				|| Character.valueOf(currentChar).toString().equalsIgnoreCase(RomanNumber.L.toString())
				|| Character.valueOf(currentChar).toString().equalsIgnoreCase(RomanNumber.V.toString())))

				// I, X, C, M cannot be repeated more than 3 times
				|| (count > 3 && (Character.valueOf(currentChar).toString().equalsIgnoreCase(RomanNumber.I.toString())
						|| Character.valueOf(currentChar).toString().equalsIgnoreCase(RomanNumber.X.toString())
						|| Character.valueOf(currentChar).toString().equalsIgnoreCase(RomanNumber.C.toString())
						|| Character.valueOf(currentChar).toString().equalsIgnoreCase(RomanNumber.M.toString())))) {

			throw new MerchantApplicationException();
		}

		// char repetition is valid.
		return true;
	}

	private static String calculateRomanicalValue(int number) {
		if (number < 1) {
			return "";
		}

		// floor key helps in getting the nearest highest value
		int value = RomanNumber.map().floorKey(number);
		if (number == value) {
			return RomanNumber.map().get(number);
		}

		// append and get the value on the basis of difference
		return RomanNumber.map().get(value) + calculateRomanicalValue(number - value);
	}
}
