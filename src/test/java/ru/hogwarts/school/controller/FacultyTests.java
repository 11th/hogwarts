package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
class FacultyTests {
    private static final long ID = 1;
    private static final String NAME = "Name";
    private static final String COLOR = "Color";

    private final JSONObject facultyObject = new JSONObject();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @BeforeEach
    public void setUp() throws Exception {
        facultyObject.put("name", NAME);
        facultyObject.put("color", COLOR);

        Faculty faculty = new Faculty();
        faculty.setId(ID);
        faculty.setName(NAME);
        faculty.setColor(COLOR);

        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(faculty));
        when(facultyRepository.findAll())
                .thenReturn(List.of(faculty));
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(any(String.class), any(String.class)))
                .thenReturn(List.of(faculty));
        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(faculty);
    }

    @Test
    public void getById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR));
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].color").value(COLOR));
    }

    @Test
    public void getByAge() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/params?name=" + NAME + "&color=" + COLOR)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].color").value(COLOR));
    }

    @Test
    public void create() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR));
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.color").value(COLOR));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}