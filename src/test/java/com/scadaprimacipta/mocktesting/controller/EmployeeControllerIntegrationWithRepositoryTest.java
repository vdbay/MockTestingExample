package com.scadaprimacipta.mocktesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scadaprimacipta.mocktesting.model.Employee;
import com.scadaprimacipta.mocktesting.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@WebMvcTest(EmployeeController.class)
@Slf4j
class EmployeeControllerIntegrationWithRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void getAllEmployee() throws Exception {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(
                new Employee(1L, "Employee 1", "Address 1", "1000"),
                new Employee(2L, "Employee 2", "Address 2", "2000")
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Employee 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address").value("Address 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Employee 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].address").value("Address 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].salary").value("2000"));

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById() throws Exception {
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.of(new Employee(1L, "Employee 1", "Address 1", "1000")));

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Employee 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Address 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value("1000"));

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeByIdNotFound() throws Exception {
        Employee employee = new Employee(1L, "Employee 1", "Address 1", "1000");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(employeeRepository, times(1)).findById(2L);
    }

    @Test
    void createEmployee() throws Exception {
        Employee employee = new Employee(1L, "Employee 1", "Address 1", "1000");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(MockMvcRequestBuilders.post("/employee/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Employee 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Address 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value("1000"));

        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployee() throws Exception {
        Employee employee = new Employee(1L, "Employee 1", "Address 1", "1000");
        Employee employeeUpdated = new Employee(1L, "Employee 1 Updated", "Address 1 Updated", "1000");
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employeeUpdated);

        mockMvc.perform(MockMvcRequestBuilders.put("/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Employee 1 Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Address 1 Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value("1000"));

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployeeNotFound() throws Exception {
        Employee employee = new Employee(1L, "Employee 1", "Address 1", "1000");
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void deleteEmployee() throws Exception {
        Employee employee = new Employee(1L, "Employee 1", "Address 1", "1000");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(MockMvcRequestBuilders.delete("/employee/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
