/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.request;

import java.util.Date;

import lombok.Data;

/**
@author - Shreyans Khobare
*/
@Data
public class EmployeeDataRequest {

	private long employeeId;
	private String title;
	private String firstName;
	private String lastName;
	private String organizationEmail;
	private boolean employmentActive = true;
	private String mobileNumber;
	private String personalEmail;
	private String address;
	private Date joiningDate;
	private Date leavingDate;
	private long organizationId;
	
}
