package com.lethanhbinh.borrowingservice.command.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBorrowingRequest {
    private String bookId;
    private String employeeId;
}
