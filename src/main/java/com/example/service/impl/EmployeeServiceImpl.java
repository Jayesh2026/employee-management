package com.example.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.exception.EmailAlreadyExistsException;
import com.example.exception.EmployeeNotFoundException;
import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import com.example.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee addEmployee(Employee employee) {
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            logger.warn("Employee with email {} already exists", employee.getEmail());
            throw new EmailAlreadyExistsException("Email already exists: " + employee.getEmail());
        }

        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Employee saved successfully: {}", savedEmployee);
        return savedEmployee;
    }

    @Override
    public Employee getEmployee(Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            logger.info("Employee found with ID {}: {}", id, employee.get());
            return employee.get();
        } else {
            logger.error("No employee found with ID {}", id);
            throw new EmployeeNotFoundException("Employee not found with ID: " + id);
        }
    }

    @Override
    public List<Employee> getAllEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        logger.info("Retrieved all employees: {}", employees);
        return employees;
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        if (employeeRepository.existsById(employee.getId())) {
            Employee updatedEmployee = employeeRepository.save(employee);
            logger.info("Employee data updated successfully: {}", updatedEmployee);
            return updatedEmployee;
        } else {
            logger.error("Cannot update - No employee found with ID {}", employee.getId());
            throw new EmployeeNotFoundException("Cannot update - Employee not found with ID: " + employee.getId());
        }
    }
    
    @Override
    public Employee deleteEmployee(Integer id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employeeRepository.deleteById(id);
            logger.info("Employee deleted successfully with ID: {}", id);
            return employee.get();
        } else {
            logger.error("Cannot delete - No employee found with ID {}", id);
            throw new EmployeeNotFoundException("Cannot delete - Employee not found with ID: " + id);
        }
    }

    @Override
    public List<Employee> deleteAllEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        employeeRepository.deleteAll();
        logger.info("All employees deleted successfully.");
        return employees;
    }
}
