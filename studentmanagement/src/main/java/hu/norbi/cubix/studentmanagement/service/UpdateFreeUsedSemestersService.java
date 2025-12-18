package hu.norbi.cubix.studentmanagement.service;

import hu.norbi.cubix.studentmanagement.model.Student;
import hu.norbi.cubix.studentmanagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateFreeUsedSemestersService {

    private final StudentRepository studentRepository;
    private final ExternalSystemService externalSystemService;


    @Scheduled(cron = "#{@scheduleConfigProperties.getCron()}")
    public void UpdateFreeUsedSemesters() {
        List<Student> students = studentRepository.findAll();

        students.forEach(student -> {
            try {
//                student.setFreeSemestersUsed(externalSystemService.getFreeSemesterUsed(student));
//                studentRepository.save(student);
//                System.out.println(student.getName()+ "tanuló elhasznált szemeszterei :" + student.getFreeSemestersUsed());

               externalSystemService.getFreeSemesterUsedForStudent(student);

            }catch (Exception e) {

            }

        });
    }
}
