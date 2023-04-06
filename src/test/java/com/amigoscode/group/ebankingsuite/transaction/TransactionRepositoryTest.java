package com.amigoscode.group.ebankingsuite.transaction;

import static org.assertj.core.api.Java6Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @AfterEach
    void deleteDataInTable(){
        transactionRepository.deleteAll();
    }

    @Test
    void canCheckIfReferenceNumberExists(){
        //given
        Transaction newTransaction = new Transaction(
                "6765456456",
                "6765456434",
                new BigDecimal(50),
                "dvhbjh37878",
                "test transaction",
                TransactionStatus.SUCCESS
        );
        transactionRepository.save(newTransaction);
        //when
        boolean status = transactionRepository.existsByReferenceNum(newTransaction.getReferenceNum());
        //then
        assertThat(status).isTrue();

    }
}
