package com.thoughtworks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.thoughtworks.exception.MerchantApplicationException;
import com.thoughtworks.utils.NumericalConversionUtils;

public class MerchantGalaxyApplicationTests {

	private static final String ROMAN_NUMBERS_REGEX = "[IVXLCDM]+";

	@Test
	public void checkNumberValidation() {
		Assertions.assertThrows(MerchantApplicationException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				NumericalConversionUtils.getInstance().toNumericalValue("XXXXIX");
			}
		});
	}

	@Test
	public void calculateValue_EmptyString_Successful() {
		Assertions.assertEquals(0, NumericalConversionUtils.getInstance().toNumericalValue(""));
	}

	@Test
	public void calculateValue_Simple1_Successful() {
		Assertions.assertEquals(2006, NumericalConversionUtils.getInstance().toNumericalValue("MMVI"));
	}

	@Test
	public void calculateValue_Simple2_Successful() {
		Assertions.assertEquals(42, NumericalConversionUtils.getInstance().toNumericalValue("XLII"));
	}

	@Test
	public void calculateValue_Complicated_Successful() {
		Assertions.assertEquals(1944, NumericalConversionUtils.getInstance().toNumericalValue("MCMXLIV"));
	}

	@Test
	public void calculateRomanNumberValue_1903_Successful() {
		Assertions.assertEquals("MCMIII", NumericalConversionUtils.getInstance().toRomanicalValue(1903));
	}

	@Test
	public void calculateRomanNumberValue_2000_Successful() {
		Assertions.assertEquals("MM", NumericalConversionUtils.getInstance().toRomanicalValue(2000));
	}

	@Test
	public void testRomanNumberRegex_Failure() {
		Assertions.assertEquals(false, "ZZ".matches(ROMAN_NUMBERS_REGEX));
	}

	@Test
	public void testRomanNumberRegex_Success() {
		Assertions.assertEquals(true, "XXXXIX".matches(ROMAN_NUMBERS_REGEX));
	}
}
