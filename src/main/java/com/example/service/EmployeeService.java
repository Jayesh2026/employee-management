package com.example.service;

import java.util.List;

import com.example.model.Employee;

public interface EmployeeService {

    public Employee addEmployee(Employee employee);
    public Employee getEmployee(Integer id);
    public List<Employee> getAllEmployee();
    public Employee updateEmployee(Employee employee);
    public Employee deleteEmployee(Integer id);
    public List<Employee> deleteAllEmployee();
    
}