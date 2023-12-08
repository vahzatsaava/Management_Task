package com.example.management_task.repository;

import com.example.management_task.repository.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity,Long>, JpaSpecificationExecutor<TaskEntity> {
    Optional<TaskEntity> findByIdAndAuthorEmail(Long taskId, String authorEmail);
    Optional<TaskEntity> findByIdAndExecutorEmail(Long taskId,String executorEmail);
}
