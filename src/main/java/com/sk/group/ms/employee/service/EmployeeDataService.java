/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.service;

import com.sk.group.ms.employee.request.EmployeeDataRequest;
import com.sk.group.shared.entity.Employee;
import com.sk.group.shared.implementation.exception.GroupException;
import com.sk.group.shared.implementation.response.employee.GetEmployeePersonalInfoResponse;
import com.sk.group.shared.implementation.response.employee.GetEmployeeResponse;
import com.sk.group.shared.implementation.response.employee.SaveEmployeeResponse;

/**
@author - Shreyans Khobare
*/
public interface EmployeeDataService {
	
	/**
	 * In this method, we are persisting values in both EMPLOYEE and EMPLOYEE_PERSONAL_INFO table
	 * @param employeeDataRequest
	 */
	public SaveEmployeeResponse saveEmployeeCompleteData(EmployeeDataRequest employeeDataRequest);
	
	/**
	 * 
	 * @param employeeId
	 * @return
	 */
	public GetEmployeePersonalInfoResponse getEmployeePersonalInfo(Long employeeId) throws GroupException;
	
	/**
	 * 
	 * @param employeeId
	 * @return
	 */
	public GetEmployeeResponse getEmployee(Long employeeId) throws GroupException;

}
