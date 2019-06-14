package com.example.ademo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomService implements UserDetailsService {
    @Autowired
    AccountRepo accountRepo;

   /* @Autowired
    PasswordEncoder encoder;*/

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return accountRepo.findByEmail(s)
                .map(account -> new User(
                        account.getEmail(),
                        /*encoder.encode(*/account.getPassword()/*)*/,
                        true,
                        true,
                        true,
                        true,
                        AuthorityUtils.createAuthorityList("read", "write"))
                ).orElseThrow(() -> new UsernameNotFoundException("OOPS"));
    }
}
