/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.controller;

import static com.sk.group.shared.implementation.feign.FeignClientConstants.EMPLOYEE_SERVICE_BASE_MAPPING;
import static com.sk.group.shared.implementation.feign.FeignClientConstants.EMPLOYEE_SERVICE_FILTER_EMPLOYEES;
import static com.sk.group.shared.implementation.feign.FeignClientConstants.EMPLOYEE_SERVICE_GET_EMPLOYEE;
import static com.sk.group.shared.implementation.feign.FeignClientConstants.EMPLOYEE_SERVICE_GET_PERSONAL_INFO;
import static com.sk.group.shared.implementation.feign.FeignClientConstants.EMPLOYEE_SERVICE_SAVE_EMPLOYEE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sk.group.ms.employee.service.EmployeeDataService;
import com.sk.group.ms.employee.validator.EmployeeRequestValidator;
import com.sk.group.shared.implementation.employee.request.FilterEmployeesRequest;
import com.sk.group.shared.implementation.employee.request.SaveEmployeeRequest;
import com.sk.group.shared.implementation.employee.response.FilterEmployeesResponse;
import com.sk.group.shared.implementation.employee.response.GetEmployeePersonalInfoResponse;
import com.sk.group.shared.implementation.employee.response.GetEmployeeResponse;
import com.sk.group.shared.implementation.employee.response.SaveEmployeeResponse;
import com.sk.group.shared.implementation.exception.GroupException;

/**
 * @author - Shreyans Khobare
 */
@RestController
@RequestMapping(EMPLOYEE_SERVICE_BASE_MAPPING)
public class EmployeeServiceController {

	@Autowired
	private EmployeeDataService employeeDataService;
	
	@Autowired
	private EmployeeRequestValidator requestValidator;

	@PostMapping(value=EMPLOYEE_SERVICE_SAVE_EMPLOYEE)
	public ResponseEntity<SaveEmployeeResponse> saveEmployeeData(
			@RequestBody SaveEmployeeRequest employeePersonalInfo) throws GroupException {

		// Add validation logic here
		requestValidator.validateSaveEmployee(employeePersonalInfo);
		SaveEmployeeResponse response = employeeDataService.saveEmployeeCompleteData(employeePersonalInfo);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value=EMPLOYEE_SERVICE_GET_PERSONAL_INFO)
	public ResponseEntity<GetEmployeePersonalInfoResponse> getEmployeePersonalInfo(@PathVariable("employeePersonalInfoId") String employeePersonalInfoId) throws GroupException {

		GetEmployeePersonalInfoResponse employeePersonalInfo = employeeDataService.getEmployeePersonalInfo(Long.parseLong(employeePersonalInfoId));
		return ResponseEntity.ok(employeePersonalInfo);

	}

	@GetMapping(value=EMPLOYEE_SERVICE_GET_EMPLOYEE)
	public ResponseEntity<GetEmployeeResponse> getEmployee(@RequestParam("employeeId") String employeeId) throws GroupException {

		GetEmployeeResponse employee = employeeDataService.getEmployee(Long.parseLong(employeeId));
		return ResponseEntity.ok(employee);

	}
	
	@PostMapping(value = EMPLOYEE_SERVICE_FILTER_EMPLOYEES)
	public ResponseEntity<FilterEmployeesResponse> filterEmployees(@RequestBody FilterEmployeesRequest filterEmployeeRequest)
			throws GroupException {

		requestValidator.validateFilterEmployees(filterEmployeeRequest);
		FilterEmployeesResponse response = employeeDataService.filterEmployees(filterEmployeeRequest);
		
		return ResponseEntity.ok(response);
		
	}

}
