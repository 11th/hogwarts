package ru.hogwarts.school.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private static long lastId;
    private final Map<Long, Student> students = new HashMap<>();

    public Student create(Student student) {
        student.setId(++lastId);
        return students.put(student.getId(), student);
    }

    public Student read(long id){
        return students.get(id);
    }

    public Collection<Student> readAll(){
        return students.values();
    }

    public Student update(Student student) {
        if (!students.containsKey(student.getId())){
            return null;
        }
        students.put(student.getId(), student);
        return students.get(student.getId());
    }

    public Student delete(long id) {
        return students.remove(id);
    }
}
