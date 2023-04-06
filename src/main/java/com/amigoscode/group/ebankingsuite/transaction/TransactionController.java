package com.amigoscode.group.ebankingsuite.transaction;

import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.exception.InsufficientBalanceException;
import com.amigoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import com.amigoscode.group.ebankingsuite.exception.ValueMismatchException;
import com.amigoscode.group.ebankingsuite.transaction.request.FundsTransferRequest;
import com.amigoscode.group.ebankingsuite.universal.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/send-funds")
    public ResponseEntity<ApiResponse> transferFunds(@RequestBody FundsTransferRequest request){

        try {
            System.out.println("begin");
            transactionService.transferFunds(request);
            return new ResponseEntity<>(new ApiResponse("funds transferred"), HttpStatus.OK);
        }catch (ValueMismatchException | InsufficientBalanceException | IllegalArgumentException e){
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * This controller fetches all transaction by status and date range of a particular account
     */
    @GetMapping("/{accountNumber}/transaction-history")
    public ResponseEntity<ApiResponse> getTransactionsByAccountNumber(@PathVariable String accountNumber,
            @RequestParam TransactionStatus status,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam Integer senderAccountNumber,
            @RequestParam Integer receiverAccountNumber) {
        try {
            return new ResponseEntity<>(new ApiResponse("transactions",
                    transactionService.getAllTransactionsByAccountNumber(status, startDate, endDate, senderAccountNumber, receiverAccountNumber)),
                    HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
