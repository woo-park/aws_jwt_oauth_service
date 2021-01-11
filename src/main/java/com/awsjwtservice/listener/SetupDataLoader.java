package com.awsjwtservice.listener;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        setupSecurityResources();
//
//        setupAccessIpData();

        alreadySetup = true;
    }


    private void setupSecurityResources() {
//        Set<Role> roles = new HashSet<>();
//        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
//        roles.add(adminRole);
//        createResourceIfNotFound("/admin/**", "", roles, "url");
//        createResourceIfNotFound("execution(public * io.security.corespringsecurity.aopsecurity.*Service.pointcut*(..))", "", roles, "pointcut");
        createUserIfNotFound("admin", "admin@admin.com", "1111", "ROLE_ADMIN");
        createUserIfNotFound("manager", "manager@manager.com", "1111", "ROLE_MANAGER");
        createUserIfNotFound("test", "user@user.com", "1111", "ROLE_USER");

//        Role managerRole = createRoleIfNotFound("ROLE_MANAGER", "매니저권한");
//        Role userRole = createRoleIfNotFound("ROLE_USER", "사용자권한");
//        createRoleHierarchyIfNotFound(managerRole, adminRole);
//        createRoleHierarchyIfNotFound(userRole, managerRole);
    }



    @Transactional
    public Account createUserIfNotFound(final String userName, final String email, final String password, final String role) {

        Account account = userRepository.findByUsername(userName);

        if (account == null) {
            account = Account.builder()
                    .username(userName)
                    .email(email)
//                        .password()
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .build();
        }
        return userRepository.save(account);
    }

}