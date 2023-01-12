package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> create(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.create(faculty));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> read(@PathVariable Long id) {
        var faculty = facultyService.read(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyService.read(id));
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> readAll(@RequestParam(name = "color", required = false) String color) {
        var faculties = facultyService.readAll();
        if (color != null){
            faculties = faculties.stream()
                    .filter(f -> f.getColor().equals(color))
                    .toList();
        }
        if (faculties.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculties);
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
        var faculty = facultyService.read(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyService.delete(id));
    }
}
