package hu.norbi.cubix.studentmanagement.mapper;


import hu.norbi.cubix.studentmanagement.api.model.StudentDto;
import hu.norbi.cubix.studentmanagement.model.Student;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDto studentToStudentDto(Student student);
    Student studentDtoToStudent(StudentDto studentDto);

    List<StudentDto> studentListToStudentDtoList(List<Student> students);
    List<Student> studentDtoListToStudentList(List<StudentDto> studentDtos);

}
