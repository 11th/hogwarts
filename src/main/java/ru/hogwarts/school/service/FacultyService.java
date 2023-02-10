package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty findById(long id) {
        logger.info("Was invoked method to get faculty by id {}", id);
        var facultyOptional = facultyRepository.findById(id);
        if (facultyOptional.isEmpty()) {
            logger.warn("Faculty {} not found", id);
            return null;
        }
        return facultyOptional.get();
    }

    public Collection<Faculty> findAll() {
        logger.info("Was invoked method to get all faculties");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByNameOrColor(String name, String color) {
        logger.info("Was invoked method to get faculty by name {} or color {}", name, color);
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Collection<Student> findStudents(long id) {
        logger.info("Was invoked method to get students for faculty {}", id);
        return studentRepository.findByFacultyId(id);
    }

    public Faculty create(Faculty faculty) {
        logger.info("Was invoked method to create faculty {}", faculty);
        return facultyRepository.save(faculty);
    }

    public Faculty update(Faculty faculty) {
        logger.info("Was invoked method to update faculty {}", faculty.getId());
        return facultyRepository.save(faculty);
    }

    public void delete(long id) {
        logger.info("Was invoked method to delete faculty {}", id);
        facultyRepository.deleteById(id);
    }
}
