/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.service;

import com.sk.group.shared.implementation.employee.request.FilterEmployeesRequest;
import com.sk.group.shared.implementation.employee.request.SaveEmployeeRequest;
import com.sk.group.shared.implementation.employee.response.FilterEmployeesResponse;
import com.sk.group.shared.implementation.employee.response.GetEmployeePersonalInfoResponse;
import com.sk.group.shared.implementation.employee.response.GetEmployeeResponse;
import com.sk.group.shared.implementation.employee.response.SaveEmployeeResponse;
import com.sk.group.shared.implementation.exception.GroupException;

/**
@author - Shreyans Khobare
*/
public interface EmployeeDataService {
	
	/**
	 * In this method, we are persisting values in both EMPLOYEE and EMPLOYEE_PERSONAL_INFO table
	 * @param employeeDataRequest
	 */
	public SaveEmployeeResponse saveEmployeeCompleteData(SaveEmployeeRequest employeeDataRequest);
	
	/**
	 * In this method, we return just a row from EMPLOYEE_PERSONAL_INFO table based on employeeId passed.
	 * @param employeeId
	 * @return
	 */
	public GetEmployeePersonalInfoResponse getEmployeePersonalInfo(Long employeeId) throws GroupException;
	
	/**
	 * In this method, we return just a row from EMPLOYEE table based on employeeId passed.
	 * @param employeeId
	 * @return
	 */
	public GetEmployeeResponse getEmployee(Long employeeId) throws GroupException;

	/**
	 * In this method, we return a list of employees based on filter parameters passed in request
	 * @param filterEmployeeRequest
	 * @return
	 */
	public FilterEmployeesResponse filterEmployees(FilterEmployeesRequest filterEmployeeRequest) throws GroupException;

}
