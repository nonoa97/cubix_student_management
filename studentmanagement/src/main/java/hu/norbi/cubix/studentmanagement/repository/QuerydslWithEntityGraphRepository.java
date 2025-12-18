package hu.norbi.cubix.studentmanagement.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.List;

public interface QuerydslWithEntityGraphRepository<T, ID> {

    List<T> findAll(Predicate predicate, String egName, Sort sort);

    Page<T> findAll(Predicate predicate, String egName, Pageable pageable);


}
