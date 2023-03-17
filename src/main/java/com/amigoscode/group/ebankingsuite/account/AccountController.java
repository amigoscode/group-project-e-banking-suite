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
     *
     */
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse> getUserAccountOverview(
                    @RequestHeader("Authorization") String jwt) {
        try {
            AccountOverviewResponse response = accountService.generateAccountOverviewByUserId(
                    jwtService.extractUserIdFromToken(jwt));
            return new ResponseEntity<>(new ApiResponse("user account overview", response),
                    HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }

    }

    /**
     * This controller allows user to close their account by getting the userId from the JWT and the relieving reason
     * from the request body
     */
    @PostMapping("/close")
    public ResponseEntity<ApiResponse> closeAccount(
            @RequestHeader("Authorization") String jwt) {
        try {
            accountService.closeAccount(jwtService.extractUserIdFromToken(jwt));

            return new ResponseEntity<>(new ApiResponse("account closed successfully"), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (AccountNotClearedException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @PostMapping("/update/pin")
    public ResponseEntity<ApiResponse> updateAccountTransactionPin(
            @RequestHeader("Authorization") String jwt,
            @RequestBody AccountTransactionPinUpdateModel pinUpdateModel) {
        try {
            accountService.updateAccountTransactionPin(
                    jwtService.extractUserIdFromToken(jwt),pinUpdateModel);
            return new ResponseEntity<>(new ApiResponse("account closed successfully"), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
        }
    }

/*    @PostMapping("/profile")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findAccountById(@PathVariable("id") Integer accountId) {
        Account account = accountService.findAccountById(accountId);
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable("id") Integer accountId, @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(accountId, account);
        if (updatedAccount != null) {
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Integer accountId) {
        accountService.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/

}