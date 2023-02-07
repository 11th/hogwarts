package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentTests {
    private final static String HOST = "http://localhost:";

    private Student studentGet = new Student();
    private Student studentPut = new Student();
    private Student studentDel = new Student();
    private final Faculty faculty = new Faculty();

    @LocalServerPort
    private int port;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private FacultyRepository facultyRepository;

    @BeforeEach
    public void setUp() {
        faculty.setName("faculty");
        faculty.setColor("color");

        studentGet.setName("get");
        studentGet.setAge(22);
        studentGet.setFaculty(faculty);
        studentGet = studentRepository.save(studentGet);

        studentPut.setName("update");
        studentPut.setAge(22);
        studentPut = studentRepository.save(studentPut);

        studentDel.setName("delete");
        studentDel.setAge(22);
        studentDel = studentRepository.save(studentDel);
    }

    @AfterEach
    public void tearDown() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    public void findAll() throws Exception {
        var response = testRestTemplate.getForObject(HOST + port + "/student", List.class);
        Assertions.assertThat(response.size()).isEqualTo(3);
        //ResponseEntity<Student[]> response = testRestTemplate.getForEntity(HOST + port + "/student", Student[].class);
        //Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        //Assertions.assertThat(response.getBody()).hasSize(3);
    }

    @Test
    public void findById() throws Exception {
        Student student = testRestTemplate.getForObject(HOST + port + "/student/" + studentGet.getId(), Student.class);
        Assertions.assertThat(student.getId()).isEqualTo(studentGet.getId());
    }

    @Test
    public void findFaculty() throws Exception {
        Faculty facultyActual = testRestTemplate.getForObject(HOST + port + "/student/" + studentGet.getId() + "/faculty", Faculty.class);
        Assertions.assertThat(facultyActual.getName()).isEqualTo(faculty.getName());
        Assertions.assertThat(facultyActual.getColor()).isEqualTo(faculty.getColor());
    }

    @Test
    public void findByAgeBetween() throws Exception {
        var response = testRestTemplate.getForObject(HOST + port + "/student/params?ageFrom=22&ageTo=22", List.class);
        Assertions.assertThat(response.size()).isEqualTo(3);
    }

    @Test
    public void getCount() throws Exception {
        Integer response = testRestTemplate.getForObject(HOST + port + "/student/count", Integer.class);
        Assertions.assertThat(response).isEqualTo(3);
    }

    @Test
    public void getAvgAge() throws Exception {
        Integer response = testRestTemplate.getForObject(HOST + port + "/student/average-age", Integer.class);
        Assertions.assertThat(response).isEqualTo(22);
    }

    @Test
    public void getLastAmountOfStudents() throws Exception {
        var response = testRestTemplate.getForObject(HOST + port + "/student/last-1-students", List.class);
        Assertions.assertThat(response.size()).isEqualTo(1);
    }

    @Test
    public void create() throws Exception {
        Student student = new Student();
        student.setName("post");
        student.setAge(18);

        ResponseEntity<Student> response = testRestTemplate.postForEntity(
                HOST + port + "/student",
                student,
                Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo("post");
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(18);
    }

    @Test
    public void update() throws Exception {
        studentPut.setName("updated");

        ResponseEntity<Student> response = testRestTemplate.exchange(
                HOST + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(studentPut),
                Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getName()).isEqualTo(studentPut.getName());
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(studentPut.getAge());
    }

    @Test
    public void delete() throws Exception {
        ResponseEntity<Void> response = testRestTemplate.exchange(
                HOST + port + "/student/" + studentDel.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void uploadAvatar() throws Exception {
        Resource resource = new ClassPathResource("avatar/avatarForTests.webp");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("avatar", resource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                HOST + port + "/student/1/avatar",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void loadAvatar() throws Exception {
        uploadAvatar();
        String response = testRestTemplate.getForObject(HOST + port + "/student/1/avatar/preview", String.class);
        Assertions.assertThat(response).isNotNull();
    }
}