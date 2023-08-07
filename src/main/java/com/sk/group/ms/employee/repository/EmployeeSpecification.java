/*
Copyright [2023] Shreyans Dilip Khobare
Proof of concept for Code Template
*/
package com.sk.group.ms.employee.repository;

import static org.springframework.data.jpa.domain.Specification.where;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.sk.group.ms.employee.constants.EmployeeServiceConstants;
import com.sk.group.shared.entity.Employee;
import com.sk.group.shared.entity.search.QueryOperator;
import com.sk.group.shared.entity.search.SpecificationFilter;
import com.sk.group.shared.implementation.organization.response.GetAllOrganizationResponse.Organization;

import jakarta.persistence.criteria.Join;

/**
 * @author - Shreyans Khobare
 */
@Component
public class EmployeeSpecification {

	public Specification<Employee> getSpecificationFromSpecificationFilters(
			List<SpecificationFilter> specificationFilter) {
		Specification<Employee> specification = where(createSpecification(specificationFilter.remove(0)));
		for (SpecificationFilter input : specificationFilter) {
			specification = specification.and(createSpecification(input));
		}
		return specification;
	}

	private Specification<Employee> createSpecification(SpecificationFilter input) {
		switch (input.getOperator()) {
		case QueryOperator.EQUALS:
			return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(input.getField()),
					castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
		case QueryOperator.NOT_EQ:
			return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(input.getField()),
					castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
		case QueryOperator.GREATER_THAN:
			return (root, query, criteriaBuilder) -> criteriaBuilder.gt(root.get(input.getField()),
					(Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
		case QueryOperator.LESS_THAN:
			return (root, query, criteriaBuilder) -> criteriaBuilder.lt(root.get(input.getField()),
					(Number) castToRequiredType(root.get(input.getField()).getJavaType(), input.getValue()));
		case QueryOperator.LIKE:
			return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(input.getField()),
					"%" + input.getValue() + "%");
		case QueryOperator.IN_STRING:
			return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(input.getField()))
					.value(castToRequiredStringType(root.get(input.getField()).getJavaType(), input.getStringValues()));
		case QueryOperator.IN_NUMBER:
			return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(input.getField()))
					.value(input.getNumberValues());
		case EmployeeServiceConstants.JOIN_EMPLOYEE_ORGANIZATION_ID:

			return (root, query, criteriaBuilder) -> {
				Join<Employee, Organization> employeeJoin = root.join("organizationId");
				return criteriaBuilder.in(employeeJoin.get("organizationId")).value(input.getNumberValues());
			};

		default:
			throw new RuntimeException("Operation not supported yet");
		}
	}

	@SuppressWarnings("unchecked")
	private Object castToRequiredType(Class fieldType, String value) {
		if (fieldType.isAssignableFrom(Double.class)) {
			return Double.valueOf(value);
		} else if (fieldType.isAssignableFrom(Integer.class)) {
			return Integer.valueOf(value);
		} else if (Enum.class.isAssignableFrom(fieldType)) {
			return Enum.valueOf(fieldType, value);
		} else if (fieldType.isAssignableFrom(Boolean.class)) {
			return Boolean.parseBoolean(value);
		} else if (fieldType.isAssignableFrom(Date.class)) {
			return Date.parse(value);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private Object castToRequiredStringType(Class fieldType, List<String> value) {
		List<Object> lists = new ArrayList<>();
		for (String s : value) {
			lists.add(castToRequiredType(fieldType, s));
		}
		return lists;
	}

}
