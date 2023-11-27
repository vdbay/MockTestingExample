package com.scadaprimacipta.mocktesting.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeTest {

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee(1L, "John Doe", "123 Main St", "50000");
    }

    @Test
    void testEmployeeGetters() {
        assertEquals(1L, employee.getId());
        assertEquals("John Doe", employee.getName());
        assertEquals("123 Main St", employee.getAddress());
        assertEquals("50000", employee.getSalary());
    }

    @Test
    void testEmployeeToString() {
        String expectedToString = "Employee(id=1, name=John Doe, address=123 Main St, salary=50000)";
        assertEquals(expectedToString, employee.toString());
    }

    @Test
    void testEmployeeSetter() {
        employee.setId(2L);
        employee.setName("Jane Doe");
        employee.setAddress("456 Oak St");
        employee.setSalary("60000");

        assertEquals(2L, employee.getId());
        assertEquals("Jane Doe", employee.getName());
        assertEquals("456 Oak St", employee.getAddress());
        assertEquals("60000", employee.getSalary());
    }
}
