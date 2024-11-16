package com.lethanhbinh.bookservice.query.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseModel {
    private String bookId;
    private String name;
    private String author;
    private Boolean isReady;
}
