package com.example.management_task.repository.pagination;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class AbstractSpecification<T> implements Specification<T> {

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return getPredicates(root, query, criteriaBuilder)
                .filter(Objects::nonNull)
                .reduce(criteriaBuilder::and)
                .orElseGet(criteriaBuilder::conjunction);
    }

    protected abstract Stream<Predicate> getPredicates(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);

    protected Predicate eq(Root<?> root, CriteriaBuilder cb, String fieldName, Object value) {
        return value != null ? cb.equal(root.get(fieldName), value) : null;
    }

    protected Predicate like(Root<?> root, CriteriaBuilder cb, String fieldName, String value) {
        return value != null ? cb.like(cb.upper(root.get(fieldName)), "%" + StringUtils.upperCase(value) + "%") : null;
    }

    protected Predicate in(Root<?> root, CriteriaBuilder cb, String fieldName, Collection<?> value) {
        Predicate predicate;
        if (value == null) {
            predicate = null;
        } else if (value.isEmpty()) {
            predicate = cb.or();
        } else {
            predicate = root.get(fieldName).in(value);
        }
        return predicate;
    }

    protected Predicate joinEq(
            Root<?> root,
            CriteriaBuilder cb,
            String joinColumn,
            String fieldName,
            Object value
    ) {
        return value != null ? cb.equal(root.join(joinColumn).get(fieldName), value) : null;
    }

}
