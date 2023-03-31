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

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final JWTService jwtService;

    public TransactionController(TransactionService transactionService, JWTService jwtService) {
        this.transactionService = transactionService;
        this.jwtService = jwtService;
    }

    @PostMapping("/send-funds")
    public ResponseEntity<ApiResponse> transferFunds(@RequestBody FundsTransferRequest request){

        try {
            transactionService.transferFunds(request);
            return new ResponseEntity<>(new ApiResponse("funds transferred"), HttpStatus.OK);
        }catch (ValueMismatchException | InsufficientBalanceException | IllegalArgumentException e){
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }catch (ResourceNotFoundException e){
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

}
