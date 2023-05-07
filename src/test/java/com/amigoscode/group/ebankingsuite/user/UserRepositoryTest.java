package com.amigoscode.group.ebankingsuite.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest{

    @Autowired
    private UserRepository userRepository;
    @AfterEach
    void deleteDataInTable(){
        userRepository.deleteAll();
    }

    @Test
    void canCheckIfUserExistsByEmailAddress(){
        //given
        String email = "ebanking@gmail.com";
        User testUser = new User(
                "test pater",
                "ebanking@gmail.com",
                "1234",
                true,
                "+23478768990"
        );
        userRepository.save(testUser);

        //when
        boolean expected = userRepository.existsByEmailAddress(email);

        //then
        assertThat(expected).isTrue();
    }

    @Test
    void canReturnOptionalOfUserByEmailAddress(){
        //given
        String emailAddress ="ebanking@gmail.com";
        User testUser = new User(
                "test pater",
                "ebanking@gmail.com",
                "1234",
                true,
                "+2348709090"
        );

        userRepository.save(testUser);

        //when
        Optional<User> expectedUser = userRepository.findByEmailAddress(emailAddress);

        //then
        assertThat(expectedUser).isEqualTo(Optional.of(testUser));
    }
}