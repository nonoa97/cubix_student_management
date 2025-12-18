package hu.norbi.cubix.studentmanagement.service;


import hu.norbi.cubix.studentmanagement.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
@RequiredArgsConstructor
public class StudentService {

    private final ExternalSystemService externalSystemMockService;
    private final StudentRepository studentRepository;

    @Value("${image.upload.dir:uploads/profile-images}")
    private String uploadDir;


    public Resource getProfilePicture(Integer id) {
        FileSystemResource fileSystemResource = new FileSystemResource(getProfilePicPathForStudent(id));
        if(!fileSystemResource.exists())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return fileSystemResource;

    }

    public void saveProfilePicture(Integer id, InputStream inputStream) {
        if(!studentRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        try{
            Files.copy(inputStream,getProfilePicPathForStudent(id), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Path getProfilePicPathForStudent(Integer id) {
        return Paths.get(uploadDir, id.toString() + ".jpg");
    }

    @Transactional
    public void updateBalance(int studentId, int amount) {
        studentRepository.findById(studentId).ifPresent(s -> s.setBalance(s.getBalance() + amount));
    }

    @Transactional
    public void updateFreeSemesters(int centralId, int freeSemestersUsed) {
        studentRepository.findByCentralId(centralId).ifPresent(student -> student.setFreeSemestersUsed(freeSemestersUsed));
    }

}
