package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty findById(long id) {
        var facultyOptional = facultyRepository.findById(id);
        if (facultyOptional.isEmpty()) {
            return null;
        }
        return facultyOptional.get();
    }

    public Collection<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByNameOrColor(String name, String color) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty update(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void delete(long id) {
        facultyRepository.deleteById(id);
    }
}
