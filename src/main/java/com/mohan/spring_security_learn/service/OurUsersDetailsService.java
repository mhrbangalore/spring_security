package com.mohan.spring_security_learn.service;

import com.mohan.spring_security_learn.repository.OurUsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OurUsersDetailsService implements UserDetailsService {

    @Autowired
    private OurUsersRepo ourUsersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ourUsersRepo.findByEmail(username).orElseThrow();
    }
}
