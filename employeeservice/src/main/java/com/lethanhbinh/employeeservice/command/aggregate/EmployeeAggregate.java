package com.lethanhbinh.employeeservice.command.aggregate;

import com.lethanhbinh.employeeservice.command.command.CreateEmployeeCommand;
import com.lethanhbinh.employeeservice.command.command.DeleteEmployeeCommand;
import com.lethanhbinh.employeeservice.command.command.UpdateEmployeeCommand;
import com.lethanhbinh.employeeservice.command.event.CreateEmployeeEvent;
import com.lethanhbinh.employeeservice.command.event.DeleteEmployeeEvent;
import com.lethanhbinh.employeeservice.command.event.UpdateEmployeeEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Aggregate
public class EmployeeAggregate {
    @AggregateIdentifier
    private String id;
    private String firstName;
    private String lastName;
    private String kin;
    private Boolean isDisciplined;

    @CommandHandler
    public EmployeeAggregate (CreateEmployeeCommand createEmployeeCommand) {
        CreateEmployeeEvent createEmployeeEvent = new CreateEmployeeEvent();
        BeanUtils.copyProperties(createEmployeeCommand, createEmployeeEvent);
        AggregateLifecycle.apply(createEmployeeEvent);
    }

    @CommandHandler
    public void handle (UpdateEmployeeCommand command) {
        UpdateEmployeeEvent event = new UpdateEmployeeEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle (DeleteEmployeeCommand command) {
        DeleteEmployeeEvent event = new DeleteEmployeeEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on (CreateEmployeeEvent event) {
        this.id = event.getId();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.kin = event.getKin();
        this.isDisciplined = event.getIsDisciplined();
    }

    @EventSourcingHandler
    public void on (UpdateEmployeeEvent event) {
        this.id = event.getId();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.kin = event.getKin();
        this.isDisciplined = event.getIsDisciplined();
    }

    @EventSourcingHandler
    public void on (DeleteEmployeeEvent event) {
        this.id = event.getId();
    }
}
