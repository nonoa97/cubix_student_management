package hu.norbi.cubix.studentmanagement.controller;

import com.querydsl.core.types.Predicate;
import hu.norbi.cubix.studentmanagement.api.CourseControllerApi;
import hu.norbi.cubix.studentmanagement.api.model.CourseDto;
import hu.norbi.cubix.studentmanagement.api.model.HistoryDataCourseDto;
import hu.norbi.cubix.studentmanagement.mapper.CourseMapper;
import hu.norbi.cubix.studentmanagement.mapper.HistoryDataMapper;
import hu.norbi.cubix.studentmanagement.model.Course;
import hu.norbi.cubix.studentmanagement.model.HistoryData;
import hu.norbi.cubix.studentmanagement.model.SpecialDay;
import hu.norbi.cubix.studentmanagement.repository.CourseRepository;
import hu.norbi.cubix.studentmanagement.repository.SpecialDayRepository;
import hu.norbi.cubix.studentmanagement.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CourseController implements CourseControllerApi {

    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final NativeWebRequest webRequest;
    private final HistoryDataMapper historyDataMapper;
    private final PageableHandlerMethodArgumentResolver pageableResolver;
    private final QuerydslPredicateArgumentResolver predicateResolver;
    private final CourseRepository courseRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final SpecialDayRepository specialDayRepository;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(webRequest);
    }

    @Override
    public ResponseEntity<CourseDto> getCourseAtById(Integer id, LocalDateTime date) {
        Course course = courseService.getCourseAt(id, date);
        return ResponseEntity.ok(courseMapper.courseToDto(course));
    }

    @Override
    public ResponseEntity<List<HistoryDataCourseDto>> getHistoryById(Long id) {
        List<HistoryData<Course>> courses = courseService.getCourseHistory(id);

        List<HistoryDataCourseDto> historyWithDtos = new ArrayList<>();
        courses.forEach(hd -> {
            historyWithDtos.add(
                    historyDataMapper.courseHistoryDataToDto(hd)
            );
        });

        return ResponseEntity.ok(historyWithDtos);
    }

    @Override
    public ResponseEntity<CourseDto> updateCourse(Integer id, CourseDto courseDto) {
        Course course = courseMapper.dtoToCourse(courseDto);
        return ResponseEntity.ok( courseMapper.courseToDtoSummary(courseService.updateCourse(id, course)));
    }

    @Override
    public ResponseEntity<List<CourseDto>> searchCourse(Integer id, String name, String studentName, String teacherName, Integer page, Integer size, String sort) {
        Predicate predicate = createPredicate("configurePredicate");
        Pageable pageable = createPageable( "configPageable");

        return ResponseEntity.ok(courseMapper.coursesToCourseDtos(courseService.searchCourses(predicate,pageable)));

    }

    public void configPageable(@SortDefault("id") Pageable pageable) {

    }

    public void configurePredicate(@QuerydslPredicate(root = Course.class) Predicate predicate){

    }

    private Predicate createPredicate(String configMethodName) {
        Method method;
        try {
            method = this.getClass().getMethod(configMethodName, Predicate.class);
            MethodParameter methodParameter = new MethodParameter(method, 0);
            ModelAndViewContainer mavContainer = null;
            WebDataBinderFactory binderFactory = null;
            return (Predicate) predicateResolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Pageable createPageable(String pageableConfigurerMethodName) {
        Method method = null;
        try {
            method = this.getClass().getMethod(pageableConfigurerMethodName, Pageable.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        MethodParameter methodParameter = new MethodParameter(method, 0);
        ModelAndViewContainer mavContainer= null;
        WebDataBinderFactory binderFactory = null;
        Pageable pageable = pageableResolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        return pageable;
    }

    @Override
    public ResponseEntity<Void> cancelLessons(Integer courseId, LocalDate day) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        SpecialDay sp = new SpecialDay();
        sp.setSource(day);
        specialDayRepository.save(sp);

        messagingTemplate.convertAndSend("/topic/courseChat/" + courseId,
                String.format("A %s kurzus %s napon elmarad.", course.getName(), day));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> rescheduleLessons(Integer courseId, LocalDate targetDay, LocalDate sourceDay) {
        if(targetDay.getDayOfWeek() == DayOfWeek.SATURDAY || targetDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            SpecialDay sp = new SpecialDay();
            sp.setSource(sourceDay);
            sp.setTarget(targetDay);
            specialDayRepository.save(sp);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
