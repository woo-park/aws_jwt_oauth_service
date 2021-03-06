package com.awsjwtservice.config.service;//package com.awswebservice.service;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
//@Service("userDetailsService")
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = userRepository.findByUsername(username);
        if (account == null) {
            if (userRepository.countByUsername(username) == 0) {
                throw new UsernameNotFoundException("No user found with username: " + username);
            }
        }

        // AccountContext로 passon or 3rd argument로 passon
//        List<GrantedAuthority> roles = new ArrayList<>();
//        roles.add(new SimpleGrantedAuthority(account.getRole()));

        // AccountContext를 user extends해서 따로 만들수있지만 여기선 바로 진행하겠다.
        String password = null;
        if(account.getPassword() == null) {
            password = "";
        } else {
            password = account.getPassword();
        }
        UserDetails user = new User(username, password, AuthorityUtils.createAuthorityList(account.getRole()));
        return user;
    }

}