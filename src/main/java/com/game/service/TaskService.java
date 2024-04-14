package com.game.service;

import com.game.entity.*;
import com.game.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(@Autowired TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getById(Long id) {
        return taskRepository.findById(id).get();
    }

    public List<Task> getAll(int pageNumber, int pageSize) {
        return taskRepository.getAll(pageNumber, pageSize);
    }

    public Integer getAllCount() {
        return taskRepository.getAllCount();
    }

    public Task createTask(String description, Status status) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);

        return taskRepository.save(task);
    }

    public Task updateTask(long id, String description, Status status) {
        Task task = taskRepository.findById(id).orElse(null);
        if (isNull(task)) {
            return null;
        }

        boolean needUpdate = false;

        if (!StringUtils.isEmpty(description)) {
            task.setDescription(description);
            needUpdate = true;
        }
        if (!StringUtils.isEmpty(status)) {
            task.setStatus(status);
            needUpdate = true;
        }

        if (needUpdate) {
            taskRepository.update(task);
        }

        return task;
    }

    public Task delete(long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (isNull(task)) {
            return null;
        }

        taskRepository.delete(task);
        return task;
    }
}
