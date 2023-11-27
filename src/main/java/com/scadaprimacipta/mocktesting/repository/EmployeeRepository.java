package com.scadaprimacipta.mocktesting.repository;
import com.scadaprimacipta.mocktesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
}
