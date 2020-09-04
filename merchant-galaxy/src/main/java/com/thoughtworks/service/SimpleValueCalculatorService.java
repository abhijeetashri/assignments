package com.thoughtworks.service;

import java.util.Arrays;

import com.thoughtworks.ApplicationConstants;
import com.thoughtworks.service.DataInitializer.LookupTable;
import com.thoughtworks.utils.NumericalConversionUtils;

public class SimpleValueCalculatorService implements CalculationService {

	private static final SimpleValueCalculatorService instance = new SimpleValueCalculatorService();

	public static SimpleValueCalculatorService getInstance() {
		return instance;
	}

	@Override
	public int calculate(String inputString, StringBuilder outputString, StringBuilder romanNumber, int result) {
		Arrays.stream(inputString.split(ApplicationConstants.TOKEN_SPLITTER))
				.filter(is -> LookupTable.getRomanUnitMappings().containsKey(is)).forEach(os -> {
					romanNumber.append(LookupTable.getRomanUnitMappings().get(os));
					outputString.append(os).append(ApplicationConstants.TOKEN_SPLITTER);
				});
		return NumericalConversionUtils.getInstance().toNumericalValue(romanNumber.toString());
	}

}
