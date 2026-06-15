package org.app.springbootdemo.service.impl;

import org.app.springbootdemo.dto.EmployeeDto;
import org.app.springbootdemo.entity.Employee;
import org.app.springbootdemo.configs.EmployeeMapper;
import org.app.springbootdemo.repository.EmployeeRepository;
import org.app.springbootdemo.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.mapToEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.mapToDto(savedEmployee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employeeMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        return employeeMapper.mapToDto(employee);
    }


    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));


        existingEmployee.setName(employeeDto.getName());
        existingEmployee.setEmail(employeeDto.getEmail());



        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return employeeMapper.mapToDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
