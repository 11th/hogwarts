package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> findAll() {
        var faculties = facultyService.findAll();
        if (faculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> findById(@PathVariable Long id) {
        var faculty = facultyService.findById(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyService.findById(id));
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<Collection<Student>> findStudents(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.findById(id).getStudents());
    }

    @GetMapping("/params")
    public ResponseEntity<Collection<Faculty>> findByParams(@RequestParam(required = false) String name,
                                                            @RequestParam(required = false) String color) {
        return ResponseEntity.ok(facultyService.findByNameOrColor(name, color));
    }

    @PostMapping
    public ResponseEntity<Faculty> create(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.create(faculty));
    }

    @PutMapping
    public ResponseEntity<Faculty> update(@RequestBody Faculty faculty) {
        var updFaculty = facultyService.update(faculty);
        if (updFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updFaculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> delete(@PathVariable Long id) {
        var faculty = facultyService.findById(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        facultyService.delete(id);
        return ResponseEntity.ok().build();
    }
}
