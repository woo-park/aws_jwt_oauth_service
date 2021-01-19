package com.awsjwtservice.config.formlogin;

//import com.awswebservice.config.auth.security.service.AccountContext;
import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class FormAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public FormAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String loginId = authentication.getName();
        String password = (String) authentication.getCredentials();


        //UserDetailsServiceImpl의 loadUserByUsername(String username)


        Account account = userRepository.findByUsername(loginId);
        if (account == null) {
            if (userRepository.countByUsername(loginId) == 0) {
                throw new UsernameNotFoundException("No user found with username: " + loginId);
            }
        }

//        roles가 entity가 아닌 String 이기때문에
//        Set<String> userRoles = account.getUserRoles()
//                .stream()
//                .map(userRole -> userRole.getRoleName())
//                .collect(Collectors.toSet());
//        List<GrantedAuthority> collect = userRoles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(account.getRole()));

//        return new AccountContext(account, roles);
        AccountContext accountContext = new AccountContext(account, roles);
//        AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(loginId);


        if (!passwordEncoder.matches(password, accountContext.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

//        String secretKey = ((FormWebAuthenticationDetails) authentication.getDetails()).getSecretKey();
//        if (secretKey == null || !secretKey.equals("secret")) {
//            throw new IllegalArgumentException("Invalid Secret");
//        }


        // 주의 해야할점은 OAuth로 인증한 유저들은 db에 password 가 null 이다. 그러므로 accountContext 에서의 getPassword 도 null 값을 가져온다.
        // quick fix 는 null 을 넘기면 되겠지만. 그렇면 다음에 인증할때 문제가 된다.
        return new UsernamePasswordAuthenticationToken(accountContext.getAccount(), accountContext.getPassword(), accountContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
