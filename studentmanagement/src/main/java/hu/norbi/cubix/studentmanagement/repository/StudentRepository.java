package hu.norbi.cubix.studentmanagement.repository;

import hu.norbi.cubix.studentmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository  extends JpaRepository<Student, Integer> {

    Optional<Student>findByCentralId(int centralId);
}
