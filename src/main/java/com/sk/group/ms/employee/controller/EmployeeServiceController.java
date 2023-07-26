/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.controller;

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

import com.sk.group.ms.employee.request.EmployeeDataRequest;
import com.sk.group.ms.employee.service.EmployeeDataService;
import com.sk.group.shared.implementation.exception.GroupException;
import com.sk.group.shared.implementation.response.employee.GetEmployeePersonalInfoResponse;
import com.sk.group.shared.implementation.response.employee.GetEmployeeResponse;
import com.sk.group.shared.implementation.response.employee.SaveEmployeeResponse;

/**
 * @author - Shreyans Khobare
 */
@RestController
@RequestMapping("api/employee-service")
public class EmployeeServiceController {

	@Autowired
	private EmployeeDataService employeeDataService;

	@PostMapping(value="/saveEmployee")
	public ResponseEntity<SaveEmployeeResponse> saveEmployeeData(
			@RequestBody EmployeeDataRequest employeePersonalInfo) {

		// Add validation logic here
		SaveEmployeeResponse response = employeeDataService.saveEmployeeCompleteData(employeePersonalInfo);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value="/getEmployeePersonalInfo/{employeeId}")
	public ResponseEntity<GetEmployeePersonalInfoResponse> getEmployeePersonalInfo(@PathVariable("employeeId") String employeeId) throws GroupException {

		GetEmployeePersonalInfoResponse employeePersonalInfo = employeeDataService.getEmployeePersonalInfo(Long.parseLong(employeeId));
		return ResponseEntity.ok(employeePersonalInfo);

	}

	@GetMapping(value="/getEmployee")
	public ResponseEntity<GetEmployeeResponse> getEmployee(@RequestParam("employeeId") String employeeId) throws GroupException {

		GetEmployeeResponse employee = employeeDataService.getEmployee(Long.parseLong(employeeId));
		return ResponseEntity.ok(employee);

	}

}
