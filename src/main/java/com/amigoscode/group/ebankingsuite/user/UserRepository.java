package com.amigoscode.group.ebankingsuite.user;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmailAddress(String emailAddress);
    Optional<User> findByEmailAddress(String email);
}
