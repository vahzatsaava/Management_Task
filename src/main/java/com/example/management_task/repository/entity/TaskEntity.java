package com.example.management_task.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@EqualsAndHashCode(exclude = {
        "author",
        "executor",
        "comments"
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "header", unique = true)
    private String header;

    private String definition;

    @Enumerated(EnumType.STRING)
    private TuskStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDateTime created;

    private LocalDateTime finished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id")
    @ToString.Exclude
    private User executor;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Comments> comments;

}
