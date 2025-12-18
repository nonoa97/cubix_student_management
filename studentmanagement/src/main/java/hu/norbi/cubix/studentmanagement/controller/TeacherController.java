package hu.norbi.cubix.studentmanagement.controller;

import hu.norbi.cubix.studentmanagement.api.TeacherControllerApi;
import hu.norbi.cubix.studentmanagement.api.model.TeacherDto;
import hu.norbi.cubix.studentmanagement.mapper.TeacherMapper;
import hu.norbi.cubix.studentmanagement.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TeacherController implements TeacherControllerApi {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final NativeWebRequest webRequest;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(webRequest);
    }

    @Override
    public ResponseEntity<TeacherDto> findById(Integer id) {
        return ResponseEntity.ok(teacherMapper.teacherToDto(teacherRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND))));
    }
}
