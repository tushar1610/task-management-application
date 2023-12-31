package com.example.task.management.application.service;

import com.example.task.management.application.entity.Task;

public interface TaskService {
    Task addTask(Task task);

    Task getTaskById(Long id);

    Task updateTaskById(Long id, Task task);

    void deleteTaskById(Long id);

    Task markTaskAsCompleted(Long id);
}
