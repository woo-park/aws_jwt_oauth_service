/**
 * 
 */
package com.awsjwtservice.config.security;

import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.awsjwtservice.config.formlogin.AccountContext;
import com.awsjwtservice.domain.Account;
import com.awsjwtservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



@Component
public class JwtAuthenticationService {
	
    private static final String SECRETKEY = Base64.getEncoder().encodeToString("ProdosSecretZKey".getBytes());;
    
    private static final String PREFIX = "Bearer";

	private static final String EMPTY = "";
    
    private static final long EXPIRATIONTIME = 86400000; //1 day in milliseconds 

	private static final String AUTHORIZATION = "Authorization";

    @Autowired
    private UserRepository userRepository;


    // OAuth 를 위한 UserDetailServiceImpl 의 loadUserByUsername 을 사용하면 안되기때문에.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = userRepository.findByUsername(username);
        if (account == null) {
            if (userRepository.countByUsername(username) == 0) {
                throw new UsernameNotFoundException("No user found with username: " + username);
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

        return new AccountContext(account, roles);
    }

    private final HttpSession httpSession;

    public JwtAuthenticationService(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public String createToken(String username, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATIONTIME);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, SECRETKEY)
            .compact();
    }

    public Authentication getAuthentication(HttpServletRequest request) {
    	String token = resolveToken(request);
    	if(token != null && validateToken(token)) {
    		String username = getUsername(token);
    		if(username != null) {
    			UserDetails userDetails = loadUserByUsername(username);
    			return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    		}
    	}
        return null;
    }

    private String getUsername(String token) {
        return Jwts.parser()
        		.setSigningKey(SECRETKEY)
        		.parseClaimsJws(token)
        		.getBody().getSubject();
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        Cookie cookie[] = request.getCookies();

//        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");
//        String teststr = (String) httpSession.getAttribute("test");

        if( cookie != null && cookie.length > 0) {


            for(Cookie cook : cookie) {
                if(cook.getName().equals("Jwt")) {
                    System.out.println("request.getHeader(AUTHORIZATION) null");
                    if(bearerToken == null) {
                        bearerToken = cook.getValue();
                    }
                }
            }


        }


        if (bearerToken != null && bearerToken.startsWith(PREFIX)) {
            return bearerToken.replace(PREFIX, EMPTY).trim();
        }

        return null;
    }

    private boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Expired or invalid JWT token");
        }
    }
}
