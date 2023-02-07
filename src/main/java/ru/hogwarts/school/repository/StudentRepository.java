package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Student;

import java.util.Collection;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAgeBetween(int from, int to);

    Collection<Student> findByFacultyId(long facultyId);

    @Query(value = "SELECT count(*) FROM student", nativeQuery = true)
    Integer getCount();

    @Query(value = "SELECT avg(age) FROM student", nativeQuery = true)
    Integer getAvgAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT ?1", nativeQuery = true)
    Collection<Student> getLastAmountOfStudents(int amount);
}
