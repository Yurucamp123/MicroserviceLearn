package com.lethanhbinh.bookservice.command.aggregate;

import com.lethanhbinh.bookservice.command.command.CreateBookCommand;
import com.lethanhbinh.bookservice.command.command.DeleteBookCommand;
import com.lethanhbinh.bookservice.command.command.UpdateBookCommand;
import com.lethanhbinh.bookservice.command.event.BookCreatedEvent;
import com.lethanhbinh.bookservice.command.event.BookDeletedEvent;
import com.lethanhbinh.bookservice.command.event.BookUpdatedEvent;
import com.lethanhbinh.commonservice.command.UpdateStatusBookCommand;
import com.lethanhbinh.commonservice.event.UpdateStatusBookEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAggregate {
    @AggregateIdentifier
    private String bookId;
    private String name;
    private String author;
    private Boolean isReady;

    @CommandHandler
    public BookAggregate (CreateBookCommand command) {
        // create event according to create book command
        BookCreatedEvent event = new BookCreatedEvent();
        BeanUtils.copyProperties(command, event);

        // send event create book
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle (UpdateBookCommand command) {
        // create event according to create book command
        BookUpdatedEvent event = new BookUpdatedEvent();
        BeanUtils.copyProperties(command, event);

        // send event create book
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle (UpdateStatusBookCommand command) {
        // create event according to create book command
        UpdateStatusBookEvent event = new UpdateStatusBookEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle (DeleteBookCommand command) {
        // create event according to create book command
        BookDeletedEvent event = new BookDeletedEvent();
        BeanUtils.copyProperties(command, event);

        // send event create book
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on (BookCreatedEvent event) {
        this.bookId = event.getBookId();
        this.author = event.getAuthor();
        this.name = event.getName();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on (BookUpdatedEvent event) {
        this.bookId = event.getBookId();
        this.author = event.getAuthor();
        this.name = event.getName();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on (UpdateStatusBookEvent event) {
        this.bookId = event.getBookId();
        this.isReady = event.isReady();
    }

    @EventSourcingHandler
    public void on (BookDeletedEvent event) {
        this.bookId = event.getBookId();
    }
}
