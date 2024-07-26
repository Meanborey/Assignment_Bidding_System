package com.example.assignment_bidding_system.security;

import com.example.assignment_bidding_system.entity.User;
import com.example.assignment_bidding_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("KANGCHI : loadUserByUsername(String username)");

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User has not been found!"));

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(user);

        log.info("KANGCHI => {}", customUserDetails.getUsername());
        log.info("KANGCHI => {}", customUserDetails.getAuthorities());

        return customUserDetails;
    }
}
