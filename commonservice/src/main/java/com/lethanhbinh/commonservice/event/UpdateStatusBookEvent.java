package com.lethanhbinh.commonservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusBookEvent {

    @TargetAggregateIdentifier
    private String bookId;
    private boolean isReady;
    private String employeeId;
    private String borrowingId;
}
