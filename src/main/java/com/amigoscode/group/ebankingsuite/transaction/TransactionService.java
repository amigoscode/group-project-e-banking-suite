package com.amigoscode.group.ebankingsuite.transaction;

import com.amigoscode.group.ebankingsuite.account.Account;
import com.amigoscode.group.ebankingsuite.account.AccountService;
import com.amigoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import com.amigoscode.group.ebankingsuite.exception.ValueMismatchException;
import com.amigoscode.group.ebankingsuite.transaction.request.FundsTransferRequest;
import com.amigoscode.group.ebankingsuite.transaction.request.TransactionHistoryRequest;
import com.amigoscode.group.ebankingsuite.transaction.response.TransactionHistoryResponse;
import com.amigoscode.group.ebankingsuite.transaction.response.TransactionType;
import com.amigoscode.group.ebankingsuite.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final UserService userService;

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();


    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.userService = userService;
    }


    @Transactional
    public void transferFunds(FundsTransferRequest request){
        if(!request.senderAccountNumber().equals(request.receiverAccountNumber())){
            Account senderAccount = accountService.accountExistsAndIsActivated(request.senderAccountNumber());
            if(ENCODER.matches(request.transactionPin(), senderAccount.getTransactionPin())) {
                Account receiverAccount = accountService.accountExistsAndIsActivated(request.receiverAccountNumber());
                accountService.debitAccount(senderAccount, request.amount());
                accountService.creditAccount(receiverAccount, request.amount());
                saveNewTransaction(request, senderAccount, receiverAccount);
                return;
            }
            throw new ValueMismatchException("incorrect transaction pin");
        }
        throw new IllegalArgumentException("sender account cannot be recipient account");
    }

    /**
     * This method save a new transaction after completion, it is an asynchronous process
     * because the method using it doesn't need it response
     */
    @Async
    public void saveNewTransaction(FundsTransferRequest request, Account senderAccount, Account receiverAccount){

        transactionRepository.save(
                new Transaction(request.senderAccountNumber(),
                        request.receiverAccountNumber(),
                        request.amount(),
                        generateTransactionReference(),
                        request.narration(),
                        TransactionStatus.SUCCESS,
                        userService.getUserByUserId(senderAccount.getUserId()).getFullName(),
                        userService.getUserByUserId(receiverAccount.getUserId()).getFullName())
        );
    }

    /**
     * generates random reference number it keeps generating until it gets a unique value.
     */
    private String generateTransactionReference(){
        final String VALUES = "abcdefghijklmnopqrstuvwxyz0123456789";
        final int referenceNumberLength = 12;
        StringBuilder builder = new StringBuilder(referenceNumberLength);
        do {
            for (int i = 0; i < referenceNumberLength; i++) {
                builder.append(VALUES.charAt(SECURE_RANDOM.nextInt(VALUES.length())));
            }
        }while (transactionRepository.existsByReferenceNum(builder.toString()));
        return builder.toString();
    }

    /**
     * This method returns a list of transactions for a particular account by userId
     */
    public List<TransactionHistoryResponse> getTransactionHistoryByUserId(TransactionHistoryRequest request, int userId, Pageable pageable) {
        Account userAccount = accountService.getAccountByUserId(userId);
        Slice<Transaction> transactions = transactionRepository.findAllByStatusAndCreatedAtBetweenAndSenderAccountNumberOrReceiverAccountNumber(
                TransactionStatus.SUCCESS,
                request.startDateTime(),
                request.endDateTime(),
                userAccount.getAccountNumber(),
                userAccount.getAccountNumber(),
                pageable
        );
        if(transactions.getContent().isEmpty()){
            throw new ResourceNotFoundException("no transactions");
        }

        return formatTransactions(transactions.getContent(), userAccount.getAccountNumber());

    }

    /**
     * This method formats the transactions into the desired format which classifies each transaction into either credit and debit for easier understanding.
     */
    public List<TransactionHistoryResponse> formatTransactions(List <Transaction> transactions, String userAccountNumber){

        List<TransactionHistoryResponse> transactionHistoryResponses = new ArrayList<>();

        transactions.forEach(
                    transaction -> {
                        TransactionHistoryResponse transactionHistoryResponse = new TransactionHistoryResponse();
                        transactionHistoryResponse.setTransactionDateTime(transaction.getCreatedAt());
                        transactionHistoryResponse.setAmount(transaction.getAmount());
                        transactionHistoryResponse.setReceiverName(transaction.getReceiverName());
                        transactionHistoryResponse.setSenderName(transaction.getSenderName());
                        transactionHistoryResponse.setTransactionType(checkTransactionType(transaction, userAccountNumber));
                        transactionHistoryResponses.add(transactionHistoryResponse);
                    }
        );

        return transactionHistoryResponses;
    }

    public TransactionType checkTransactionType(Transaction transaction, String userAccountNumber){

        if(transaction.getReceiverAccountNumber().equals(userAccountNumber)){
            return TransactionType.CREDIT;
        }else if(transaction.getSenderAccountNumber().equals(userAccountNumber)){
            return TransactionType.DEBIT;
        }
           throw new IllegalArgumentException("error processing cannot determine transaction type");
    }
}
