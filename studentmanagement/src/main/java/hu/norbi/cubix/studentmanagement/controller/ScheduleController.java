package hu.norbi.cubix.studentmanagement.controller;

import hu.norbi.cubix.studentmanagement.api.ScheduleControllerApi;
import hu.norbi.cubix.studentmanagement.api.model.ScheduledLessonDto;
import hu.norbi.cubix.studentmanagement.mapper.ScheduledLessonMapper;
import hu.norbi.cubix.studentmanagement.model.ScheduledLesson;
import hu.norbi.cubix.studentmanagement.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ScheduleController implements ScheduleControllerApi {

    private final ScheduleService scheduleService;
    private final ScheduledLessonMapper scheduledLessonMapper;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ScheduleControllerApi.super.getRequest();
    }

    @Override
    public ResponseEntity<List<ScheduledLessonDto>> getSchedule(Integer studentId, Integer teacherId, LocalDate from, LocalDate until) {
        if(!validateId(studentId, teacherId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Map<LocalDate, List<ScheduledLesson>> schedule;
        if (studentId != null) {
            schedule = scheduleService.getStudentSchedule(studentId, from, until);
        } else {
            schedule = scheduleService.getTeacherSchedule(teacherId, from, until);
        }

        List<ScheduledLessonDto> result = convertScheduleToDto(schedule);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ScheduledLessonDto> searchSchedule(Integer studentId, Integer teacherId, LocalDate from, String course) {
        if(!validateId(studentId, teacherId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Map.Entry<LocalDate, ScheduledLesson> foundScheduledLessons = null;
        if (studentId != null) {
            foundScheduledLessons = scheduleService.searchInStudentSchedule(studentId,  course, from);
        } else {
            foundScheduledLessons = scheduleService.searchInTeacherSchedule(teacherId, course, from);
        }
        if(foundScheduledLessons == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        ScheduledLessonDto scheduledLessonDto = scheduledLessonMapper.scheduledLessonToDto(foundScheduledLessons.getValue());
        scheduledLessonDto.setDay(foundScheduledLessons.getKey());

        return ResponseEntity.ok(scheduledLessonDto);
    }


    private List<ScheduledLessonDto> convertScheduleToDto(
            Map<LocalDate, List<ScheduledLesson>> schedule) {

        ArrayList<ScheduledLessonDto> result = new ArrayList<>();

        for (Map.Entry<LocalDate, List<ScheduledLesson>> entry : schedule.entrySet()) {
            LocalDate day = entry.getKey();
            List<ScheduledLesson> lessons = entry.getValue();

            List<ScheduledLessonDto> lessonDtos =
                    scheduledLessonMapper.scheduledLessonsToDtos(lessons);

            lessonDtos.forEach(i -> i.setDay(day));
            result.addAll(lessonDtos);
        }

        return result;
    }

    private boolean validateId(Integer teacherId, Integer studentId) {
        return (studentId != null && teacherId == null) ||
                (studentId == null && teacherId != null);
    }

}
