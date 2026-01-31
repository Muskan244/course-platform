package com.assignment.course_platform.service;

import com.assignment.course_platform.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).map(userEntity ->
                User.builder()
                        .username(email)
                        .password(userEntity.getPassword())
                        .build()
        ).orElseThrow(() -> new UsernameNotFoundException(
                "Email [%s] not found".formatted(email)
        ));
    }
}
