package com.amigoscode.group.ebankingsuite.transaction;

import com.amigoscode.group.ebankingsuite.account.*;
import com.amigoscode.group.ebankingsuite.exception.ValueMismatchException;
import com.amigoscode.group.ebankingsuite.transaction.request.FundsTransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountService accountService;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, accountService);
    }

    @Test
    void canSuccessfullyTransferFunds() {
        //given
        FundsTransferRequest request = new FundsTransferRequest("165568799", "986562737", new BigDecimal(200), "1234", "test transfer");
        Account senderAccount = new Account(1,new BigDecimal(200), AccountStatus.ACTIVATED,"986562737", Tier.LEVEL1,"$2a$10$j4ogRjGJWnPUrmdE82Mq5ueybC9SxGTCgQkvzzE7uSbYXoKqIMKxa");
        Account receiverAccount = new Account(1,new BigDecimal(0), AccountStatus.ACTIVATED,"165568799", Tier.LEVEL1,"$2a$10$j4ogRjGJWnPUrmdE82Mq5ueybC9SxGTCgQkvzzE7uSbYXoKqIMKxa");

        given(accountService.accountExistsAndIsActivated("986562737")).willReturn(senderAccount);
        given(accountService.accountExistsAndIsActivated("165568799")).willReturn(receiverAccount);
        //when
        transactionService.transferFunds(request);

        //then
        verify(accountService, times(1)).debitAccount(senderAccount, request.amount());
        verify(accountService, times(1)).creditAccount(receiverAccount, request.amount());
    }

    @Test
    void willThrowValueMismatch_TransferFunds_TransactionPinMismatch(){
        //given
        FundsTransferRequest request = new FundsTransferRequest("165568799", "986562737", new BigDecimal(200), "1224", "test transfer");
        Account senderAccount = new Account(1,new BigDecimal(200), AccountStatus.ACTIVATED,"986562737", Tier.LEVEL1,"$2a$10$j4ogRjGJWnPUrmdE82Mq5ueybC9SxGTCgQkvzzE7uSbYXoKqIMKxa");
        given(accountService.accountExistsAndIsActivated("986562737")).willReturn(senderAccount);

        //when
        //then
        assertThatThrownBy(()-> transactionService.transferFunds(request))
                .isInstanceOf(ValueMismatchException.class)
                .hasMessage("incorrect transaction pin");
    }

    @Test
    void willThrowIllegalArgumentException_TransferFunds_SenderAndReceiverAccountNumberIsTheSame(){
        //given
        FundsTransferRequest request = new FundsTransferRequest("165568799", "165568799", new BigDecimal(200), "1224", "test transfer");

        //when
        //then
        assertThatThrownBy(()-> transactionService.transferFunds(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("sender account cannot be recipient account");
    }
}