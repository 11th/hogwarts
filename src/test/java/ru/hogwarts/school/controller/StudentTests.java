package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentTests {
    private final static String HOST = "http://localhost:";

    private final long lastId = 302;

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void findAll() throws Exception {
        Assertions
                .assertThat(testRestTemplate.getForObject(HOST + port + "/student", String.class))
                .isNotNull();
    }

    @Test
    public void findById() throws Exception {
        Assertions
                .assertThat(testRestTemplate.getForObject(HOST + port + "/student/1", String.class))
                .isNotNull();
    }

    @Test
    public void findFaculty() throws Exception {
        Assertions
                .assertThat(testRestTemplate.getForObject(HOST + port + "/student/1/faculty", String.class))
                .isNotNull();
    }

    @Test
    public void findByAgeBetween() throws Exception {
        Assertions
                .assertThat(testRestTemplate.getForObject(HOST + port + "/student/params?ageFrom=18&ageTo=20", String.class))
                .isNotNull();
    }

    @Test
    @Disabled
    public void create() throws Exception {
        Student student = new Student();
        student.setName("test");
        student.setAge(18);

        ResponseEntity<Student> response = testRestTemplate.postForEntity(
                HOST + port + "/student",
                student,
                Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Disabled
    public void update() throws Exception {
        Student student = new Student();
        student.setId(lastId);
        student.setName("Иванова");
        student.setAge(33);

        ResponseEntity<Student> response = testRestTemplate.exchange(
                HOST + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(student),
                Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Disabled
    public void delete() throws Exception {
        ResponseEntity<Void> response = testRestTemplate.exchange(
                HOST + port + "/student/" + lastId,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}