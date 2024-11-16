package com.lethanhbinh.employeeservice.command.event;

import com.lethanhbinh.employeeservice.command.data.Employee;
import com.lethanhbinh.employeeservice.command.data.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.DisallowReplay;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class EmployeeEventHandler {
    @Autowired
    private EmployeeRepository employeeRepository;

    @EventHandler
    public void on(CreateEmployeeEvent event) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(event, employee);
        employeeRepository.save(employee);
    }

    @EventHandler
    public void on(UpdateEmployeeEvent event) {
        Optional<Employee> employeeOptional = employeeRepository.findById(event.getId());
        employeeOptional.ifPresent(employee -> {
            BeanUtils.copyProperties(event, employee);
            employeeRepository.save(employee);
        });
    }

    @EventHandler
    @DisallowReplay
    public void on(DeleteEmployeeEvent event) {
//        Optional<Employee> employeeOptional = employeeRepository.findById(event.getId());
//        employeeOptional.ifPresent(employee -> {
//            employeeRepository.delete(employee);
//        });

        try {
            Employee employee = employeeRepository.findById(event.getId()).orElseThrow(() -> new Exception("Employee not available"));
            employeeRepository.delete(employee);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
