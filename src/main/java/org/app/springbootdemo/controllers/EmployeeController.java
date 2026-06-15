package org.app.springbootdemo.controllers;

import org.app.springbootdemo.dto.EmployeeDto;
import org.app.springbootdemo.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // NOTE: X-Api-Key validation is now handled centrally by ApiKeyFilter.
    // If a request reaches here, the key has already been verified.

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();

        // Custom response header: total count of records
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(employees.size()))
                .body(employees);
    }


    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }


    // Example: inspect ALL incoming headers (useful for debugging/learning)
    @GetMapping("/headers")
    public ResponseEntity<Map<String, String>> getRequestHeaders(
            @RequestHeader Map<String, String> headers) {
        return ResponseEntity.ok(headers);
    }


    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(
            @RequestHeader(value = "X-Request-Id", required = false) String requestId,
            @RequestBody EmployeeDto employeeDto) {  // @RequestBody reads JSON from request

        // If client didn't send a request id, generate one (common pattern for tracing)
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        EmployeeDto savedEmployee = employeeService.createEmployee(employeeDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("X-Request-Id", requestId)
                .body(savedEmployee);  // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeDto employeeDto) {

        EmployeeDto updatedEmployee = employeeService.updateEmployee(id, employeeDto);
        return ResponseEntity.ok(updatedEmployee);  // 200 OK
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
