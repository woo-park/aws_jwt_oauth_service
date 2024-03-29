package com.awsjwtservice.service;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.LoginProvider;
import com.awsjwtservice.repository.UserRepository;
import com.awsjwtservice.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        Optional<Account> accounts = userRepository.findByEmailOrUsername(accountDto.getEmail(), accountDto.getUsername());
//        Account account = userRepository.findByUsername(accountDto.getUsername());

//        if (account == null) {
        if(accounts.isPresent()){
            // must throw an error message to client
        } else {

            // 방법 1
            Account account = Account.builder()
                    .username(accountDto.getUsername())
                    .email(accountDto.getEmail())
                    .password(passwordEncoder.encode(accountDto.getPassword()))
                    .role(accountDto.getRole())
                    .loginProvider(LoginProvider.DEFAULT)
                    .build();

            //방법 2
            // ModelMapper modelMapper = new ModelMapper();
            // Account account2 = modelMapper.map(accountDto, Account.class);
            // account.setPassword(passwordEncoder.encode(account.getPassword()));

            userRepository.save(account);
        }
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

    @Transactional
    public Optional<Account> getUser(String usernameOrEmail) {
        return userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail);
    }

    @Transactional
    public Account findUser(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    @Override
    public void updateUser(Account account) {
        userRepository.save(account);
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

    public List<Account> listAll() {
        return userRepository.findAll(Sort.by("email").ascending());
    }


// use this later to validate -> good reference
//    private void validateDuplicateMember(Member member) {
//        List<Member> findMembers = memberRepository.findByName(member.getName());
//        if (!findMembers.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        }
//    }
}