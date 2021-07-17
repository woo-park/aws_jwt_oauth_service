package com.awsjwtservice.repository;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.LoginProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    Account findByUsernameAndLoginProvider(String username, LoginProvider loginProvider);

    Optional<Account> findByEmailOrUsername(String email, String username);

    int countByUsername(String username);

    Optional<Account> findByEmail(String email);



    Account findOneById(long id);

    List<Account> findAll();

    List<Account> findAllByUsername(String username);
}