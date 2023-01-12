package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacultyService {
    private static long lastId;
    private final Map<Long, Faculty> faculties = new HashMap<>();

    public Faculty create(Faculty faculty) {
        faculty.setId(++lastId);
        return faculties.put(faculty.getId(), faculty);
    }

    public Faculty read(long id){
        return faculties.get(id);
    }

    public Collection<Faculty> readAll(){
        return faculties.values();
    }

    public Faculty update(Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())){
            return null;
        }
        faculties.put(faculty.getId(), faculty);
        return faculties.get(faculty.getId());
    }

    public Faculty delete(long id) {
        return faculties.remove(id);
    }
}
