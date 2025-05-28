package irrigationsystem.controller;

import irrigationsystem.dto.ReportDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/report")
    @SendTo("/notification/report")
    public void sendReport(ReportDto report) {
        messagingTemplate.convertAndSend("/notification/report", report);
    }
}
