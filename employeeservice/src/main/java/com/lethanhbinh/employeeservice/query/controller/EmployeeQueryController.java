package com.lethanhbinh.employeeservice.query.controller;

import com.lethanhbinh.employeeservice.query.model.EmployeeResponseModel;
import com.lethanhbinh.employeeservice.query.queries.GetAllEmployeeQuery;
import com.lethanhbinh.employeeservice.query.queries.GetDetailEmployeeQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Get query")
@Slf4j
public class EmployeeQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @Operation(
            summary = "Get list employee",
            description = "Get API endpoint employee with filters",
            responses = {
                    @ApiResponse(
                            description = "SUCCESS",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping
    public List<EmployeeResponseModel> getAllEmployees (@RequestParam(required = false, defaultValue = "false") Boolean isDisciplined) {
        return queryGateway.query(
                new GetAllEmployeeQuery(isDisciplined),
                ResponseTypes.multipleInstancesOf(EmployeeResponseModel.class)).join();
    }

    @Operation(
            summary = "Get employee detail",
            description = "Get API endpoint employee detail",
            responses = {
                    @ApiResponse(
                            description = "SUCCESS",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/{employeeId}")
    public EmployeeResponseModel getEmployeeDetail (@PathVariable String employeeId) {
        return queryGateway.query(new GetDetailEmployeeQuery(employeeId),
                ResponseTypes.instanceOf(EmployeeResponseModel.class)).join();
    }
}
