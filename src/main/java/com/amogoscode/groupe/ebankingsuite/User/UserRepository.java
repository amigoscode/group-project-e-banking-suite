package com.amogoscode.groupe.ebankingsuite.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    boolean existsByEmailAddress(String emailAddress);
    Optional<User> findByEmailAddress(String email);
}
