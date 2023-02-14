package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student findById(long id) {
        logger.info("Was invoked method to get student by id {}", id);
        var studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            logger.warn("Student {} not found", id);
            return null;
        }
        return studentOptional.get();
    }

    public Collection<Student> findAll() {
        logger.info("Was invoked method to get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween(int from, int to) {
        logger.info("Was invoked method to get students with age from {} to {}", from, to);
        return studentRepository.findByAgeBetween(from, to);
    }

    public Integer getCount() {
        logger.info("Was invoked method to get count of students");
        return studentRepository.getCount();
    }

    public Integer getAvgAge() {
        logger.info("Was invoked method to get average age of students");
        return (int) studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
        //return studentRepository.getAvgAge();
    }

    public Collection<Student> getLastAmountOfStudents(int amount) {
        logger.info("Was invoked method to get last {} students", amount);
        return studentRepository.getLastAmountOfStudents(amount);
    }

    public Collection<String> getNames(String startsWith) {
        logger.info("Was invoked method to get names of students");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(n -> n.startsWith(startsWith.toUpperCase()))
                .sorted()
                .toList();
    }

    public Student create(Student student) {
        logger.info("Was invoked method to create student {}", student);
        return studentRepository.save(student);
    }

    public Student update(Student student) {
        logger.info("Was invoked method to update student {}", student.getId());
        return studentRepository.save(student);
    }

    public void delete(long id) {
        logger.info("Was invoked method to delete student {}", id);
        studentRepository.deleteById(id);
    }

    public Avatar findAvatar(long studentId) {
        logger.info("Was invoked method to get student's {} avatar", studentId);
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    public void uploadAvatar(long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method to upload avatar to student {}", studentId);
        var student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            Path filePath = Path.of(avatarsDir,
                    student.get().getId() + "." + getFileExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (InputStream is = avatarFile.getInputStream();
                 OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedInputStream bis = new BufferedInputStream(is, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            ) {
                bis.transferTo(bos);
            }
            Avatar avatar = findAvatar(studentId);
            avatar.setStudent(student.get());
            avatar.setFilePath(filePath.toString());
            avatar.setFileSize(avatarFile.getSize());
            avatar.setMediaType(avatarFile.getContentType());
            avatar.setData(avatarFile.getBytes());
            avatarRepository.save(avatar);
        }
    }

    private String getFileExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
