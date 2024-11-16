package com.lethanhbinh.bookservice.command.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdatedEvent {
    private String bookId;
    private String name;
    private String author;
    private Boolean isReady;
}
