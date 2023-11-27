package com.scadaprimacipta.mocktesting.controller;

import com.scadaprimacipta.mocktesting.model.Employee;
import com.scadaprimacipta.mocktesting.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void getAllEmployee() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(
                new Employee(1L, "Employee 1", "Address 1", "1000"),
                new Employee(2L, "Employee 2", "Address 2", "2000")
        ));

        // Act
        var result = employeeController.getAllEmployee();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Employee 1", result.get(0).getName());
        assertEquals("Employee 2", result.get(1).getName());

        // Verify
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee(1L, "Employee 1", "Address 1", "1000")));
        var result = employeeController.getEmployeeById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Employee 1", result.getBody().getName());
    }

    @Test
    void getEmployeeByIdNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        var result = employeeController.getEmployeeById(1L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void createEmployee() {
        var employee = new Employee(1L, "Employee 1", "Address 1", "1000");
        when(employeeRepository.save(employee)).thenReturn(employee);
        var result = employeeController.createEmployee(employee);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Employee 1", result.getBody().getName());

        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void updateEmployee() {
        var employee = new Employee(1L, "Employee 1", "Address 1", "1000");
        var employeeUpdated = new Employee(1L, "Employee 1 Updated", "Address 1 Updated", "1000");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employeeUpdated);
        var result = employeeController.updateEmployee(1L, employee);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Employee 1 Updated", result.getBody().getName());
        assertEquals("Address 1 Updated", result.getBody().getAddress());

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void updateEmployeeNotFound() {
           var employee = new Employee(1L, "Employee 1", "Address 1", "1000");
            when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
            var result = employeeController.updateEmployee(1L, employee);

            assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

            verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void deleteEmployee() {
        var employee = new Employee(1L, "Employee 1", "Address 1", "1000");

        lenient().when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        var result = employeeController.deleteEmployee(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(employeeRepository, times(1)).deleteById(1L);
    }


}
