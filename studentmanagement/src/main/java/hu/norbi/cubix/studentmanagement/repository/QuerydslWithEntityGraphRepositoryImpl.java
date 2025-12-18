package hu.norbi.cubix.studentmanagement.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import hu.norbi.cubix.studentmanagement.model.Course;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

public class QuerydslWithEntityGraphRepositoryImpl
        extends SimpleJpaRepository<Course, Integer>
        implements QuerydslWithEntityGraphRepository<Course, Integer> {

    private  EntityManager entityManager;
    private EntityPath<Course> path;
    private PathBuilder<Course> pathBuilder;
    private Querydsl querydsl;

    public QuerydslWithEntityGraphRepositoryImpl(EntityManager em) {
        super(Course.class, em);
        this.entityManager = em;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Course.class);
        this.pathBuilder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, pathBuilder);
    }

    @Override
    public List<Course> findAll(Predicate predicate, String egName, Sort sort) {

        JPAQuery query =(JPAQuery) querydsl.applySorting(sort, createQuery(predicate).select(path));
        EntityGraph<?> entityGraph = entityManager.getEntityGraph(egName);
        query.setHint(org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD.getKey(), entityGraph);
        return query.fetch();


    }



    public Page<Course> findAll(Predicate predicate, String egName, Pageable pageable) {

        JPAQuery query =(JPAQuery) querydsl.applyPagination(pageable, createQuery(predicate).select(path));

        EntityGraph<?> entityGraph = entityManager.getEntityGraph(egName);
        query.setHint(org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD.getKey(), entityGraph);


        long total = createQuery(predicate).fetchCount();

        return PageableExecutionUtils.getPage(query.fetch(), pageable, () -> total);


    }

    private JPAQuery createQuery(Predicate predicate) {
        return querydsl.createQuery(path).where(predicate);
    }
}
