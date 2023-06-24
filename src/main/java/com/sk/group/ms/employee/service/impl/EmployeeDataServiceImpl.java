/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sk.group.ms.employee.repository.EmployeePersonalInfoRepository;
import com.sk.group.ms.employee.repository.EmployeeRepository;
import com.sk.group.ms.employee.request.EmployeeDataRequest;
import com.sk.group.ms.employee.service.EmployeeDataService;
import com.sk.group.shared.entity.Employee;
import com.sk.group.shared.entity.EmployeePersonalInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author - Shreyans Khobare
 */
@Service
@Slf4j
public class EmployeeDataServiceImpl implements EmployeeDataService {

	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * In this method, we are persisting values in both EMPLOYEE and
	 * EMPLOYEE_PERSONAL_INFO table
	 * 
	 * @param employeeDataRequest
	 * @return
	 */
	@Override
	public EmployeePersonalInfo saveEmployeeCompleteData(EmployeeDataRequest employeeDataRequest) {

		Employee employee = Employee.builder().employmentActive(employeeDataRequest.isEmploymentActive())
				.firstName(employeeDataRequest.getFirstName()).lastName(employeeDataRequest.getLastName())
				.organizationEmail(employeeDataRequest.getOrganizationEmail()).title(employeeDataRequest.getTitle())
				.build();
		employee = employeeRepository.save(employee);

		EmployeePersonalInfo personalInfo = EmployeePersonalInfo.builder().employeeId(employee.getEmployeeId())
				.address(employeeDataRequest.getAddress()).joiningDate(employeeDataRequest.getJoiningDate())
				.leavingDate(employeeDataRequest.getLeavingDate()).mobileNumber(employeeDataRequest.getMobileNumber())
				.personalEmail(employeeDataRequest.getPersonalEmail()).build();
		personalInfo = employeePersonalInfoRepository.save(personalInfo);

		return personalInfo;
	}

	/**
	 * 
	 * @param employeeId
	 * @return
	 */
	public EmployeePersonalInfo getEmployeePersonalInfo(Long employeeId) {

		return employeePersonalInfoRepository.findById(employeeId).get();

	}

	/**
	 * 
	 * @param employeeId
	 * @return
	 */
	public Employee getEmployee(Long employeeId) {

		return employeeRepository.findById(employeeId).get();

	}

}
