package com.lethanhbinh.bookservice.command.controller;

import com.lethanhbinh.bookservice.command.command.CreateBookCommand;
import com.lethanhbinh.bookservice.command.command.DeleteBookCommand;
import com.lethanhbinh.bookservice.command.command.UpdateBookCommand;
import com.lethanhbinh.bookservice.command.data.Book;
import com.lethanhbinh.bookservice.command.model.BookRequestModel;
import com.lethanhbinh.bookservice.command.model.BookResponseModel;
import com.lethanhbinh.commonservice.services.KafkaService;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
public class BookCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private KafkaService kafkaService;

    @PostMapping
    private BookResponseModel addNewBook (@Valid @RequestBody BookRequestModel bookRequestModel) {
        CreateBookCommand createBookCommand = new CreateBookCommand
                (UUID.randomUUID().toString(), bookRequestModel.getName(), bookRequestModel.getAuthor(), true);
        commandGateway.sendAndWait(createBookCommand);
        return new BookResponseModel(createBookCommand.getBookId(), createBookCommand.getName(),
                createBookCommand.getAuthor(), createBookCommand.getIsReady());
    }

    @PutMapping
    private BookResponseModel updateBook (@Valid @RequestBody BookRequestModel bookRequestModel) {
        UpdateBookCommand updateBookCommand = new UpdateBookCommand
                (bookRequestModel.getBookId(), bookRequestModel.getName(), bookRequestModel.getAuthor(), bookRequestModel.getIsReady());
        commandGateway.sendAndWait(updateBookCommand);
        return new BookResponseModel(updateBookCommand.getBookId(), updateBookCommand.getName(),
                updateBookCommand.getAuthor(), updateBookCommand.getIsReady());
    }

    @DeleteMapping("/{bookId}")
    private String deleteBook (@PathVariable String bookId) {
        DeleteBookCommand deleteBookCommand = new DeleteBookCommand(bookId);
        commandGateway.sendAndWait(deleteBookCommand);
        return "Delete successfully";
    }

    @PostMapping("/sendMessage")
    public void sendMessage (@RequestBody String message) {
        kafkaService.sendMessage("test", message);
    }
}
