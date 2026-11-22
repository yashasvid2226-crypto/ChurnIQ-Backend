package com.churniq.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager manager;
    private final SecurityContextRepository contextRepository;

    public AuthController(
            AuthenticationManager manager,
            SecurityContextRepository contextRepository) {
        this.manager = manager;
        this.contextRepository = contextRepository;
    }

    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken csrfToken) {
        return Map.of("token", csrfToken.getToken());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> body,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            String username = body.get("username");
            String password = body.get("password");

            if (username == null || password == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("message", "Username and password are required."));
            }

            Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContext context =
                SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            contextRepository.saveContext(context, request, response);

            return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "role", "ADMIN"
            ));

        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(401)
                .body(Map.of("message", "Invalid username or password."));
        }
    }

    @GetMapping("/me")
    public Map<String, String> me(Authentication authentication) {
        return Map.of(
            "username", authentication.getName(),
            "role", "ADMIN"
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        new SecurityContextLogoutHandler().logout(
            request, response, SecurityContextHolder.getContext().getAuthentication()
        );

        return ResponseEntity.ok(
            Map.of("message", "Logged out successfully.")
        );
    }
}
