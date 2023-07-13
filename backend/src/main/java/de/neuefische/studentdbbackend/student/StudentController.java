package de.neuefische.studentdbbackend.student;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentResponseDto> getAllStudents(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String courseOfStudies
    ) {
        if (firstName != null) {
            return studentService.getStudentsByFirstName(firstName);
        } else if (lastName != null) {
            return studentService.getStudentsByLastName(lastName);
        } else if (courseOfStudies != null) {
            return studentService.getStudentsByCourse(courseOfStudies);
        }
        return studentService.getAllStudents();
    }

    @GetMapping("2")
    public List<StudentResponseDto> getAllStudents2(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String courseOfStudies
    ) {
        if (firstName != null) {
            return studentService.getStudentsByFirstName(firstName);
        } else if (lastName != null) {
            return studentService.getStudentsByLastName(lastName);
        } else if (courseOfStudies != null) {
            return studentService.getStudentsByCourse(courseOfStudies);
        }
        return studentService.getAllStudents();
    }
//
//    @GetMapping("3")
//    public List<StudentResponseDto> getAllStudents3(
//            @RequestParam(required = false) String firstName,
//            @RequestParam(required = false) String lastName,
//            @RequestParam(required = false) String courseOfStudies
//    ) {
//        if (firstName != null) {
//            return studentService.getStudentsByFirstName(firstName);
//        } else if (lastName != null) {
//            return studentService.getStudentsByLastName(lastName);
//        } else if (courseOfStudies != null) {
//            return studentService.getStudentsByCourse(courseOfStudies);
//        }
//        return studentService.getAllStudents();
//    }

    @GetMapping("/search")
    public StudentResponseDto searchStudent(
            @RequestParam String firstName,
            @RequestParam String lastName
    ) {
        return studentService.getStudentByFirstNameAndLastName(firstName, lastName);
    }

    @GetMapping("/{matriculationNumber}")
    public StudentResponseDto getStudentByMatriculationNumber(@PathVariable String matriculationNumber) {
        return studentService.getStudentByMatriculationNumber(matriculationNumber);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDto addStudent(@Valid @RequestBody NewStudentDto studentRequestDto) {
        return studentService.addStudent(studentRequestDto);
    }

    @PutMapping("/{matriculationNumber}")
    public StudentResponseDto updateStudent(@PathVariable String matriculationNumber, @Valid @RequestBody NewStudentDto studentRequestDto) {
        return studentService.updateStudent(matriculationNumber, studentRequestDto);
    }

    @DeleteMapping("/{matriculationNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable String matriculationNumber) {
        studentService.deleteStudent(matriculationNumber);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStudentNotFoundException(StudentNotFoundException exception) {
        int a = 7;
        int b = -7;

        Map<String, Object> body = new LinkedHashMap<>();
        if (a+b == 0) {
            body = null;
        }
        body.put("error", exception.getMessage());
        body.put("timestamp", Instant.now().toString());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleValidationException(BindException ex) {
        Map<String, Object> responseBody = new HashMap<>();

        for (FieldError error : ex.getFieldErrors()) {
            responseBody.put(error.getField(), error.getDefaultMessage());
            responseBody.put("timestamp", Instant.now());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

}
