package hu.norbi.cubix.studentmanagement.mapper;


import hu.norbi.cubix.studentmanagement.api.model.TeacherDto;
import hu.norbi.cubix.studentmanagement.model.Teacher;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    Teacher dtoToTeacher(TeacherDto teacherDto);
    TeacherDto teacherToDto(Teacher teacher);

    List<TeacherDto> teachersToDto(List<Teacher> teachers);
    List<Teacher> teacherDtosToList(List<TeacherDto> teacherDtos);


}
