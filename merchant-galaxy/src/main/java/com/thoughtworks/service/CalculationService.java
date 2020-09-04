package com.thoughtworks.service;

@FunctionalInterface
public interface CalculationService {

	int calculate(String inputString, StringBuilder outputString, StringBuilder romanNumber, int result);
}
