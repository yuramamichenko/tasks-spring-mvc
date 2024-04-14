package com.game.controller;
import com.game.entity.Task;

import com.game.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("/rest/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(@Autowired TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public List<TaskInfo> getAll(@RequestParam(required = false) Integer pageNumber,
                                 @RequestParam(required = false) Integer pageSize) {
        pageNumber = isNull(pageNumber) ? 0 : pageNumber;
        pageSize = isNull(pageSize) ? 5 : pageSize;

        List<Task> players = taskService.getAll(pageNumber, pageSize);
        return players.stream().map(TaskController::toTaskInfo).collect(Collectors.toList());
    }

    @GetMapping("/count")
    public Integer getAllCount() {
        return taskService.getAllCount();
    }

    @GetMapping("/{ID}")
    public Task getById(@PathVariable("ID") long id) {
        return taskService.getById(id);
    }

    @PostMapping
    public ResponseEntity<TaskInfo> createPlayer(@RequestBody TaskInfo info) {
        if (StringUtils.isEmpty(info.description))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (isNull(info.status)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Task task = taskService.createTask(info.description, info.status);
        return ResponseEntity.status(HttpStatus.OK).body(toTaskInfo(task));
    }

    @PostMapping("/{ID}")
    public ResponseEntity<TaskInfo> updatePlayer(@PathVariable("ID") long id,
                                                 @RequestBody TaskInfo info) {
        if (id <= 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        if (isNull(info.description))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        if (isNull(info.status))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Task task = taskService.updateTask(id, info.description, info.status);
        if (isNull(task)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(toTaskInfo(task));
        }
    }

    @DeleteMapping("/{ID}")
    public ResponseEntity delete(@PathVariable("ID") long id) {
        if (id <= 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        Task task = taskService.delete(id);
        if (isNull(task)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    private static TaskInfo toTaskInfo(Task task) {
        if (isNull(task)) return null;

        TaskInfo result = new TaskInfo();
        result.id = task.getId();
        result.description = task.getDescription();
        result.status = task.getStatus();

        return result;
    }
}
