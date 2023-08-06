/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.validator;

import com.sk.group.shared.implementation.employee.request.FilterEmployeesRequest;
import com.sk.group.shared.implementation.employee.request.SaveEmployeeRequest;
import com.sk.group.shared.implementation.exception.ControllerRequestValidationFailureException;

/**
@author - Shreyans Khobare
*/
public interface EmployeeRequestValidator {

	/**
	 * In this method, we validate the request parameters of '/saveEmployee' endpoint
	 * @param employeePersonalInfo
	 * @throws ControllerRequestValidationFailureException
	 */
	public void validateSaveEmployee(SaveEmployeeRequest employeePersonalInfo) throws ControllerRequestValidationFailureException;

	/**
	 * In this method, we validate the request parameters of '/filterEmployees' endpoint
	 * @param filterEmployeeRequest
	 * @throws ControllerRequestValidationFailureException
	 */
	public void validateFilterEmployees(FilterEmployeesRequest filterEmployeeRequest) throws ControllerRequestValidationFailureException;

}
