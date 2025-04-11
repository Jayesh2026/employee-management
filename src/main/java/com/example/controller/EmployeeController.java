package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.model.Employee;
import com.example.response.SuccessResponse;
import com.example.service.EmployeeService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<SuccessResponse> addEmployee(@RequestBody @Valid Employee employee) {
        Employee savedEmployee = employeeService.addEmployee(employee);
        SuccessResponse successResponse = new SuccessResponse("Employee added successfully", HttpStatus.CREATED.value(), savedEmployee);
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getEmployee(@PathVariable Integer id) {
        Employee employee = employeeService.getEmployee(id);
        SuccessResponse successResponse = new SuccessResponse("Employee retrieved successfully", HttpStatus.OK.value(), employee);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployee();
        SuccessResponse successResponse = new SuccessResponse("All employees retrieved successfully", HttpStatus.OK.value(), employees);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> updateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
        employee.setId(id);

        Employee updatedEmployee = employeeService.updateEmployee(employee);
        SuccessResponse successResponse = new SuccessResponse("Employee updated successfully", HttpStatus.OK.value(), updatedEmployee);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteEmployee(@PathVariable Integer id) {
        Employee deletedEmployee = employeeService.deleteEmployee(id);
        SuccessResponse successResponse = new SuccessResponse("Employee deleted successfully", HttpStatus.OK.value(), deletedEmployee);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteAllEmployees() {
        List<Employee> deletedEmployees = employeeService.deleteAllEmployee();
        SuccessResponse successResponse = new SuccessResponse("All employees deleted successfully", HttpStatus.OK.value(), deletedEmployees);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}
