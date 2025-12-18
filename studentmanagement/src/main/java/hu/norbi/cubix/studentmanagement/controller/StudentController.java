package hu.norbi.cubix.studentmanagement.controller;

import hu.norbi.cubix.studentmanagement.api.StudentControllerApi;
import hu.norbi.cubix.studentmanagement.api.model.StudentDto;
import hu.norbi.cubix.studentmanagement.mapper.StudentMapper;
import hu.norbi.cubix.studentmanagement.repository.StudentRepository;
import hu.norbi.cubix.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StudentController implements StudentControllerApi {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final NativeWebRequest webRequest;
    private final StudentService studentService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(webRequest);
    }

    @Override
    public ResponseEntity<StudentDto> findById1(Integer id) {
        return ResponseEntity.ok(studentMapper.studentToStudentDto(studentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND))));
    }

    @Override
    public ResponseEntity<Resource> getProfilePicture(Integer id) {
        return ResponseEntity.ok(studentService.getProfilePicture(id));
    }

    @Override
    public ResponseEntity<Void> uploadProfilePicture(Integer id, MultipartFile content) {
        try{
            studentService.saveProfilePicture(id, content.getInputStream());
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }


}
