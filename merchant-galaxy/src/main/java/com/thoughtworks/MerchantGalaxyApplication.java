package com.thoughtworks;

import com.thoughtworks.service.CreditCostCalculatorService;
import com.thoughtworks.service.DataInitializer;
import com.thoughtworks.service.DataInitializer.LookupTable;
import com.thoughtworks.service.SimpleValueCalculatorService;

public class MerchantGalaxyApplication {

	public static void main(String[] args) throws InterruptedException {

		String[] unitCommandArray = { "glob is I", "prok is V", "pish is X", "tegj is L",
				"glob glob Silver is 34 Credits" };

		log("Intializing data...");
		DataInitializer.initialize(unitCommandArray);
		log("Data initialization finished.");
		log(LookupTable.getRomanUnitMappings());
		log(LookupTable.getCreditUnitMappings());

		log("Data processing started...");
		Thread.sleep(5000);

		String[] inputCommands = { "how much is pish tegj glob glob ?", "how many Credits is glob prok Silver ?",
				"how much wood could a woodchuck chuck if a woodchuck could chuck wood ?" };

		for (String inputString : inputCommands) {
			StringBuilder outputString = new StringBuilder();
			StringBuilder romanNumber = new StringBuilder();
			int result = 0;

			if (inputString.contains("Credits")) {
				result = CreditCostCalculatorService.getInstance().calculate(inputString, outputString, romanNumber,
						result);
			} else {
				result = SimpleValueCalculatorService.getInstance().calculate(inputString, outputString, romanNumber,
						result);
			}

			if (outputString.length() > 0)
				outputString.append(" is ").append(result);
			else
				outputString.append("I have no idea what you are talking about");

			log(outputString);
		}
	}

	private static void log(Object object) {
		System.out.println(object.toString());
	}
}
