package com.lethanhbinh.borrowingservice.command.saga;

import com.lethanhbinh.borrowingservice.command.command.DeleteBorrowingCommand;
import com.lethanhbinh.borrowingservice.command.event.CreateBorrowingEvent;
import com.lethanhbinh.commonservice.command.UpdateStatusBookCommand;
import com.lethanhbinh.commonservice.event.UpdateStatusBookEvent;
import com.lethanhbinh.commonservice.model.BookResponseCommonModel;
import com.lethanhbinh.commonservice.query.GetBookDetailQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
public class BorrowingSaga {
    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    private void handle(CreateBorrowingEvent event) {
        log.info("Borrowing create event saga for bookId: {}", event.getBookId());

        try {
            GetBookDetailQuery getBookDetailQuery = new GetBookDetailQuery(event.getBookId());
            BookResponseCommonModel bookResponseCommonModel =
                    queryGateway.query(getBookDetailQuery, ResponseTypes.instanceOf(BookResponseCommonModel.class)).join();

            SagaLifecycle.associateWith("bookId", event.getBookId());
            UpdateStatusBookCommand command = new UpdateStatusBookCommand(event.getBookId(),
                    false, event.getEmployeeId(), event.getId());
            commandGateway.sendAndWait(command);

            if (!bookResponseCommonModel.getIsReady()) {
                throw new Exception("Book is already borrowed");
            }

        } catch (Exception e) {
            rollBackCreateBorrowing(event.getId());
            log.error(e.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "bookId")
    private void handle (UpdateStatusBookEvent event) {
        log.info("Book update status event Saga for bookId: {}", event.getBookId());

        SagaLifecycle.end();
    }

    private void rollBackCreateBorrowing (String borrowingId) {
        DeleteBorrowingCommand command = new DeleteBorrowingCommand(borrowingId);
        commandGateway.sendAndWait(command);
        SagaLifecycle.end();
    }
}
