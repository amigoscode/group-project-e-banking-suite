package com.amigoscode.group.ebankingsuite.transaction;

import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.transaction.request.FundsTransferRequest;
import com.amigoscode.group.ebankingsuite.universal.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;

import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class  TransactionControllerTest {

    private TransactionController transactionController;
    @Mock
    private TransactionService transactionService;
    @Mock
    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        this.transactionController = new TransactionController(transactionService, jwtService);
    }

    @Test
    void transferFunds_successful() {
        //given
        FundsTransferRequest fundsTransferRequest = new FundsTransferRequest("165568799", "986562737", new BigDecimal(200), "1224", "test transfer");

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        //when
        ResponseEntity<ApiResponse> responseEntity = transactionController.transferFunds(fundsTransferRequest);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}