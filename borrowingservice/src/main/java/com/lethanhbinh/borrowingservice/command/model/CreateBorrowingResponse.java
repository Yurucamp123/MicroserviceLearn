package com.lethanhbinh.borrowingservice.command.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBorrowingResponse {
    private String id;
    private String bookId;
    private String employeeId;
    private Date borrowingDate;
}