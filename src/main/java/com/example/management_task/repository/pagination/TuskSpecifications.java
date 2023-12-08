package com.example.management_task.repository.pagination;

import com.example.management_task.dto.SearchFilterRequest;
import com.example.management_task.repository.entity.TaskEntity;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class TuskSpecifications extends AbstractSpecification<TaskEntity>{
    private final SearchFilterRequest filterRequest;

    @Override
    protected Stream<Predicate> getPredicates(Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();


        if (filterRequest.getAuthorId() != null) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("author").get("id"), filterRequest.getAuthorId())
            ));
        }

        if (filterRequest.getExecutorId() != null) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("executor").get("id"), filterRequest.getExecutorId())
            ));
        }

        if (filterRequest.getSearch() != null) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("header")), "%" + filterRequest.getSearch().toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("definition")), "%" + filterRequest.getSearch().toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.join("comments", JoinType.LEFT).get("text")), "%" + filterRequest.getSearch().toLowerCase() + "%")
            ));

        }
        if (filterRequest.getTuskStatus() != null) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("status"), filterRequest.getTuskStatus())
            ));
        }

        if (filterRequest.getPriority() != null) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("priority"), filterRequest.getPriority())
            ));
        }

        return predicates.stream();
    }


}
