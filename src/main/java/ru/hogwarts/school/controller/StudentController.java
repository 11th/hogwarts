package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.create(student));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> read(@PathVariable Long id) {
        var student = studentService.read(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentService.read(id));
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> readAll(@RequestParam(name = "age", required = false) Integer age) {
        var students = studentService.readAll();
        if (age != null) {
            students = students.stream()
                    .filter(s -> s.getAge() == age)
                    .toList();
        }
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
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
        var student = studentService.read(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
