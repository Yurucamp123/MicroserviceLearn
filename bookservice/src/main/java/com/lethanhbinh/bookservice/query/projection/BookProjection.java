package com.lethanhbinh.bookservice.query.projection;

import com.lethanhbinh.bookservice.command.data.Book;
import com.lethanhbinh.bookservice.command.data.BookRepository;
import com.lethanhbinh.commonservice.model.BookResponseCommonModel;
import com.lethanhbinh.bookservice.query.queries.GetAllBooksQuery;
import com.lethanhbinh.commonservice.query.GetBookDetailQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookProjection {

    @Autowired
    private BookRepository bookRepository;

    @QueryHandler
    public BookResponseCommonModel handleGetBookDetail(GetBookDetailQuery getBookDetailQuery) throws Exception {
        BookResponseCommonModel bookResponseModel = new BookResponseCommonModel();
        Optional<Book> book = bookRepository.findById(getBookDetailQuery.getBookId());
        book.ifPresent(item -> {
            BeanUtils.copyProperties(item, bookResponseModel);
        });
        return bookResponseModel;
    }

    @QueryHandler
    public List<BookResponseCommonModel> handleGetAllBooks(GetAllBooksQuery getAllBooksQuery) {
        List<BookResponseCommonModel> bookResponseModels = new ArrayList<>();
        try {
            List<Book> listEntity = bookRepository.findAll();
            // stream.map giong map ben javascript
            bookResponseModels = listEntity.stream().map(book -> {
                BookResponseCommonModel bookResponseModel = new BookResponseCommonModel();
                BeanUtils.copyProperties(book, bookResponseModel);
                return bookResponseModel;
            }).toList();
        } catch (Exception ex) {
            throw ex;
        }
        return bookResponseModels;
    }
}
