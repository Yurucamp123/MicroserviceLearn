package com.lethanhbinh.borrowingservice.command.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeleteBorrowingCommand {
    @TargetAggregateIdentifier
    private String id;
}
