package com.monapp.tasks;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TaskService {

    public List<Task> listerTout() {
        return Task.listAll();
    }

    public Task trouverParId(Long id) {
        return Task.findById(id);
    }

    @Transactional
    public Task creer(Task task) {
        Task.persist(task);
        return task;
    }

    @Transactional
    public Task modifier(Long id, Task modifs) {
        Task existing = Task.findById(id);
        if (existing == null) return null;
        existing.setTitre(modifs.getTitre());
        existing.setDescription(modifs.getDescription());
        if (modifs.isTerminee()) {
            existing.markAsDone();
        }
        return existing;
    }

    @Transactional
    public boolean supprimer(Long id) {
        return Task.deleteById(id);
    }
}
