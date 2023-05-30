package com.amigoscode.group.ebankingsuite.account;

import com.amigoscode.group.ebankingsuite.account.request.AccountTransactionPinUpdateModel;
import com.amigoscode.group.ebankingsuite.account.response.AccountOverviewResponse;
import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.exception.AccountNotClearedException;
import com.amigoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import com.amigoscode.group.ebankingsuite.universal.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final JWTService jwtService;

    @Autowired
    public AccountController(AccountService accountService, JWTService jwtService) {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    /**
     * This controller fetches the user account overview by getting the userId from the JWT token
     */
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse> getUserAccountOverview(
                    @RequestHeader("Authorization") String jwt) {

    AccountOverviewResponse response = accountService.generateAccountOverviewByUserId(jwtService.extractUserIdFromToken(jwt));
    return new ResponseEntity<>(new ApiResponse("user account overview", response),HttpStatus.OK);

    }

    /**
     * This controller allows user to close their account by getting the userId from the JWT and the relieving reason
     * from the request body
     */
    @DeleteMapping()
    public ResponseEntity<ApiResponse> closeAccount(
            @RequestHeader("Authorization") String jwt) {
            accountService.closeAccount(jwtService.extractUserIdFromToken(jwt));
            return new ResponseEntity<>(new ApiResponse("account closed successfully"), HttpStatus.OK);
    }
    @PutMapping("/transaction-pin")
    public ResponseEntity<ApiResponse> updateAccountTransactionPin(
            @RequestHeader("Authorization") String jwt, @RequestBody @Validated AccountTransactionPinUpdateModel pinUpdateModel) {

            accountService.updateAccountTransactionPin(jwtService.extractUserIdFromToken(jwt),pinUpdateModel);
            return new ResponseEntity<>(new ApiResponse("transaction pin set"), HttpStatus.OK);
    }

}