package hu.norbi.centraleducationsystem.jms;

import hu.norbi.centraleducationsystem.jmsdto.FreeSemesterRequest;
import hu.norbi.centraleducationsystem.jmsdto.FreeSemesterResponse;
import hu.norbi.centraleducationsystem.ws.StudentXmlWs;
import jakarta.jms.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FreeSemesterRequestConsumer {

    private final JmsTemplate jmsTemplate;
    private final StudentXmlWs studentXmlWs;

    @JmsListener(destination = "free_semester_request")
    public void onDelayMessage(Message<FreeSemesterRequest> message) {
      Topic replyTo = (Topic) message.getHeaders().get(JmsHeaders.REPLY_TO);

        int studentId = message.getPayload().getStudentId();
        int freeSemesterUsed = studentXmlWs.getFreeSemesterUsed(studentId);

        FreeSemesterResponse response = new FreeSemesterResponse();
        response.setStudentId(studentId);
        response.setNumFreeSemesters(freeSemesterUsed);
        
        jmsTemplate.convertAndSend(replyTo, response);

    }
}
