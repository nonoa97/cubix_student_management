package hu.norbi.cubix.studentmanagement.xmlws;

import hu.norbi.cubix.studentmanagement.api.model.ScheduledLessonDto;
import hu.norbi.cubix.studentmanagement.mapper.ScheduledLessonMapper;
import hu.norbi.cubix.studentmanagement.model.ScheduledLesson;
import hu.norbi.cubix.studentmanagement.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SearchScheduleXmlWsImpl  implements SearchScheduleXmlWs {

    private final ScheduleService scheduleService;
    private final ScheduledLessonMapper scheduledLessonMapper;


    @Override
    public List<ScheduledLessonDto> searchStudentSchedule(Integer studentId, LocalDate startDate, LocalDate endDate) {

        try {
            if (studentId != null) {
                ArrayList<ScheduledLessonDto> result = new ArrayList<>();

                Map<LocalDate, List<ScheduledLesson>> timeTableForStudent = scheduleService
                        .getStudentSchedule(studentId, startDate, endDate);

                for (Map.Entry<LocalDate, List<ScheduledLesson>> entry : timeTableForStudent.entrySet()) {
                    LocalDate day = entry.getKey();
                    List<ScheduledLesson> lessons = entry.getValue();
                    List<ScheduledLessonDto> dtos = scheduledLessonMapper.scheduledLessonsToDtos(lessons);
                    dtos.forEach(i -> i.setDay(day));
                    result.addAll(dtos);
                }
                return result;
            }

            else
                return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }


    }
}
