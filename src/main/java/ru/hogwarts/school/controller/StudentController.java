package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> findAll() {
        var students = studentService.findAll();
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable Long id) {
        var student = studentService.findById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> findFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id).getFaculty());
    }

    @GetMapping("/params")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam Integer ageFrom,
                                                                @RequestParam Integer ageTo) {
        return ResponseEntity.ok(studentService.findByAgeBetween(ageFrom, ageTo));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getCount() {
        return ResponseEntity.ok(studentService.getCount());
    }

    @GetMapping("/average-age")
    public ResponseEntity<Integer> getAvgAge() {
        return ResponseEntity.ok(studentService.getAvgAge());
    }

    @GetMapping("/last-{amount}-students")
    public ResponseEntity<Collection<Student>> getLastAmountOfStudents(@PathVariable Integer amount) {
        return ResponseEntity.ok(studentService.getLastAmountOfStudents(amount));
    }

    @GetMapping("/names")
    public ResponseEntity<Collection<String>> getNames(@RequestParam("startsWith") String startsWith) {
        return ResponseEntity.ok(studentService.getNames(startsWith));
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.create(student));
    }

    @PutMapping
    public ResponseEntity<Student> update(@RequestBody Student student) {
        var updStudent = studentService.update(student);
        if (updStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> delete(@PathVariable Long id) {
        var student = studentService.findById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        studentService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}/avatar/preview")
    public ResponseEntity<byte[]> findPreview(@PathVariable Long id) {
        Avatar avatar = studentService.findAvatar(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping("/{id}/avatar")
    public void findAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = studentService.findAvatar(id);
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable("id") Long id,
                                               @RequestParam("avatar") MultipartFile avatar) throws IOException {
        studentService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping("students-to-console")
    public ResponseEntity<String> writeStudentsToConsole() {
        studentService.writeStudentsToConsole();
        return ResponseEntity.ok("done!");
    }

    @GetMapping("students-to-console-sync")
    public ResponseEntity<String> writeStudentsToConsoleSync() {
        studentService.writeStudentsToConsoleSync();
        return ResponseEntity.ok("done!");
    }
}
