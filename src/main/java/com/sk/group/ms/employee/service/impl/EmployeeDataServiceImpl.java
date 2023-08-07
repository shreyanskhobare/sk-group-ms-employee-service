/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sk.group.ms.employee.constants.EmployeeServiceConstants;
import com.sk.group.ms.employee.repository.EmployeePersonalInfoRepository;
import com.sk.group.ms.employee.repository.EmployeeRepository;
import com.sk.group.ms.employee.repository.EmployeeSpecification;
import com.sk.group.ms.employee.service.EmployeeDataService;
import com.sk.group.shared.entity.Employee;
import com.sk.group.shared.entity.EmployeePersonalInfo;
import com.sk.group.shared.entity.OrganizationData;
import com.sk.group.shared.entity.search.QueryOperator;
import com.sk.group.shared.entity.search.SpecificationFilter;
import com.sk.group.shared.implementation.employee.request.FilterEmployeesRequest;
import com.sk.group.shared.implementation.employee.request.SaveEmployeeRequest;
import com.sk.group.shared.implementation.employee.response.FilterEmployeesResponse;
import com.sk.group.shared.implementation.employee.response.FilterEmployeesResponse.ResponseEmployee;
import com.sk.group.shared.implementation.employee.response.FilterEmployeesResponse.ResponseEmployee.ResponseEmployeePersonalInfo;
import com.sk.group.shared.implementation.employee.response.GetEmployeePersonalInfoResponse;
import com.sk.group.shared.implementation.employee.response.GetEmployeeResponse;
import com.sk.group.shared.implementation.employee.response.SaveEmployeeResponse;
import com.sk.group.shared.implementation.exception.GroupException;

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

	@Value("${message.no.employee.found.filter:No employee found for Filter Parameters- }")
	private String messageEmployeeNotFoundForFilter;

	@Autowired
	private EmployeePersonalInfoRepository employeePersonalInfoRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeSpecification employeeSpecification;

	/**
	 * In this method, we are persisting values in both EMPLOYEE and
	 * EMPLOYEE_PERSONAL_INFO table
	 * 
	 * @param employeeDataRequest
	 * @return
	 */
	@Override
	public SaveEmployeeResponse saveEmployeeCompleteData(SaveEmployeeRequest employeeDataRequest) {

		OrganizationData organization = OrganizationData.builder()
				.organizationId(employeeDataRequest.getOrganizationId()).build();

		Employee employee = Employee.builder().employmentActive(employeeDataRequest.isEmploymentActive())
				.organizationId(organization).firstName(employeeDataRequest.getFirstName().trim())
				.lastName(employeeDataRequest.getLastName().trim())
				.organizationEmail(employeeDataRequest.getOrganizationEmail().trim())
				.title(employeeDataRequest.getTitle().trim()).build();
		employee = employeeRepository.save(employee);

		EmployeePersonalInfo personalInfo = EmployeePersonalInfo.builder().employeeId(employee)
				.address(employeeDataRequest.getAddress().trim()).joiningDate(employeeDataRequest.getJoiningDate())
				.leavingDate(employeeDataRequest.getLeavingDate())
				.mobileNumber(employeeDataRequest.getMobileNumber().trim())
				.personalEmail(employeeDataRequest.getPersonalEmail().trim()).build();

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

		EmployeePersonalInfo employeeInfo = null;
		try {

			employeeInfo = employeePersonalInfoRepository.findById(employeeId).get();

		} catch (Exception e) {
			// NO ROWS Returned will also be thrown as an exception
			LOGGER.error(
					"Exception occured while trying to find any entry in EMPLOYEE_PERSONAL_INFO table for employeeId: "
							+ employeeId,
					e);
			throw new GroupException(HttpStatus.NOT_FOUND, EmployeeServiceConstants.EMPLOYEE_PERSONAL_INFO_NOT_FOUND,
					messageEmployeePersonalInfoNotFound + employeeId);

		}

		GetEmployeePersonalInfoResponse response = new GetEmployeePersonalInfoResponse();
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

		Employee employee = null;
		try {

			employee = employeeRepository.findById(employeeId).get();

		} catch (Exception e) {

			// NO ROWS Returned will also be thrown as an exception
			LOGGER.error(
					"Exception occured while trying to find any entry in EMPLOYEE table for employeeId: " + employeeId,
					e);
			throw new GroupException(HttpStatus.NOT_FOUND, EmployeeServiceConstants.EMPLOYEE_NOT_FOUND,
					messageEmployeeNotFound + employeeId);

		}

		GetEmployeeResponse response = new GetEmployeeResponse();
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

	/**
	 * In this method, we return a list of employees based on filter parameters
	 * passed in request
	 * 
	 * @param filterEmployeeRequest
	 * @return
	 * @throws GroupException
	 */
	public FilterEmployeesResponse filterEmployees(FilterEmployeesRequest filterEmployeeRequest) throws GroupException {

		List<Employee> employeeList = null;
		try {

			List<SpecificationFilter> specificationFilter = new ArrayList<>();

			if (!IterableUtils.isEmpty(filterEmployeeRequest.getOrganizationId())) {
				SpecificationFilter organizationFilter = SpecificationFilter.builder()
						.field("organizationId.organizationId")
						.operator(EmployeeServiceConstants.JOIN_EMPLOYEE_ORGANIZATION_ID)
						.numberValues(filterEmployeeRequest.getOrganizationId()).build();
				specificationFilter.add(organizationFilter);

			}

			if (!IterableUtils.isEmpty(filterEmployeeRequest.getEmployeeId())) {
				SpecificationFilter employeeIdFilter = SpecificationFilter.builder().field("employeeId")
						.operator(QueryOperator.IN_NUMBER).numberValues(filterEmployeeRequest.getEmployeeId()).build();
				specificationFilter.add(employeeIdFilter);
			}

			if (null != filterEmployeeRequest.getEmploymentActive()) {
				SpecificationFilter employmentActiveFilter = SpecificationFilter.builder().field("employmentActive")
						.operator(QueryOperator.EQUALS).value(filterEmployeeRequest.getEmploymentActive().toString())
						.build();
				specificationFilter.add(employmentActiveFilter);
			}
			
			Specification<Employee> specification = employeeSpecification
					.getSpecificationFromSpecificationFilters(specificationFilter);
			employeeList = employeeRepository.findAll(specification);

		} catch (Exception e) {

			// Any type of filter exception or no row returned exception will be thrown here
			LOGGER.error("Exception occured while trying to filter EMPLOYEE table with request parameters: "
					+ filterEmployeeRequest.toString(), e);
			throw new GroupException(HttpStatus.NOT_FOUND, EmployeeServiceConstants.EMPLOYEE_NOT_FOUND,
					messageEmployeeNotFoundForFilter + filterEmployeeRequest.toString());

		}

		FilterEmployeesResponse response = new FilterEmployeesResponse();

		if (!IterableUtils.isEmpty(employeeList)) {

			LOGGER.debug("Number of rows returned by filter: [" + filterEmployeeRequest.toString() + "] is : "
					+ employeeList.size());
			List<ResponseEmployee> responseEmployeeList = new ArrayList<>();

			for (Employee employee : employeeList) {
				LOGGER.debug("Creating response for employeeId: " + employee.getEmployeeId());
				ResponseEmployee responseEmployee = new ResponseEmployee();
				responseEmployee.setEmployeeId(employee.getEmployeeId());
				responseEmployee.setTitle(employee.getTitle());
				responseEmployee.setFirstName(employee.getFirstName());
				responseEmployee.setLastName(employee.getLastName());
				responseEmployee.setEmploymentActive(employee.isEmploymentActive());
				responseEmployee.setOrganizationEmail(employee.getOrganizationEmail());
				responseEmployee.setOrganizationId(employee.getOrganizationId().getOrganizationId());

				// Do not return EmployeePersonalInfo unless explicitly requested for
				if (null == filterEmployeeRequest.getReturnEmployeePersonalInfo()
						|| !filterEmployeeRequest.getReturnEmployeePersonalInfo()) {

					responseEmployee.setEmployeePersonalInfoId(employee.getEmployeePersonalInfo().getId());

				} else {

					// Set Employee Personal Info as well
					EmployeePersonalInfo employeePersonalInfo = employee.getEmployeePersonalInfo();
					ResponseEmployeePersonalInfo personalInfo = new ResponseEmployeePersonalInfo();

					personalInfo.setAddress(employeePersonalInfo.getAddress());
					personalInfo.setEmployeeId(employeePersonalInfo.getEmployeeId().getEmployeeId());
					personalInfo.setId(employeePersonalInfo.getId());
					personalInfo.setJoiningDate(employeePersonalInfo.getJoiningDate());
					personalInfo.setLeavingDate(employeePersonalInfo.getLeavingDate());
					personalInfo.setMobileNumber(employeePersonalInfo.getMobileNumber());
					personalInfo.setPersonalEmail(employeePersonalInfo.getPersonalEmail());

					responseEmployee.setEmployeePersonalInfo(personalInfo);

				}

				responseEmployeeList.add(responseEmployee);

			}

			response.setEmployeeList(responseEmployeeList);

		}

		return response;

	}

}
