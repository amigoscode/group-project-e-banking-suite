package com.amigoscode.group.ebankingsuite.transaction;

import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.exception.InsufficientBalanceException;
import com.amigoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import com.amigoscode.group.ebankingsuite.exception.ValueMismatchException;
import com.amigoscode.group.ebankingsuite.transaction.request.FundsTransferRequest;
import com.amigoscode.group.ebankingsuite.transaction.request.TransactionHistoryRequest;
import com.amigoscode.group.ebankingsuite.universal.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        transactionService.transferFunds(request);
        return new ResponseEntity<>(new ApiResponse("funds transferred"), HttpStatus.OK);
    }

    /**
     * This controller fetches all transaction based on a range of date and also implements pagination to help reduce load time
     */
    @PostMapping()
    public ResponseEntity<ApiResponse> generateTransactionHistory(@RequestHeader("Authorization") String jwt,
                                                                  @RequestParam int size,
                                                                  @RequestParam int page,
                                                                  @RequestBody TransactionHistoryRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(new ApiResponse("transaction history",
                    transactionService.getTransactionHistoryByUserId(request,jwtService.extractUserIdFromToken(jwt),pageable)),
                    HttpStatus.OK);
    }
}
