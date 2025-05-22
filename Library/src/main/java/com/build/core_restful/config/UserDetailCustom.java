package com.build.core_restful.config;

import com.build.core_restful.service.UserService;
import com.build.core_restful.util.exception.NewException;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {
    private final UserService userService;

    public UserDetailCustom(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.build.core_restful.domain.User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new NewException("User not found with email: " + email);
        }

        return new User(
                user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
    }
}
