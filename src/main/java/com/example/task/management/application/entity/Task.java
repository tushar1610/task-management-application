package com.example.task.management.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;
    @NotNull
    @NotBlank(message = "Task Heading cannot be blank")
    @Length(max = 50)
    private String taskHeading;
    @NotNull
    @NotBlank(message = "Task Description cannot be blank")
    @Length(max = 50)
    private String taskDescription;
    private LocalDate taskCreationDate;
    private LocalTime taskCreationTime;
    private Boolean isTaskCompleted;
}
