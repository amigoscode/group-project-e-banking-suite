package com.amigoscode.group.ebankingsuite.transaction;

import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.transaction.request.FundsTransferRequest;
import com.amigoscode.group.ebankingsuite.transaction.request.TransactionHistoryRequest;
import com.amigoscode.group.ebankingsuite.universal.ApiResponse;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<ApiResponse> transferFunds(@RequestBody @Validated FundsTransferRequest request){

        transactionService.transferFunds(request);
        return new ResponseEntity<>(new ApiResponse("funds transferred"), HttpStatus.OK);
    }

    /**
<<<<<<< HEAD
     * This controller fetches all transaction based on a range of date and also implements pagination to help reduce load time
     */
    @PostMapping()
    public ResponseEntity<ApiResponse> generateTransactionHistory(@RequestHeader("Authorization") String jwt,
                                                                  @RequestParam int size,
                                                                  @RequestParam int page,
                                                                  @RequestBody @Validated TransactionHistoryRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(new ApiResponse("transaction history",
                    transactionService.getTransactionHistoryByUserId(request,jwtService.extractUserIdFromToken(jwt),pageable)),
                    HttpStatus.OK);
    }

    /**
     * This controller generates a pdf statement for montly or yearly transactions of a particular account
     */
    @SneakyThrows
    @GetMapping("/statement/{accountNumber}")
    public ResponseEntity<byte[]> accountTransactionStatement(
            @RequestParam String accountNumber,
            @RequestParam(required = true) int year,
            @RequestParam(required = false, defaultValue = "0") int month) {

        byte[] statementBytes = transactionService.generateAccountStatement(accountNumber, year, month);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "statement.pdf");

        return new ResponseEntity<>(statementBytes, headers, HttpStatus.OK);
    }
}
