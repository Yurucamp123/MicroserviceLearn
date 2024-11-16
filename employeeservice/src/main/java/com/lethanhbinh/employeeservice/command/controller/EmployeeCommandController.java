package com.lethanhbinh.employeeservice.command.controller;

import com.lethanhbinh.employeeservice.command.command.CreateEmployeeCommand;
import com.lethanhbinh.employeeservice.command.command.DeleteEmployeeCommand;
import com.lethanhbinh.employeeservice.command.command.UpdateEmployeeCommand;
import com.lethanhbinh.employeeservice.command.data.EmployeeRepository;
import com.lethanhbinh.employeeservice.command.model.CreateEmployeeModel;
import com.lethanhbinh.employeeservice.command.model.DeleteEmployeeModel;
import com.lethanhbinh.employeeservice.command.model.EmployeeResponseModel;
import com.lethanhbinh.employeeservice.command.model.UpdateEmployeeModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Update query")
public class EmployeeCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Operation(
            summary = "Add new employee",
            description = "API endpoint add employee",
            responses = {
                    @ApiResponse(
                            description = "SUCCESS",
                            responseCode = "200"
                    )
            }
    )
    @PostMapping
    public EmployeeResponseModel addNewEmployee(@Valid @RequestBody CreateEmployeeModel model) {
        CreateEmployeeCommand command = new CreateEmployeeCommand(
                UUID.randomUUID().toString(), model.getFirstName(),
                model.getLastName(), model.getKin(), false);

        commandGateway.sendAndWait(command);
        EmployeeResponseModel employeeResponseModel = new EmployeeResponseModel();
        BeanUtils.copyProperties(command, employeeResponseModel);
        return employeeResponseModel;
    }

    @Operation(
            summary = "Update an existing employee by id",
            description = "API endpoint update employee",
            responses = {
                    @ApiResponse(
                            description = "SUCCESS",
                            responseCode = "200"
                    )
            }
    )
    @PutMapping("/{employeeId}")
    public EmployeeResponseModel updateEmployee(@Valid @RequestBody UpdateEmployeeModel model,
                                                @PathVariable String employeeId) throws Exception {
        if (employeeRepository.findById(employeeId).isEmpty()) {
            throw new Exception("Employee is not available");
        }

        UpdateEmployeeCommand command = new UpdateEmployeeCommand(
                employeeId, model.getFirstName(),
                model.getLastName(), model.getKin(), model.getIsDisciplined());

        commandGateway.sendAndWait(command);
        EmployeeResponseModel employeeResponseModel = new EmployeeResponseModel();
        BeanUtils.copyProperties(command, employeeResponseModel);
        return employeeResponseModel;
    }

    @Operation(
            summary = "Delete an existing employee by id",
            description = "API endpoint delete employee",
            responses = {
                    @ApiResponse(
                            description = "SUCCESS",
                            responseCode = "200"
                    )
            }
    )
    @DeleteMapping("/{employeeId}")
    public String deleteEmployee(@PathVariable String employeeId) throws Exception {
//        if (employeeRepository.findById(employeeId).isEmpty()) {
//            throw new Exception("Employee is not available");
//        }

        DeleteEmployeeCommand command = new DeleteEmployeeCommand(employeeId);
        return commandGateway.sendAndWait(command);
    }
}
