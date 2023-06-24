/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.service;

import com.sk.group.ms.employee.request.EmployeeDataRequest;
import com.sk.group.shared.entity.Employee;
import com.sk.group.shared.entity.EmployeePersonalInfo;

/**
@author - Shreyans Khobare
*/
public interface EmployeeDataService {
	
	/**
	 * In this method, we are persisting values in both EMPLOYEE and EMPLOYEE_PERSONAL_INFO table
	 * @param employeeDataRequest
	 */
	public EmployeePersonalInfo saveEmployeeCompleteData(EmployeeDataRequest employeeDataRequest);
	
	/**
	 * 
	 * @param employeeId
	 * @return
	 */
	public EmployeePersonalInfo getEmployeePersonalInfo(Long employeeId);
	
	/**
	 * 
	 * @param employeeId
	 * @return
	 */
	public Employee getEmployee(Long employeeId);

}
