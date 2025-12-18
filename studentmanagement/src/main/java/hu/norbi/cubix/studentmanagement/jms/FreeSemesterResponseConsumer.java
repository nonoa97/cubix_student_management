package hu.norbi.cubix.studentmanagement.jms;

import hu.norbi.centraleducationsystem.jmsdto.FreeSemesterResponse;
import hu.norbi.cubix.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FreeSemesterResponseConsumer {

    private final StudentService studentService;

    @JmsListener(destination = "free_semester_responses", containerFactory = "educationFactory")
    public void onDelayMessage(FreeSemesterResponse freeSemesterResponse) {

        studentService.updateFreeSemesters(freeSemesterResponse.getStudentId(), freeSemesterResponse.getNumFreeSemesters());
    }


}
