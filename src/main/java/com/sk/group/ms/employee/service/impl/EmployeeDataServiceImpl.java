/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sk.group.ms.employee.repository.EmployeePersonalInfoRepository;
import com.sk.group.ms.employee.repository.EmployeeRepository;
import com.sk.group.ms.employee.request.EmployeeDataRequest;
import com.sk.group.ms.employee.service.EmployeeDataService;
import com.sk.group.shared.entity.Employee;
import com.sk.group.shared.entity.EmployeePersonalInfo;
import com.sk.group.shared.entity.OrganizationData;
import com.sk.group.shared.implementation.exception.GroupErrorCodes;
import com.sk.group.shared.implementation.exception.GroupException;
import com.sk.group.shared.implementation.response.employee.GetEmployeePersonalInfoResponse;
import com.sk.group.shared.implementation.response.employee.GetEmployeeResponse;
import com.sk.group.shared.implementation.response.employee.SaveEmployeeResponse;

/**
 * @author - Shreyans Khobare
 */
@Service
public class EmployeeDataServiceImpl implements EmployeeDataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDataServiceImpl.class);

	@Value("${message.employee.save.success:Employee save successful. EmployeeId is }")
	private String messageSaveSuccessful;

	@Value("${message.no.employee.personal.info.found:No personal info found for EmployeeId- }")
	private String messageEmployeePersonalInfoNotFound;

	@Value("${message.no.employee.found:No employee found for EmployeeId- }")
	private String messageEmployeeNotFound;

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
	public SaveEmployeeResponse saveEmployeeCompleteData(EmployeeDataRequest employeeDataRequest) {

		OrganizationData organization = OrganizationData.builder()
				.organizationId(employeeDataRequest.getOrganizationId()).build();

		Employee employee = Employee.builder().employmentActive(employeeDataRequest.isEmploymentActive())
				.organizationId(organization).firstName(employeeDataRequest.getFirstName())
				.lastName(employeeDataRequest.getLastName())
				.organizationEmail(employeeDataRequest.getOrganizationEmail()).title(employeeDataRequest.getTitle())
				.build();
		employee = employeeRepository.save(employee);

		EmployeePersonalInfo personalInfo = EmployeePersonalInfo.builder().employeeId(employee)
				.address(employeeDataRequest.getAddress()).joiningDate(employeeDataRequest.getJoiningDate())
				.leavingDate(employeeDataRequest.getLeavingDate()).mobileNumber(employeeDataRequest.getMobileNumber())
				.personalEmail(employeeDataRequest.getPersonalEmail()).build();
		personalInfo = employeePersonalInfoRepository.save(personalInfo);

		SaveEmployeeResponse response = new SaveEmployeeResponse();
		response.setEmployeeId(employee.getEmployeeId());
		response.setSuccessMessage(messageSaveSuccessful + employee.getEmployeeId());
		return response;

	}

	/**
	 * 
	 * @param employeeId
	 * @return
	 * @throws GroupException
	 */
	public GetEmployeePersonalInfoResponse getEmployeePersonalInfo(Long employeeId) throws GroupException {

		GetEmployeePersonalInfoResponse response = new GetEmployeePersonalInfoResponse();

		EmployeePersonalInfo employeeInfo = employeePersonalInfoRepository.findById(employeeId).get();
		if (null == employeeInfo) {

			LOGGER.error("Did not find any entry in EMPLOYEE_PERSONAL_INFO table for employeeId: " + employeeId);
			throw new GroupException(HttpStatus.NOT_FOUND, GroupErrorCodes.EMPLOYEE_PERSONAL_INFO_NOT_FOUND,
					messageEmployeePersonalInfoNotFound + employeeId);

		}

		response.setAddress(employeeInfo.getAddress());
		response.setEmployeeId(employeeInfo.getEmployeeId().getEmployeeId());
		response.setId(employeeInfo.getId());
		response.setJoiningDate(employeeInfo.getJoiningDate());
		response.setLeavingDate(employeeInfo.getLeavingDate());
		response.setMobileNumber(employeeInfo.getMobileNumber());
		response.setPersonalEmail(employeeInfo.getPersonalEmail());

		return response;

	}

	/**
	 * 
	 * @param employeeId
	 * @return
	 */
	public GetEmployeeResponse getEmployee(Long employeeId) throws GroupException {

		GetEmployeeResponse response = new GetEmployeeResponse();
		Employee employee = employeeRepository.findById(employeeId).get();
		if (null == employee) {

			LOGGER.error("Did not find any entry in EMPLOYEE table for employeeId: " + employeeId);
			throw new GroupException(HttpStatus.NOT_FOUND, GroupErrorCodes.EMPLOYEE_NOT_FOUND,
					messageEmployeeNotFound + employeeId);

		}

		response.setEmployeeId(employeeId);
		response.setEmployeePersonalInfoId(employee.getEmployeePersonalInfo().getId());
		response.setEmploymentActive(employee.isEmploymentActive());
		response.setFirstName(employee.getFirstName());
		response.setLastName(employee.getLastName());
		response.setOrganizationEmail(employee.getOrganizationEmail());
		response.setOrganizationId(employee.getOrganizationId().getOrganizationId());
		response.setTitle(employee.getTitle());
		
		return response;

	}

}
