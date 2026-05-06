package com.monapp.auth;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Set;

@Singleton
public class Startup {

    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        // reset and load all test users
        Utilisateur.deleteAll();
        Utilisateur user = new Utilisateur(
                "user",
                new String(Base64.getEncoder().encode("secret".getBytes()), Charset.defaultCharset()),
                Set.of("user")
        );
        user.persist();

        Utilisateur admin = new Utilisateur(
                "admin",
                new String(Base64.getEncoder().encode("admin123".getBytes()), Charset.defaultCharset()),
                Set.of("user", "admin")
        );
        admin.persist();
    }
}