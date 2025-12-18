package hu.norbi.cubix.studentmanagement.service;


import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import hu.norbi.cubix.studentmanagement.dto.StudentAvgSemesterPerCourseDto;
import hu.norbi.cubix.studentmanagement.model.Course;
import hu.norbi.cubix.studentmanagement.model.HistoryData;
import hu.norbi.cubix.studentmanagement.model.QCourse;
import hu.norbi.cubix.studentmanagement.repository.CourseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public Iterable<Course> searchCoursesSummary(Predicate predicate, Pageable pageable) {
        return courseRepository.findAll(predicate, pageable);
    }

    @Transactional
    @Cacheable("pagedCoursesWithRelationships")
    public Iterable<Course> searchCourses(Predicate predicate, Pageable pageable) {

//        List<Course> courses = courseRepository.findAll(predicate,"Course.students", pageable).getContent();
//        courses =courseRepository.findAll(QCourse.course.in(courses), "Course.teachers", Sort.unsorted());

        List<Course> courses = courseRepository.findAll(predicate, pageable).getContent();
        BooleanExpression in = QCourse.course.in(courses);
        courses = courseRepository.findAll(in, "Course.students", Sort.unsorted());
        courses = courseRepository.findAll(in, "Course.teachers", pageable.getSort());

        return courses;
    }

    @Transactional
    public List<HistoryData<Course>> getCourseHistory(long id) {

        List resultList = AuditReaderFactory.get(entityManager)
                .createQuery()
                .forRevisionsOfEntity(Course.class, false, true)
                .add(AuditEntity.property("id").eq(id))
                .getResultList()
                .stream().map(o -> {
                            Object[] ob = (Object[]) o;
                            DefaultRevisionEntity defaultRevisionEntity = (DefaultRevisionEntity) ob[1];
                            Course course = (Course) ob[0];
                            course.getTeachers().size();
                            course.getStudents().size();
                            return new HistoryData<Course>(
                                    course,
                                    (RevisionType) ob[2],
                                    defaultRevisionEntity.getId(),
                                    defaultRevisionEntity.getRevisionDate()
                            );
                        }
                ).toList();

        return resultList;
    }

    @Transactional
    public Course getCourseAt(int id, LocalDateTime timeStamp) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        Date date = Timestamp.valueOf(timeStamp);
        Number revision = auditReader.getRevisionNumberForDate(date);


        Course course = auditReader.find(Course.class, id, revision);

        if (course == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            course.getStudents().size();
            course.getTeachers().size();
        }

        return course;
    }

    @Transactional
    public Course updateCourse(int id, Course course) {
        if (courseRepository.existsById(id)) {
            course.setId(id);
            return courseRepository.save(course);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<StudentAvgSemesterPerCourseDto> generateReport() {
        System.out.println("CourseService.generateReport called at thread" + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return courseRepository.findAverageSemesterByCourse();
    }
}