package com.lethanhbinh.bookservice.query.controller;

import com.lethanhbinh.commonservice.model.BookResponseCommonModel;
import com.lethanhbinh.bookservice.query.queries.GetAllBooksQuery;
import com.lethanhbinh.commonservice.query.GetBookDetailQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookQueryModel {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping("/{bookId}")
    public BookResponseCommonModel getBookDetail (@PathVariable String bookId) {
        GetBookDetailQuery getBookDetailQuery = new GetBookDetailQuery();
        getBookDetailQuery.setBookId(bookId);

        // return book detail
        return queryGateway.query(getBookDetailQuery, ResponseTypes.instanceOf(BookResponseCommonModel.class)).join();
    }

    @GetMapping("")
    public List<BookResponseCommonModel> getAllBooks () {
        GetAllBooksQuery getAllBooksQuery = new GetAllBooksQuery();

        return queryGateway.query
                (getAllBooksQuery, ResponseTypes.multipleInstancesOf(BookResponseCommonModel.class)).join();
    }
}
