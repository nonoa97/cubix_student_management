package hu.norbi.cubix.studentmanagement.repository;

import hu.norbi.cubix.studentmanagement.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
