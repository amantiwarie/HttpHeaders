package org.app.springbootdemo.configs;

import org.app.springbootdemo.dto.EmployeeDto;
import org.app.springbootdemo.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    // Entity -> DTO
    public EmployeeDto mapToDto(Employee employee) {

        EmployeeDto dto = new EmployeeDto();

        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());


        return dto;
    }

    // DTO -> Entity
    public Employee mapToEntity(EmployeeDto dto) {

        Employee employee = new Employee();

        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());

        return employee;
    }
}