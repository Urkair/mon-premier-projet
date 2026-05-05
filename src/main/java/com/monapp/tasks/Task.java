package com.monapp.tasks;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Task extends PanacheEntity {

    @NotBlank(message = "Le titre ne doit pas être vide")
    @Size(min = 2, max = 50, message = "Le titre doit faire entre 2 et 50 caractères ")
    private String titre;

    @NotBlank(message = "La description ne doit pas être vide")
    @Size(max = 500, message = "La description ne doit pas excéder 500 caractères")
    private String description;
    private boolean terminee;

    public Task(String titre, String description) {
        this.titre = titre;
        this.description = description;
        this.terminee = false;
    }

    public Task() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTerminee() {
        return terminee;
    }

    public void markAsDone() {
        this.terminee = true;
    }


}
