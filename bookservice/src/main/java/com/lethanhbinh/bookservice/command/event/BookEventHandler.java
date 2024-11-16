package com.lethanhbinh.bookservice.command.event;

import com.lethanhbinh.bookservice.command.data.Book;
import com.lethanhbinh.bookservice.command.data.BookRepository;
import com.lethanhbinh.commonservice.event.UpdateStatusBookEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookEventHandler {
    @Autowired
    private BookRepository bookRepository;

    @EventHandler
    public void on (BookCreatedEvent event) {
        Book book = new Book();
        BeanUtils.copyProperties(event, book);
        bookRepository.save(book);
    }

    @EventHandler
    public void on (BookUpdatedEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getBookId());
        oldBook.ifPresent(book -> {
            book.setBookId(event.getBookId());
            book.setName(event.getName());
            book.setAuthor(event.getAuthor());
            book.setIsReady(event.getIsReady());
            bookRepository.save(book);
        });
    }

    @EventHandler
    public void on (UpdateStatusBookEvent event) {
        Optional<Book> book = bookRepository.findById(event.getBookId());
        book.ifPresent(bookItem -> {
            bookItem.setIsReady(event.isReady());
            bookRepository.save(bookItem);
        });
    }

    @EventHandler
    public void on (BookDeletedEvent event) {
        Optional<Book> oldBook = bookRepository.findById(event.getBookId());
        oldBook.ifPresent(book -> bookRepository.delete(book));
    }
}
