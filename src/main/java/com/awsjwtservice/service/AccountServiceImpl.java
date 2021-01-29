package com.awsjwtservice.service;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.repository.UserRepository;
import com.awsjwtservice.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void createUser(Account account){

//        Role role = roleRepository.findByRoleName("ROLE_USER");
//        Set<Role> roles = new HashSet<>();
//        roles.add(role);
//        account.setUserRoles(roles);
        userRepository.save(account);
    }


    @Transactional
    public void createUserIfNotFound(AccountDto accountDto) {


        Account account = userRepository.findByUsername(accountDto.getUsername());

        if (account == null) {
//            방법 1
            account = Account.builder()
                    .username(accountDto.getUsername())
                    .email(accountDto.getEmail())
//                        .password()
                    .password(passwordEncoder.encode(accountDto.getPassword()))
                    .role(accountDto.getRole())
                    .build();

            //방법 2
//            ModelMapper modelMapper = new ModelMapper();
//            Account account2 = modelMapper.map(accountDto, Account.class);
//
//            account.setPassword(passwordEncoder.encode(account.getPassword()));

        }
        userRepository.save(account);
    }

    @Transactional
    @Override
    public void modifyUser(AccountDto accountDto){

        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDto, Account.class);

//        if(accountDto.getRoles() != null){
//            Set<Role> roles = new HashSet<>();
//            accountDto.getRoles().forEach(role -> {
//                Role r = roleRepository.findByRoleName(role);
//                roles.add(r);
//            });
//            account.setUserRoles(roles);
//        }
//        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        userRepository.save(account);

    }

    @Transactional
    public AccountDto getUser(Long id) {

        Account account = userRepository.findById(id).orElse(new Account());
        ModelMapper modelMapper = new ModelMapper();
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

////        List<String> roles = account.getUserRoles()
////                .stream()
////                .map(role -> role.getRoleName())
////                .collect(Collectors.toList());
//
//        accountDto.setRoles(roles);
        return accountDto;
    }

    @Transactional
    public List<Account> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Secured("ROLE_MANAGER")
    public void order() {
        System.out.println("order");
    }
}