/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sk.group.shared.entity.EmployeePersonalInfo;

/**
@author - Shreyans Khobare
*/
@Repository
public interface EmployeePersonalInfoRepository extends CrudRepository<EmployeePersonalInfo, Long>{

}
