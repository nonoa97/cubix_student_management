package hu.norbi.cubix.studentmanagement.service;

import hu.norbi.cubix.studentmanagement.model.ScheduledLesson;
import hu.norbi.cubix.studentmanagement.model.Semester;
import hu.norbi.cubix.studentmanagement.model.SpecialDay;
import hu.norbi.cubix.studentmanagement.repository.ScheduledLessonRepository;
import hu.norbi.cubix.studentmanagement.repository.SpecialDayRepository;
import hu.norbi.cubix.studentmanagement.repository.StudentRepository;
import hu.norbi.cubix.studentmanagement.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ScheduledLessonRepository scheduledLessonRepository;
    private final SpecialDayRepository specialDayRepository;

    @Transactional
    @Cacheable("studentSchedule")
    public Map<LocalDate, List<ScheduledLesson>> getStudentSchedule(int studentId, LocalDate startDate, LocalDate endDate) {
        Semester semester = getSemester(startDate, endDate);
        studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        List<ScheduledLesson> lessons = scheduledLessonRepository
                .findByStudentIdAndSemester(studentId,
                        semester.getYear(),
                        semester.getSemesterType());

        return buildSchedule(lessons, startDate, endDate);
    }

    @Transactional
    @Cacheable("teacherSchedule")
    public Map<LocalDate, List<ScheduledLesson>> getTeacherSchedule(int teacherId, LocalDate startDate, LocalDate endDate) {
        Semester semester = getSemester(startDate, endDate);
        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        List<ScheduledLesson> lessons = scheduledLessonRepository
                .findByTeacherIdAndSemester(teacherId,
                        semester.getYear(),
                        semester.getSemesterType());

        return buildSchedule(lessons, startDate, endDate);
    }

    public Map.Entry<LocalDate, ScheduledLesson> searchInStudentSchedule(
            int studentId, String courseName, LocalDate startDate) {
        Map<LocalDate, List<ScheduledLesson>> schedule =
                getStudentSchedule(studentId, startDate,
                        Semester.createSemesterFrom(startDate).getSemesterEnd());

        return searchInSchedule(schedule, courseName);
    }

    public Map.Entry<LocalDate, ScheduledLesson> searchInTeacherSchedule(
            int teacherId, String courseName, LocalDate startDate) {
        Map<LocalDate, List<ScheduledLesson>> schedule =
                getTeacherSchedule(teacherId, startDate,
                        Semester.createSemesterFrom(startDate).getSemesterEnd());

        return searchInSchedule(schedule, courseName);
    }

    private Map.Entry<LocalDate, ScheduledLesson> searchInSchedule(
            Map<LocalDate, List<ScheduledLesson>> schedule,
            String courseName) {

        for (Map.Entry<LocalDate, List<ScheduledLesson>> entry : schedule.entrySet()) {
            LocalDate day = entry.getKey();
            List<ScheduledLesson> lessons = entry.getValue();

            for (ScheduledLesson sl : lessons) {
                if(sl.getCourse().getName().toLowerCase()
                        .startsWith(courseName.toLowerCase())) {
                    return Map.entry(day, sl);
                }
            }
        }

        return null;
    }

    private Map<LocalDate, List<ScheduledLesson>> buildSchedule(
            List<ScheduledLesson> lessons,
            LocalDate startDate,
            LocalDate endDate) {

        Map<LocalDate, List<ScheduledLesson>> schedule = new HashMap<>();

        Map<Integer, List<ScheduledLesson>> lessonsByDayOfWeek = lessons.stream()
                .collect(Collectors.groupingBy(ScheduledLesson::getDayOfWeek));

        List<SpecialDay> specialDays = specialDayRepository.findBySourceDayOrTargetDay(startDate, endDate);
        Map<LocalDate, List<SpecialDay>> specialDaysBySource = specialDays.stream()
                .collect(Collectors.groupingBy(SpecialDay::getSource));
        Map<LocalDate, List<SpecialDay>> specialDaysByTarget = specialDays.stream()
                .filter(sd -> sd.getTarget() != null)
                .collect(Collectors.groupingBy(SpecialDay::getTarget));

        for(LocalDate day = startDate; !day.isAfter(endDate); day = day.plusDays(1)) {
            ArrayList<ScheduledLesson> lessonsOnDay = new ArrayList<>();

            int dayOfWeek = day.getDayOfWeek().getValue();

            List<ScheduledLesson> normalLessons = lessonsByDayOfWeek.get(dayOfWeek);
            if(normalLessons != null && isDayNotMovedOrBankHoliday(specialDaysBySource, day)) {
                lessonsOnDay.addAll(normalLessons);
            }

            Integer movedDayOfWeek = getDayOfWeekMovedToThisDay(specialDaysByTarget, day);
            if(movedDayOfWeek != null) {
                lessonsOnDay.addAll(lessonsByDayOfWeek.get(movedDayOfWeek));
            }

            lessonsOnDay.sort(Comparator.comparing(ScheduledLesson::getStartTime));
            schedule.put(day, lessonsOnDay);
        }

        return schedule;
    }

    private static Semester getSemester(LocalDate startDate, LocalDate endDate) {
        Semester semester = Semester.createSemesterFrom(startDate);
        Semester semesterEnd = Semester.createSemesterFrom(endDate);
        if(!semester.equals(semesterEnd)) {
            throw new IllegalArgumentException("from and until should be in the same semester");
        }
        return semester;
    }

    private Integer getDayOfWeekMovedToThisDay(Map<LocalDate, List<SpecialDay>> specialDaysByTargetDay, LocalDate day) {
        List<SpecialDay> movedToThisDay = specialDaysByTargetDay.get(day);
        if(movedToThisDay == null || movedToThisDay.isEmpty())
            return null;

        return movedToThisDay.get(0).getSource().getDayOfWeek().getValue();
    }


    private boolean  isDayNotMovedOrBankHoliday(Map<LocalDate, List<SpecialDay>> specialDaysBySourceDay, LocalDate day) {
        List<SpecialDay> specialDaysOnDay = specialDaysBySourceDay.get(day);
        return specialDaysOnDay == null || specialDaysOnDay.isEmpty();
    }

}
