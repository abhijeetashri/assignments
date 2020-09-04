package com.thoughtworks.service;

import java.util.Map;

import com.thoughtworks.ApplicationConstants;
import com.thoughtworks.service.DataInitializer.LookupTable;
import com.thoughtworks.utils.NumericalConversionUtils;

public class CreditCostCalculatorService implements CalculationService {

	private static final CreditCostCalculatorService instance = new CreditCostCalculatorService();

	public static CreditCostCalculatorService getInstance() {
		return instance;
	}

	@Override
	public int calculate(String inputString, StringBuilder outputString, StringBuilder romanNumber, int result) {
		for (String is : inputString.split(ApplicationConstants.TOKEN_SPLITTER)) {
			if (LookupTable.getRomanUnitMappings().containsKey(is))
				romanNumber.append(LookupTable.getRomanUnitMappings().get(is));
			if (LookupTable.getCreditUnitMappings().containsKey(is)) {
				int quantity = NumericalConversionUtils.getInstance().toNumericalValue(romanNumber.toString());
				int costPerUnit = LookupTable.getCreditUnitMappings().get(is);
				for (int i = 0; i < romanNumber.length(); i++) {
					final String number = String.valueOf(romanNumber.charAt(i));
					String unitType = LookupTable.getRomanUnitMappings().entrySet().stream()
							.filter(entry -> entry.getValue().equals(number)).map(Map.Entry::getKey).findFirst()
							.orElse(null);
					outputString.append(unitType).append(ApplicationConstants.TOKEN_SPLITTER);
				}
				outputString.append(is);
				result = quantity * costPerUnit;
			}
		}
		return result;
	}

}
