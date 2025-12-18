package hu.norbi.cubix.studentmanagement;

import hu.norbi.cubix.studentmanagement.service.InitDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
@EnableAsync
public class StudentManagementApplication implements CommandLineRunner {

    private final InitDbService initDbService;

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initDbService.deleteAll();
        initDbService.deleteAudTables();
        initDbService.initDb();

    }
}
