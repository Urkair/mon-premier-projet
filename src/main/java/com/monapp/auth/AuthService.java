package com.monapp.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Transactional
    public Utilisateur create(Utilisateur user) {
        user.password = new String(Base64.getEncoder().encode(user.password.getBytes()), Charset.defaultCharset());
        Utilisateur.persist(user);
        return user;
    }

    public Utilisateur findById(String username) {
        return Utilisateur.findById(username);
    }

    @Transactional
    public Utilisateur modify(String username, String password, Set<String> roles) {
        Utilisateur existing = Utilisateur.findById(username);
        if (null == existing) {
            return null;
        }
        existing.password = new String(Base64.getEncoder().encode(password.getBytes()), Charset.defaultCharset());
        existing.roles = roles;
        return existing;
    }

    @Transactional
    public boolean delete(String username) {
        return Utilisateur.deleteById(username);
    }
}
