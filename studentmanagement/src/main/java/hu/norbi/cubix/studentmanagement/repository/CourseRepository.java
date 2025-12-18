package hu.norbi.cubix.studentmanagement.repository;


import hu.norbi.cubix.studentmanagement.dto.StudentAvgSemesterPerCourseDto;
import hu.norbi.cubix.studentmanagement.model.Course;
import hu.norbi.cubix.studentmanagement.model.QCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer>,
        QuerydslPredicateExecutor<Course>,
        QuerydslBinderCustomizer<QCourse>,
        QuerydslWithEntityGraphRepository<Course, Integer> {

    @Override
    default void customize(QuerydslBindings bindings, QCourse course){
        bindings.bind(course.name).first((path, value) -> path.startsWithIgnoreCase(value));

        bindings.bind(course.teachers.any().name).first((path, value) -> path.startsWithIgnoreCase(value));

        bindings.bind(course.students.any().semester).all((path, values) -> {

                    if (values.isEmpty()) {
                        return Optional.empty();
                    }

                    Iterator<? extends Integer> it = values.iterator();

                    Integer from = 1;
                    Integer to = it.next();

                    if (it.hasNext()) {
                        from = to;
                        to = it.next();
                    }

                    return Optional.of(path.between(from, to));
                });
    }

    @Query("""
    SELECT c.name AS courseName, AVG(s.semester) AS avgSemester
    FROM Course c
    JOIN c.students s
    GROUP BY c.name
    ORDER BY AVG(s.semester) DESC
""")
    List<StudentAvgSemesterPerCourseDto> findAverageSemesterByCourse();





}
