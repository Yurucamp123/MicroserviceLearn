package com.lethanhbinh.borrowingservice.command.event;

import com.lethanhbinh.borrowingservice.command.data.Borrowing;
import com.lethanhbinh.borrowingservice.command.data.BorrowingRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BorrowingEventHandler {
    @Autowired
    private BorrowingRepository borrowingRepository;

    @EventHandler
    public void on(CreateBorrowingEvent event) {
        Borrowing borrowing = new Borrowing();
        BeanUtils.copyProperties(event, borrowing);
        borrowingRepository.save(borrowing);
    }

    @EventHandler
    public void on(DeleteBorrowingEvent event) {
        Optional<Borrowing> borrowing = borrowingRepository.findById(event.getId());
        borrowing.ifPresent(borrowRecorded -> borrowingRepository.delete(borrowRecorded));
    }
}
