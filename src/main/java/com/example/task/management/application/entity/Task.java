package com.example.task.management.application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    @NotEmpty(message = "Task Heading cannot be blank")
    @Length(max = 50)
    private String taskHeading;
    @NotEmpty(message = "Task Description cannot be blank")
    @Length(max = 50)
    private String taskDescription;
    private LocalDate taskCreationDate;
    private LocalTime taskCreationTime;
    private Boolean isTaskCompleted;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = User.class)
    @JoinColumn(
            name = "userId",
            referencedColumnName = "userId"
    )
    private User user;
}
