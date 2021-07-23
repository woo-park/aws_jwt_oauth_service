package com.awsjwtservice.service;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.dto.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    void createUser(Account account);

    void modifyUser(AccountDto accountDto);

    List<Account> getUsers();

    AccountDto getUser(Long id);

    void deleteUser(Long idx);

    void order();

    void createUserIfNotFound(AccountDto accountDto);

    Optional<Account> getUser(String email);

    Account findUser(String email);

    void updateUser(Account account);

    List<Account> listAll();


}
