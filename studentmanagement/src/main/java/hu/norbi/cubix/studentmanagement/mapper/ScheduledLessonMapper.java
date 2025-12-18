package hu.norbi.cubix.studentmanagement.mapper;

import hu.norbi.cubix.studentmanagement.api.model.ScheduledLessonDto;
import hu.norbi.cubix.studentmanagement.model.ScheduledLesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduledLessonMapper {


    @Mapping(target = "courseName", source="course.name")
    public ScheduledLessonDto scheduledLessonToDto(ScheduledLesson lesson);

    public List<ScheduledLessonDto> scheduledLessonsToDtos(List<ScheduledLesson> items);

}