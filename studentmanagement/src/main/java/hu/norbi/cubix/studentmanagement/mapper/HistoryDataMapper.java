package hu.norbi.cubix.studentmanagement.mapper;

import hu.norbi.cubix.studentmanagement.api.model.HistoryDataCourseDto;
import hu.norbi.cubix.studentmanagement.model.Course;
import hu.norbi.cubix.studentmanagement.model.HistoryData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoryDataMapper {
    HistoryDataCourseDto courseHistoryDataToDto(HistoryData<Course> hd);
}
