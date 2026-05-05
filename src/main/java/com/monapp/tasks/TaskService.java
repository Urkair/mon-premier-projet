package com.monapp.tasks;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class TaskService {

    private final Map<Long, Task> tasks = new LinkedHashMap<>();
    private final AtomicLong counter = new AtomicLong(1);

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