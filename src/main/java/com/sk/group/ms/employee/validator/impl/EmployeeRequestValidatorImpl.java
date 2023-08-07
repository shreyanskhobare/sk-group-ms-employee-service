/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.validator.impl;

import static com.sk.group.shared.implementation.feign.FeignClientConstants.EMPLOYEE_SERVICE_SAVE_EMPLOYEE;
import static com.sk.group.shared.implementation.feign.FeignClientConstants.EMPLOYEE_SERVICE_FILTER_EMPLOYEES;

import java.util.Date;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sk.group.ms.employee.validator.EmployeeRequestValidator;
import com.sk.group.shared.implementation.employee.request.FilterEmployeesRequest;
import com.sk.group.shared.implementation.employee.request.SaveEmployeeRequest;
import com.sk.group.shared.implementation.exception.ControllerRequestValidationFailureException;
import com.sk.group.shared.implementation.exception.GroupErrorCodes;
import com.sk.group.shared.implementation.exception.GroupException;

import io.micrometer.common.util.StringUtils;

/**
@author - Shreyans Khobare
*/
@Service
public class EmployeeRequestValidatorImpl implements EmployeeRequestValidator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRequestValidatorImpl.class);
	
	@Value("${message.controller.request.class.empty:Request Class is empty}")
	private String requestClassEmpty;
	
	@Value("${message.controller.request.parameter.missing:Missing mandatory request parameter}")
	private String requestParameterMissing;
	
	/**
	 * In this method, we validate the request parameters of '/saveEmployee' endpoint
	 * @param employeePersonalInfo
	 * @throws GroupException
	 */
	@Override
	public void validateSaveEmployee(SaveEmployeeRequest request) throws ControllerRequestValidationFailureException {
		
		if (null == request) {
			requestClassEmpty(EMPLOYEE_SERVICE_SAVE_EMPLOYEE);
		}
		
		if (StringUtils.isBlank(request.getTitle())) {
			missingParameterException("title", EMPLOYEE_SERVICE_SAVE_EMPLOYEE);
		}
		
		if (StringUtils.isBlank(request.getFirstName())) {
			missingParameterException("firstName", EMPLOYEE_SERVICE_SAVE_EMPLOYEE);
		}
		
		if (StringUtils.isBlank(request.getLastName())) {
			missingParameterException("lastName", EMPLOYEE_SERVICE_SAVE_EMPLOYEE);
		}
		
		if (StringUtils.isBlank(request.getMobileNumber())) {
			missingParameterException("mobileNumber", EMPLOYEE_SERVICE_SAVE_EMPLOYEE);
		}
		
		if (StringUtils.isBlank(request.getOrganizationEmail())) {
			missingParameterException("organizationEmail", EMPLOYEE_SERVICE_SAVE_EMPLOYEE);
		}
		
		if (StringUtils.isBlank(request.getPersonalEmail())) {
			missingParameterException("personalEmail", EMPLOYEE_SERVICE_SAVE_EMPLOYEE);
		}
		
		if (StringUtils.isBlank(request.getAddress())) {
			missingParameterException("address", EMPLOYEE_SERVICE_SAVE_EMPLOYEE);
		}
		
		if (null == request.getOrganizationId()) {
			missingParameterException("organizationId", EMPLOYEE_SERVICE_SAVE_EMPLOYEE);
		}
		
		if (null == request.getJoiningDate()) {
			// Set current date in this case.
			request.setJoiningDate(new Date());
		}
		
	}
	
	/**
	 * In this method, we validate the request parameters of '/filterEmployees' endpoint
	 * @param filterEmployeeRequest
	 * @throws ControllerRequestValidationFailureException
	 */
	@Override
	public void validateFilterEmployees(FilterEmployeesRequest filterEmployeeRequest) throws ControllerRequestValidationFailureException {
		
		if (null == filterEmployeeRequest || (null == filterEmployeeRequest.getEmploymentActive()
				&& CollectionUtils.isEmpty(filterEmployeeRequest.getEmployeeId())
				&& CollectionUtils.isEmpty(filterEmployeeRequest.getOrganizationId()))) {
			requestClassEmpty(EMPLOYEE_SERVICE_FILTER_EMPLOYEES);
		}
		
	}
	
	/**
	 * Common method for throwing REQUEST_PARAMETER_MISSING exception.
	 * @param field
	 * @throws ControllerRequestValidationFailureException
	 */
	private void missingParameterException(String field, String endpoint) throws ControllerRequestValidationFailureException {
		
		LOGGER.error("Mandatory field [" + field + "] is missing in request of [" + endpoint + "]");
		throw new ControllerRequestValidationFailureException(GroupErrorCodes.REQUEST_PARAMETER_MISSING, requestParameterMissing);
		
	}
	

	/**
	 * Common method for throwing REQUEST_OBJECT_EMPTY exception
	 * @param endpoint
	 * @throws ControllerRequestValidationFailureException
	 */
	private void requestClassEmpty(String endpoint) throws ControllerRequestValidationFailureException {
		
		LOGGER.error("Request class of [" + endpoint + "] is null");
		throw new ControllerRequestValidationFailureException(GroupErrorCodes.REQUEST_OBJECT_EMPTY, requestClassEmpty);
		
	}

}
