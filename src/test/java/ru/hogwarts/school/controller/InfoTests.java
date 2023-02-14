package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.service.InfoService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InfoController.class)
public class InfoTests {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private InfoService infoService;

    @InjectMocks
    private InfoController infoController;

    @Test
    public void getSumOfNumbers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/sum-of-numbers?num=10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(55));
    }
}
