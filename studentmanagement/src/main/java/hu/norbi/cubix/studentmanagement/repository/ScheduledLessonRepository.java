package hu.norbi.cubix.studentmanagement.repository;

import hu.norbi.cubix.studentmanagement.model.ScheduledLesson;
import hu.norbi.cubix.studentmanagement.model.SemesterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ScheduledLessonRepository extends JpaRepository<ScheduledLesson, Integer> {



    @Query("""
           SELECT sl FROM ScheduledLesson sl
           JOIN sl.course c
           JOIN c.students s
           WHERE s.id = :studentId
           AND c.semester.year = :year
           AND c.semester.semesterType = :semesterType
           """)
    public List<ScheduledLesson> findByStudentIdAndSemester( int studentId, int year, SemesterType semesterType );


    @Query("""
           SELECT sl FROM ScheduledLesson sl
           JOIN sl.course c
           JOIN c.teachers s
           WHERE s.id = :teacherId
           AND c.semester.year = :year
           AND c.semester.semesterType = :semesterType
           """)
    public List<ScheduledLesson> findByTeacherIdAndSemester( int teacherId, int year, SemesterType semesterType );
}
