package com.example.management_task.repository;

import com.example.management_task.repository.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments,Long> {
    Optional<Comments> findByIdAndAuthorEmail(Long taskId, String authorEmail);
}
