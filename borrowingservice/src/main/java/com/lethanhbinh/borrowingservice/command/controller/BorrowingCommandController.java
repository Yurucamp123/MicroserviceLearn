package com.lethanhbinh.borrowingservice.command.controller;

import com.lethanhbinh.borrowingservice.command.command.CreateBorrowingCommand;
import com.lethanhbinh.borrowingservice.command.model.CreateBorrowingRequest;
import com.lethanhbinh.borrowingservice.command.model.CreateBorrowingResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/borrowing")
public class BorrowingCommandController {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public CreateBorrowingResponse createBorrowing (@RequestBody CreateBorrowingRequest request) {
        CreateBorrowingCommand command = new CreateBorrowingCommand(
                UUID.randomUUID().toString(), request.getBookId(), request.getEmployeeId(), new Date()
        );

        commandGateway.sendAndWait(command);
        CreateBorrowingResponse response = new CreateBorrowingResponse();
        BeanUtils.copyProperties(command, response);
        return response;
    }
}
