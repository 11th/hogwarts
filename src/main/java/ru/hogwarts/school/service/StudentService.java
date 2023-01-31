package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
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

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Student findById(long id) {
        var studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            return null;
        }
        return studentOptional.get();
    }

    public Collection<Student> findAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween(int from, int to) {
        return studentRepository.findByAgeBetween(from, to);
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student update(Student student) {
        return studentRepository.save(student);
    }

    public void delete(long id) {
        studentRepository.deleteById(id);
    }

    public Avatar findAvatar(long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    public void uploadAvatar(long studentId, MultipartFile avatarFile) throws IOException {
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
