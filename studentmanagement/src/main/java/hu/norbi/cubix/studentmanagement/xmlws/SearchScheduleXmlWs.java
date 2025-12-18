package hu.norbi.cubix.studentmanagement.xmlws;

import hu.norbi.cubix.studentmanagement.api.model.ScheduledLessonDto;
import jakarta.jws.WebService;

import java.time.LocalDate;
import java.util.List;

@WebService
public interface SearchScheduleXmlWs {

    public List<ScheduledLessonDto> searchStudentSchedule(Integer studentId,
                                                          LocalDate startDate, LocalDate endDate);


}
