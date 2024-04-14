package com.game.repository;

import com.game.entity.*;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {
    private static final List<Task> storage = new CopyOnWriteArrayList<Task>() {{
        add(new Task(1L, "buy milk", Status.PAUSED));
        add(new Task(2L, "go dance", Status.IN_PROGRESS));
        add(new Task(3L, "drive bike", Status.DONE));
        add(new Task(4L, "clean car", Status.IN_PROGRESS));
        add(new Task(5L, "visit friend", Status.PAUSED));
        add(new Task(6L, "get job", Status.IN_PROGRESS));
        add(new Task(7L, "go to forest", Status.DONE));
        add(new Task(8L, "find place", Status.IN_PROGRESS));
        add(new Task(9L, "drink water", Status.PAUSED));
        add(new Task(10L, "walk with a dog", Status.PAUSED));
    }};

    public List<Task> getAll(int pageNumber, int pageSize) {
        return storage.stream()
                .sorted(Comparator.comparingLong(Task::getId))
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    public int getAllCount() {
        return storage.size();
    }

    public Task save(Task task) {
        task.setId(getMaxId() + 1);
        storage.add(task);
        return task;
    }

    public Task update(Task task) {
        return task;
    }

    public Optional<Task> findById(long id) {
        return storage.stream().filter(task -> id == task.getId()).findFirst();
    }

    public void delete(Task task) {
        storage.remove(task);
    }

    private long getMaxId() {
        return storage.stream()
                .map(Task::getId)
                .max(Long::compareTo)
                .orElse(1L);
    }
}
