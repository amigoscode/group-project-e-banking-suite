package com.amigoscode.group.ebankingsuite.transaction;

import com.amigoscode.group.ebankingsuite.account.*;
import com.amigoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import com.amigoscode.group.ebankingsuite.exception.ValueMismatchException;
import com.amigoscode.group.ebankingsuite.notification.NotificationSenderService;
import com.amigoscode.group.ebankingsuite.notification.emailNotification.EmailSenderService;
import com.amigoscode.group.ebankingsuite.transaction.request.FundsTransferRequest;
import com.amigoscode.group.ebankingsuite.transaction.request.TransactionHistoryRequest;
import com.amigoscode.group.ebankingsuite.transaction.response.TransactionHistoryResponse;
import com.amigoscode.group.ebankingsuite.transaction.response.TransactionType;
import com.amigoscode.group.ebankingsuite.user.User;
import com.amigoscode.group.ebankingsuite.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private UserService userService;
    @Mock
    private EmailSenderService emailNotificationService;
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionRepository, accountService, userService, (NotificationSenderService) emailNotificationService);
    }

    @Test
    void canSuccessfullyTransferFunds() {
        //given
        FundsTransferRequest request = new FundsTransferRequest("165568799", "986562737", new BigDecimal(200), "1234", "test transfer");

        Account senderAccount = new Account(1,new BigDecimal(200), AccountStatus.ACTIVATED,"986562737", Tier.LEVEL1,"$2a$10$j4ogRjGJWnPUrmdE82Mq5ueybC9SxGTCgQkvzzE7uSbYXoKqIMKxa");
        Account receiverAccount = new Account(2,new BigDecimal(0), AccountStatus.ACTIVATED,"165568799", Tier.LEVEL1,"$2a$10$j4ogRjGJWnPUrmdE82Mq5ueybC9SxGTCgQkvzzE7uSbYXoKqIMKxa");

        User senderUser = new User(1, "test Name 1", "test1@mail.com", userService.encodePassword("12345"), true);
        User reveiverUser = new User(2, "test Name 2", "test2@mail.com", userService.encodePassword("12345"), true);

        given(accountService.accountExistsAndIsActivated("986562737")).willReturn(senderAccount);
        given(accountService.accountExistsAndIsActivated("165568799")).willReturn(receiverAccount);
        given(userService.getUserByUserId(1)).willReturn(senderUser);
        given(userService.getUserByUserId(2)).willReturn(reveiverUser);

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
    void will_Throw_IllegalArgumentException_For_TransferFunds_When_SenderAndReceiverAccountNumberIsTheSame(){
        //given
        FundsTransferRequest request = new FundsTransferRequest("165568799", "165568799", new BigDecimal(200), "1224", "test transfer");

        //when
        //then
        assertThatThrownBy(()-> transactionService.transferFunds(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("sender account cannot be recipient account");
    }

    void can_generate_transaction_history_when_all_params_are_valid(){
        //given
        Account senderAccount = new Account(1,new BigDecimal(200), AccountStatus.ACTIVATED,"986562737", Tier.LEVEL1,"$2a$10$j4ogRjGJWnPUrmdE82Mq5ueybC9SxGTCgQkvzzE7uSbYXoKqIMKxa");

    }
    @Test
    void can_format_transaction_history_when_all_params_are_valid(){
        //given
        List<Transaction> transactions =
                List.of(new Transaction("878676790", "765362789", new BigDecimal("500"), "testRefNum", "testTransaction", TransactionStatus.SUCCESS, "testSender", "testReceiver"));

        List<TransactionHistoryResponse> expectedOutput =
                List.of(new TransactionHistoryResponse(transactions.get(0).getCreatedAt(),
                        transactions.get(0).getAmount(),
                        TransactionType.DEBIT,
                        transactions.get(0).getSenderName(),
                        transactions.get(0).getReceiverName()));

        //when
        List<TransactionHistoryResponse> output = transactionService.formatTransactions(transactions, "878676790");

        //then
        assertThat(output).usingFieldByFieldElementComparator().
                isEqualTo(expectedOutput);
    }

    @Test
    void can_confirm_transaction_type_is_Credit(){
        //given
        Transaction transaction = new Transaction("878676790", "765362789", new BigDecimal("500"), "testRefNum", "testTransaction", TransactionStatus.SUCCESS, "testSender", "testReceiver");
        String userAccountNumber = "765362789";

        //when
        TransactionType actualOutput = transactionService.checkTransactionType(transaction, userAccountNumber);

        //then
        assertThat(actualOutput).isEqualTo(TransactionType.CREDIT);
    }

    @Test
    void can_Confirm_Transaction_Type_Is_Debit(){
        //given
        Transaction transaction = new Transaction("878676790", "765362789", new BigDecimal("500"), "testRefNum", "testTransaction", TransactionStatus.SUCCESS, "testSender", "testReceiver");
        String userAccountNumber = "878676790";

        //when
        TransactionType actualOutput = transactionService.checkTransactionType(transaction, userAccountNumber);

        //then
        assertThat(actualOutput).isEqualTo(TransactionType.DEBIT);
    }

    @Test
    void will_throw_IllegalArgumentExceptionWhen_Transaction_Type_Cannot_Be_Determined(){
        //given
        Transaction transaction = new Transaction("878676790", "765362789", new BigDecimal("500"), "testRefNum", "testTransaction", TransactionStatus.SUCCESS, "testSender", "testReceiver");
        String userAccountNumber = "878676793";

        //when
        //then
        assertThatThrownBy(()-> transactionService.checkTransactionType(transaction, userAccountNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("error processing cannot determine transaction type");
    }

    @Test
    void can_Generate_Transaction_History_Between_Dates_by_userId(){
        //given
        int userId = 1;
        Account userAccount = new Account(1,new BigDecimal(200), AccountStatus.ACTIVATED,"986562737", Tier.LEVEL1,"$2a$10$j4ogRjGJWnPUrmdE82Mq5ueybC9SxGTCgQkvzzE7uSbYXoKqIMKxa");
        Slice<Transaction> transactions =
                new SliceImpl<>(List.of(new Transaction("986562737", "765362789", new BigDecimal("500"), "testRefNum", "testTransaction", TransactionStatus.SUCCESS, "testSender", "testReceiver")),
                        Pageable.unpaged(), false);

        TransactionHistoryRequest request = new TransactionHistoryRequest(LocalDateTime.now(),LocalDateTime.now().plusHours(2L));

        given(accountService.getAccountByUserId(userId)).willReturn(userAccount);
        given(transactionRepository.findAllByStatusAndCreatedAtBetweenAndSenderAccountNumberOrReceiverAccountNumber(
                TransactionStatus.SUCCESS,
                request.startDateTime(),
                request.endDateTime(),
                "986562737",
                "986562737",
                PageRequest.of(1,1)
        )).willReturn((Page<Transaction>) transactions);

        //when
        //then
        assertThat(transactionService.getTransactionHistoryByUserId(request, userId, PageRequest.of(1,1)).get(0))
                .isInstanceOf(TransactionHistoryResponse.class);

    }

    @Test
    void can_Throw_ResourceNotFoundException_When_Transaction_Is_Empty_When_Generating_Transaction_History_Between_Dates_by_userId(){
        //given
        int userId = 1;
        Account userAccount = new Account(1,new BigDecimal(200), AccountStatus.ACTIVATED,"986562737", Tier.LEVEL1,"$2a$10$j4ogRjGJWnPUrmdE82Mq5ueybC9SxGTCgQkvzzE7uSbYXoKqIMKxa");
        Slice<Transaction> transactions = new SliceImpl<>(List.of(), Pageable.unpaged(), false);

        TransactionHistoryRequest request = new TransactionHistoryRequest(LocalDateTime.now(),LocalDateTime.now().plusHours(2L));

        given(accountService.getAccountByUserId(userId)).willReturn(userAccount);
        given(transactionRepository.findAllByStatusAndCreatedAtBetweenAndSenderAccountNumberOrReceiverAccountNumber(
                TransactionStatus.SUCCESS,
                request.startDateTime(),
                request.endDateTime(),
                "986562737",
                "986562737",
                PageRequest.of(1,1)
        )).willReturn((Page<Transaction>) transactions);

        //when
        //then
        assertThatThrownBy(()->transactionService.getTransactionHistoryByUserId(request, userId, PageRequest.of(1,1)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("no transactions");

    }
}