package com.lethanhbinh.employeeservice.command.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeModel {
    @NotBlank(message = "First name must be mandatory")
    private String firstName;

    @NotBlank(message = "Last name must be mandatory")
    private String lastName;

    @NotBlank(message = "KIN must be mandatory")
    private String kin;

    @NotNull(message = "IsDisciplined must be mandatory")
    private Boolean isDisciplined;
}
