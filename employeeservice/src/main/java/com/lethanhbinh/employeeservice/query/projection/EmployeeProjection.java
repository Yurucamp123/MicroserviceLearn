package com.lethanhbinh.employeeservice.query.projection;

import com.lethanhbinh.employeeservice.command.data.Employee;
import com.lethanhbinh.employeeservice.command.data.EmployeeRepository;
import com.lethanhbinh.employeeservice.query.model.EmployeeResponseModel;
import com.lethanhbinh.employeeservice.query.queries.GetAllEmployeeQuery;
import com.lethanhbinh.employeeservice.query.queries.GetDetailEmployeeQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EmployeeProjection {
    @Autowired
    private EmployeeRepository employeeRepository;

    @QueryHandler
    public List<EmployeeResponseModel> handle (GetAllEmployeeQuery query) {
        log.info("Calling get all employees");

        List<Employee> listEntity = employeeRepository.findAllByIsDisciplined(query.getIsDisciplined());
        return listEntity.stream().map(employee -> {
            EmployeeResponseModel model = new EmployeeResponseModel();
            BeanUtils.copyProperties(employee, model);
            return model;
        }).toList();
    }

    @QueryHandler
    public EmployeeResponseModel handle (GetDetailEmployeeQuery query) throws Exception {
        Employee employee = employeeRepository.findById(query.getId()).orElseThrow(() -> new Exception("Employee not available"));
        EmployeeResponseModel model = new EmployeeResponseModel();
        BeanUtils.copyProperties(employee, model);
        return model;
    }
}
