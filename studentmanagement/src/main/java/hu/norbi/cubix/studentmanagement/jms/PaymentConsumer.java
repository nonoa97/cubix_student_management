package hu.norbi.cubix.studentmanagement.jms;

import hu.norbi.cubix.studentmanagement.service.StudentService;
import hu.norbi.financeservice.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final StudentService studentService;

    @JmsListener(destination = "payments", containerFactory = "financeFactory")
    public void onPaymentMessage(PaymentDto dto) {
        studentService.updateBalance(dto.getStudentId(), dto.getAmount());
    }

}
