package com.monapp.auth;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Entity
@Table(name = "users")
public class Utilisateur extends PanacheEntityBase {

    @Id
    @NotBlank(message = "L'identifiant ne doit pas être vide")
    @Size(min = 2, message = "L'identifiant doit faire au minimum deux caractères")
    public String username;

    @NotBlank(message = "L'identifiant ne doit pas être vide")
    @Size(min = 4, message = "Le mot de passe ne doit pas faire moins de 4 caractères")
    public String password;

    @Convert(converter = RolesConverter.class)
    public Set<String> roles;

    public Utilisateur() {}

    public Utilisateur(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
