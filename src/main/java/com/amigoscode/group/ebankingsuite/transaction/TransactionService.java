package com.amigoscode.group.ebankingsuite.transaction;

import com.amigoscode.group.ebankingsuite.account.Account;
import com.amigoscode.group.ebankingsuite.account.AccountService;
import com.amigoscode.group.ebankingsuite.account.AccountStatus;
import com.amigoscode.group.ebankingsuite.exception.AccountNotActivatedException;
import com.amigoscode.group.ebankingsuite.exception.ValueMismatchException;
import com.amigoscode.group.ebankingsuite.transaction.request.FundsTransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();


    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    private boolean isAccountActivated(Account account){
        if(!account.getAccountStatus().equals(AccountStatus.ACTIVATED)){
            throw new AccountNotActivatedException("account not activated contact the customer support service");
        }
        return true;
    }
    @Transactional
    public void transferFunds(FundsTransferRequest request){
        if(!request.senderAccountId().equals(request.receiverAccountId())){
            Account senderAccount = accountService.getAccountById(request.senderAccountId());
            if(ENCODER.matches(request.transactionPin(),senderAccount.getTransactionPin())){
                if(isAccountActivated(senderAccount)) {
                    accountService.debitAccount(request.senderAccountId(), request.amount());
                    accountService.creditAccount(request.receiverAccountId(), request.amount());
                    saveNewTransaction(request);
                    return;
                }
            }
            throw new ValueMismatchException("invalid transaction pin");
        }
        throw new IllegalArgumentException("sender account cannot be recipient account");
    }

    /**
     * This method save a new transaction after completion, it is an asynchronous process
     * because the method using it doesn't need it response
     */
    @Async
    public void saveNewTransaction(FundsTransferRequest request){
        transactionRepository.save(
                new Transaction(request.senderAccountId(),
                        request.receiverAccountId(),
                        generateTransactionReference(),
                        request.narration(),
                        TransactionStatus.SUCCESS
                        )
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
        }while (!transactionRepository.existsByReferenceNum(builder.toString()));
        return builder.toString();
    }


}
