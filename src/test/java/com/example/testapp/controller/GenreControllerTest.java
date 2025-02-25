package com.example.testapp.controller;

import com.example.testapp.DTO.GenreDTO;
import com.example.testapp.service.impl.GenreServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GenreController.class)
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GenreServiceImpl genreService;

    @InjectMocks
    private GenreController genreController;

    @Test
    @WithMockUser
    void addGenre_Success() throws Exception {
        GenreDTO genreDTO = new GenreDTO();

        when(genreService.addGenre(any(GenreDTO.class))).thenReturn(genreDTO);

        mockMvc.perform(post("/api/v1/genres/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void getAllGenres_Success() throws Exception {
        List<GenreDTO> genreDTOList = List.of(
                new GenreDTO("Novel"),
                new GenreDTO("Poem")
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(genreDTOList);

        when(genreService.getAllGenres()).thenReturn(genreDTOList);

        mockMvc.perform(get("/api/v1/genres/getAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @WithMockUser
    void getGenreById() throws Exception {
        long genreId = 1L;
        GenreDTO genres = new GenreDTO(genreId);

        when(genreService.getGenreById(genreId)).thenReturn(genres);

        mockMvc.perform(get("/api/v1/genres/get/id/{id}", genreId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateGenreById() throws Exception {
        long genreId = 1L;
        GenreDTO genreDTO = new GenreDTO(genreId);

        when(genreService.updateGenreById(genreId, genreDTO)).thenReturn(genreDTO);
        when(genreService.getGenreById(genreId)).thenReturn(genreDTO);

        mockMvc.perform(put("/api/v1/genres/update/allFields/{id}", genreId)
                        .with(csrf())
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateGenre() throws Exception {
        long genreId = 1L;
        GenreDTO genreDTO = new GenreDTO(genreId);
        genreDTO.setName("Old Genre");
        Map<String, Object> map = new HashMap<>();
        map.put("name", "New Genre");

        when(genreService.updateGenreFields(eq(genreId), anyMap())).thenAnswer(invocation -> {
            genreDTO.setName("New Genre");
            return genreDTO;
        });
        when(genreService.getGenreById(genreId)).thenReturn(genreDTO);

        mockMvc.perform(patch("/api/v1/genres/update/{id}", genreId)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(map))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Genre"));
        verify(genreService,times(1)).updateGenreFields(eq(genreId), anyMap());
    }

    @Test
    @WithMockUser
    void deleteGenreById() throws Exception {
        long genreId = 1L;
        GenreDTO genreDTO = new GenreDTO(genreId);

        doNothing().when(genreService).deleteGenreById(genreId);
        when(genreService.getGenreById(genreId)).thenReturn(genreDTO);

        mockMvc.perform(delete("/api/v1/genres/delete/{id}", genreId)
                        .with(csrf()))
                .andExpect(status().isGone());
    }

    @Test
    @WithMockUser
    void getGenreByName() throws Exception {
        String name = "Novel";
        GenreDTO genreDTO = new GenreDTO(name);

        when(genreService.getGenreByName(name)).thenReturn(genreDTO);

        mockMvc.perform(get("/api/v1/genres/get/name/{name}", name)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void sortGenresByPopularityDescending() throws Exception {
        GenreDTO genreDTO = new GenreDTO();
        GenreDTO genreDTO1 = new GenreDTO();
        genreDTO1.setCountOfBorrowingBookWithGenre(10L);
        genreDTO.setCountOfBorrowingBookWithGenre(2L);
        List<GenreDTO> genreDTOList = List.of(genreDTO1, genreDTO);

        when(genreService.getMostPopularGenres()).thenReturn(genreDTOList);

        mockMvc.perform(get("/api/v1/genres/sort/popularityDesc")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }
}
