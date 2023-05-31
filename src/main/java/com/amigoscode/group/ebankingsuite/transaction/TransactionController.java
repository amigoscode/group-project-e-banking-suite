package com.amigoscode.group.ebankingsuite.transaction;

import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.transaction.request.FundsTransferRequest;
import com.amigoscode.group.ebankingsuite.transaction.request.TransactionHistoryRequest;
import com.amigoscode.group.ebankingsuite.universal.ApiResponse;
import com.itextpdf.text.DocumentException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;

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
    public ResponseEntity<ApiResponse> transferFunds(@RequestHeader("Authorization") String jwt,
                @RequestBody @Validated FundsTransferRequest request){

        transactionService.transferFunds(request,jwtService.extractUserIdFromToken(jwt));
        return new ResponseEntity<>(new ApiResponse("funds transferred"), HttpStatus.OK);
    }

    /**
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
     * This controller generates monthly or yearly account statement in pdf format
     */
    @PostMapping(value = "/account-statement", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateTransactionStatement(
            @RequestHeader("Authorization") String jwt,
            @RequestBody TransactionHistoryRequest request) throws DocumentException {

        int userId = jwtService.extractUserIdFromToken(jwt);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.builder("inline")
                        .filename("transaction_report.pdf")
                        .build()
        );

        ByteArrayOutputStream accountStatement = transactionService.generateTransactionStatement(request,userId);
        return new ResponseEntity<>(accountStatement.toByteArray(), headers, HttpStatus.OK);
    }
}
