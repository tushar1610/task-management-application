package com.example.task.management.application.serviceImpl;

import com.example.task.management.application.entity.Task;
import com.example.task.management.application.entity.User;
import com.example.task.management.application.repository.TaskRepository;
import com.example.task.management.application.repository.UserRepository;
import com.example.task.management.application.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Task addTask(Long userId, Task task) {
        User user = userRepository.findById(userId).get();
        Task newTask = Task.builder()
                .taskHeading(task.getTaskHeading())
                .taskDescription(task.getTaskDescription())
                .taskCreationDate(LocalDate.now())
                .taskCreationTime(LocalTime.now())
                .isTaskCompleted(false)
                .user(user)
                .build();
        return taskRepository.save(newTask);
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    @Override
    public Task updateTaskById(Long taskId, Task task) {
        Optional<Task> savedTask = taskRepository.findById(taskId);
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
    public void deleteTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).get();
        task.setUser(null);
        taskRepository.save(task);
        taskRepository.deleteById(taskId);
    }

    @Override
    public Task markTaskAsCompleted(Long taskId) {
        Optional<Task> savedTask = taskRepository.findById(taskId);
        if (savedTask.isEmpty()) {
            return null;
        }
        savedTask.get().setIsTaskCompleted(true);
        return taskRepository.save(savedTask.get());
    }

    @Override
    public List<Task> getAllTasksByUserId(Long userId) {
        return taskRepository.findAllByUserUserId(userId);
    }
}
