package com.lethanhbinh.borrowingservice.command.aggregate;

import com.lethanhbinh.borrowingservice.command.command.CreateBorrowingCommand;
import com.lethanhbinh.borrowingservice.command.command.DeleteBorrowingCommand;
import com.lethanhbinh.borrowingservice.command.event.CreateBorrowingEvent;
import com.lethanhbinh.borrowingservice.command.event.DeleteBorrowingEvent;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Aggregate
public class BorrowingAggregate {
    @AggregateIdentifier
    private String id;

    private String bookId;
    private String employeeId;
    private Date borrowingDate;
    private Date returnDate;

    @CommandHandler
    public BorrowingAggregate(CreateBorrowingCommand command) {
        CreateBorrowingEvent event = new CreateBorrowingEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle (DeleteBorrowingCommand command) {
        DeleteBorrowingEvent event = new DeleteBorrowingEvent(command.getId());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on (CreateBorrowingEvent event) {
        this.id = event.getId();
        this.bookId = event.getBookId();
        this.employeeId = event.getEmployeeId();
        this.borrowingDate = event.getBorrowingDate();
    }

    @EventSourcingHandler
    public void on (DeleteBorrowingEvent event) {
        this.id = event.getId();
    }
}
