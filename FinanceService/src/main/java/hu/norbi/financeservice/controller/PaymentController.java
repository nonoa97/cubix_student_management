package hu.norbi.financeservice.controller;

import hu.norbi.financeservice.dto.PaymentDto;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final JmsTemplate jmsTemplate;

    public PaymentController(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @PostMapping("/api/payments")
    public void registerPayment(@RequestBody PaymentDto payment) {
        this.jmsTemplate.convertAndSend("payments", payment);
    }
}
