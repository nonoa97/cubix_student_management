package hu.norbi.cubix.studentmanagement.service;

import hu.norbi.cubix.studentmanagement.model.*;
import hu.norbi.cubix.studentmanagement.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InitDbService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ScheduledLessonRepository scheduledLessonRepository;
    private final SpecialDayRepository specialDayRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void deleteAudTables() {

        jdbcTemplate.update("DELETE FROM student_course_aud");
        jdbcTemplate.update("DELETE FROM teacher_course_aud");
        jdbcTemplate.update("DELETE FROM user_course_aud");
        jdbcTemplate.update("DELETE FROM student_aud");
        jdbcTemplate.update("DELETE FROM teacher_aud");
        jdbcTemplate.update("DELETE FROM student_management_user_aud");
        jdbcTemplate.update("DELETE FROM course_aud");



    }

    @Transactional
    public void deleteAll() {
        scheduledLessonRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        courseRepository.deleteAll();

    }


    @Transactional
    public void initDb() {



    Semester semester = new Semester();
    semester.setSemesterType(SemesterType.FALL);
    semester.setYear(2025);

        Course c1 = new Course();
        c1.setName("Kurzus 1");
        c1.setSemester(semester);
        Course c2 = new Course();
        c2.setName("Kurzus 2");
        Course c3 = new Course();
        c3.setName("Kurzus 3");
        Course c4 = new Course();
        c4.setName("Kurzus 4");

        courseRepository.saveAll(List.of(c1, c2, c3, c4));

        ScheduledLesson sl1 = new ScheduledLesson();
        sl1.setCourse(c1);
        sl1.setDayOfWeek(1);
        sl1.setStartTime(LocalTime.of(8, 0));
        sl1.setEndTime(LocalTime.of(9, 0));

        ScheduledLesson sl2 = new ScheduledLesson();
        sl2.setCourse(c1);
        sl2.setDayOfWeek(2);
        sl2.setStartTime(LocalTime.of(8, 0));
        sl2.setEndTime(LocalTime.of(9, 0));

        ScheduledLesson sl3 = new ScheduledLesson();
        sl3.setCourse(c1);
        sl3.setDayOfWeek(2);
        sl3.setStartTime(LocalTime.of(10, 0));
        sl3.setEndTime(LocalTime.of(12, 0));

        ScheduledLesson sl4 = new ScheduledLesson();
        sl4.setCourse(c1);
        sl4.setDayOfWeek(3);
        sl4.setStartTime(LocalTime.of(8, 0));
        sl4.setEndTime(LocalTime.of(9, 0));
        scheduledLessonRepository.saveAll(List.of(sl1, sl2, sl3, sl4));

        //-------------------------------------------

        Teacher t1 = new Teacher();
        t1.setName("Tanár 1");
        t1.setUsername("tanar1");
        t1.setPassword(passwordEncoder.encode("pass"));
        t1.setBirthdate(LocalDate.of(1980, 1, 1));
        t1.getCourses().addAll(List.of(c1, c2));

        Teacher t2 = new Teacher();
        t2.setName("Tanár 2");
        t2.setUsername("tanar2");
        t2.setPassword(passwordEncoder.encode("pass"));
        t2.setBirthdate(LocalDate.of(1970, 5, 2));
        t2.getCourses().addAll(List.of(c3, c4));

        Teacher t3 = new Teacher();
        t3.setName("Tanár 3");
        t3.setUsername("tanar3");
        t3.setPassword(passwordEncoder.encode("pass"));
        t3.setBirthdate(LocalDate.of(1966, 7, 23));
        t3.getCourses().addAll(List.of(c4, c2));

        teacherRepository.saveAll(List.of(t1, t2, t3));

        //-------------------------------------------------------------

        Student s1 = new Student();
        s1.setName("Diák 1");
        s1.setUsername("diak1");
        s1.setPassword(passwordEncoder.encode("pass"));
        s1.setCentralId(100);
        s1.setBirthdate(LocalDate.of(2001, 1, 1));
        s1.setSemester(2);
        s1.getCourses().addAll(List.of(c1, c2));

        Student s2 = new Student();
        s2.setName("Diák 2");
        s2.setUsername("diak2");
        s2.setPassword(passwordEncoder.encode("pass"));
        s2.setCentralId(200);
        s2.setBirthdate(LocalDate.of(2001, 10, 1));
        s2.setSemester(2);
        s2.getCourses().addAll(List.of(c1, c2));

        Student s3 = new Student();
        s3.setName("Diák 3");
        s3.setUsername("diak3");
        s3.setPassword(passwordEncoder.encode("pass"));
        s3.setCentralId(300);
        s3.setBirthdate(LocalDate.of(2002, 5, 1));
        s3.setSemester(1);
       s3.getCourses().addAll(List.of(c3, c4));


        Student s4 = new Student();
        s4.setName("Diák 4");
        s4.setUsername("diak4");
        s4.setPassword(passwordEncoder.encode("pass"));
        s4.setCentralId(400);
        s4.setBirthdate(LocalDate.of(2006, 12, 21));
        s4.setSemester(1);
        s4.getCourses().addAll(List.of(c3, c4));

        studentRepository.saveAll(List.of(s1, s2, s3, s4));


    }


}
