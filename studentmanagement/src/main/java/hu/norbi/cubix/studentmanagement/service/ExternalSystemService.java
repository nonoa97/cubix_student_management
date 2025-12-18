package hu.norbi.cubix.studentmanagement.service;

import hu.norbi.centraleducationsystem.jmsdto.FreeSemesterRequest;
import hu.norbi.cubix.studentmanagement.aspect.RetryMethod;
import hu.norbi.cubix.studentmanagement.model.Student;
import hu.norbi.cubix.studentmanagement.wsclient.StudentXmlWsImplService;
import jakarta.jms.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalSystemService {
    
    private final JmsTemplate jmsTemplate;

    @RetryMethod(attempts = 5, delay = 500)
    public int getFreeSemesterUsed(Student student) {
        return new StudentXmlWsImplService()
                .getStudentXmlWsImplPort()
                .getFreeSemesterUsed(student.getCentralId());
    }
    
    public void getFreeSemesterUsedForStudent(Student student) {

        Topic replyTo = jmsTemplate.execute(session -> session.createTopic("free_semester_responses"));
        FreeSemesterRequest fsr = new FreeSemesterRequest();
        fsr.setStudentId(student.getCentralId());
        jmsTemplate.convertAndSend("free_semester_request", fsr, message -> {
            message.setJMSReplyTo(replyTo);
            return message;
        });


    }

}
