package com.awsjwtservice.repository;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.LoginProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    Account findByUsernameAndLoginProvider(String username, LoginProvider loginProvider);

    int countByUsername(String username);

    Optional<Account> findByEmail(String email);
}