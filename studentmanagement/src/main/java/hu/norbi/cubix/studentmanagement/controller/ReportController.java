package hu.norbi.cubix.studentmanagement.controller;


import hu.norbi.cubix.studentmanagement.dto.StudentAvgSemesterPerCourseDto;
import hu.norbi.cubix.studentmanagement.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
public class ReportController {

    private final CourseService courseService;

    @GetMapping("/api/report")
    @Async
    public CompletableFuture<List<StudentAvgSemesterPerCourseDto>> generateReport (){
        System.out.println("CourseController.generateReport called at thread" + Thread.currentThread().getName());
        return  CompletableFuture.completedFuture(courseService.generateReport());

    }
}
