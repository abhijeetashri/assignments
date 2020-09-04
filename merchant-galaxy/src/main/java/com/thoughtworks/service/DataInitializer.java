package com.thoughtworks.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.ApplicationConstants;
import com.thoughtworks.utils.NumericalConversionUtils;

public final class DataInitializer {

	private static Map<String, String> romanUnitMapping = new HashMap<>();
	private static Map<String, Integer> creditUnitMapping = new HashMap<>();

	public static void initialize(String[] commands) {

		for (String unitCommand : commands) {
			Arrays.stream(unitCommand.split(ApplicationConstants.TOKEN_SPLITTER))
					.filter(str -> str.matches(ApplicationConstants.ROMAN_NUMBERS_REGEX))
					.forEach(value -> romanUnitMapping.put(unitCommand.split(ApplicationConstants.TOKEN_SPLITTER)[0],
							value));
		}

		Arrays.stream(commands).filter(cmd -> !cmd.contains("how") && cmd.contains("Credits")).forEach(command -> {
			StringBuilder romanNumeral = new StringBuilder();
			String key = null;
			boolean hasKeyFormed = false;
			boolean isValueExtracted = false;
			for (String inputCmdToken : command.split(ApplicationConstants.TOKEN_SPLITTER)) {
				if (romanUnitMapping.containsKey(inputCmdToken)) {
					romanNumeral.append(romanUnitMapping.get(inputCmdToken));
				} else if (!hasKeyFormed) {
					key = romanNumeral.insert(0, inputCmdToken + ApplicationConstants.KEY_SEPARATOR).toString();
					hasKeyFormed = true;
				}
				if (hasKeyFormed && inputCmdToken.matches(ApplicationConstants.NUMERICAL_VALUE_REGEX)) {
					String[] keyTokens = key.split(ApplicationConstants.KEY_SEPARATOR);
					int numericalValue = NumericalConversionUtils.getInstance().toNumericalValue(keyTokens[1]);
					int valuePerUnit = Integer.parseInt(inputCmdToken) / numericalValue;
					creditUnitMapping.put(keyTokens[0], valuePerUnit);
					isValueExtracted = true;
				}
				if (hasKeyFormed && isValueExtracted)
					break;
			}
		});
	}

	public static class LookupTable {

		public static Map<String, String> getRomanUnitMappings() {
			return Collections.unmodifiableMap(romanUnitMapping);
		}

		public static Map<String, Integer> getCreditUnitMappings() {
			return Collections.unmodifiableMap(creditUnitMapping);
		}
	}
}
