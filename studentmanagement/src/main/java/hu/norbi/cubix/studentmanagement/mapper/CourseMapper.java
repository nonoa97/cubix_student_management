package hu.norbi.cubix.studentmanagement.mapper;



import hu.norbi.cubix.studentmanagement.api.model.CourseDto;
import hu.norbi.cubix.studentmanagement.model.Course;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {


    Course dtoToCourse(CourseDto dto);

    CourseDto courseToDto(Course course);

    @Named("summary")
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    CourseDto courseToDtoSummary(Course course);

    List<CourseDto> coursesToCourseDtos(List<Course> courses);
    List<Course> courseDtosToCourses(List<CourseDto> courseDtos);

    @IterableMapping(qualifiedByName = "summary")
    List<CourseDto> coursesToCourseDtosSummary(Iterable<Course> courses);

    List<CourseDto> coursesToCourseDtos(Iterable<Course> courses);


}
