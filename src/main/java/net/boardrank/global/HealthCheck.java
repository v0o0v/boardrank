package net.boardrank.global;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/healthCheck")
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok("good");
    }

}
