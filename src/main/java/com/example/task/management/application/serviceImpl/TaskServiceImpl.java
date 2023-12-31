package com.example.task.management.application.serviceImpl;

import com.example.task.management.application.entity.Task;
import com.example.task.management.application.repository.TaskRepository;
import com.example.task.management.application.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task addTask(Task task) {
        Task newTask = Task.builder()
                .taskHeading(task.getTaskHeading())
                .taskDescription(task.getTaskDescription())
                .taskCreationDate(LocalDate.now())
                .taskCreationTime(LocalTime.now())
                .isTaskCompleted(false)
                .build();
        return taskRepository.save(newTask);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public Task updateTaskById(Long id, Task task) {
        Optional<Task> savedTask = taskRepository.findById(id);
        if (savedTask.isEmpty()) {
            return null;
        }
        if(!savedTask.get().getTaskHeading().equals(task.getTaskHeading())){
            savedTask.get().setTaskHeading(task.getTaskHeading());
        }
        if(!savedTask.get().getTaskDescription().equals(task.getTaskDescription())){
            savedTask.get().setTaskDescription(task.getTaskDescription());
        }
        return taskRepository.save(savedTask.get());
    }

    @Override
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task markTaskAsCompleted(Long id) {
        Optional<Task> savedTask = taskRepository.findById(id);
        if (savedTask.isEmpty()) {
            return null;
        }
        savedTask.get().setIsTaskCompleted(true);
        return taskRepository.save(savedTask.get());
    }
}
