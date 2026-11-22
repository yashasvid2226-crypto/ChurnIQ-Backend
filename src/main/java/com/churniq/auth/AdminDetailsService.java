package com.churniq.auth;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class AdminDetailsService implements UserDetailsService {
    private final AdminUserRepository repository;

    public AdminDetailsService(AdminUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        AdminUser admin = repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Administrator not found"));

        return User.withUsername(admin.getUsername())
            .password(admin.getPassword())
            .roles(admin.getRole())
            .build();
    }
}
