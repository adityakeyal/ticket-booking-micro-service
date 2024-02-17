package com.sapient.theater.web.rest;

import com.sapient.theater.dto.ShowInformation;
import com.sapient.theater.exception.NoRecordFoundException;
import com.sapient.theater.service.ShowService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


@WebMvcTest(ShowResource.class)
class ShowResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShowService showService;

    @Test
    public void testControllerSuccess() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate parse = LocalDate.parse("2024-02-17", formatter);
        final List<ShowInformation> showInformations = List.of(new ShowInformation("Theater 1", "Screen 1", LocalDateTime.now()), new ShowInformation("Theater 1", "Screen 2", LocalDateTime.now()));

        Mockito.when(this.showService.fetchShowsForMovie("KOLKATA", "11111111-1111-1111-1111-111111111111", parse)).thenReturn(showInformations);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/shows/KOLKATA/11111111-1111-1111-1111-111111111111/2024-02-17")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    public void testControllerNotFound() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate parse = LocalDate.parse("2024-02-17", formatter);
        Mockito.when(this.showService.fetchShowsForMovie("KOLKATA", "11111111-1111-1111-1111-111111111111", parse)).thenThrow(new NoRecordFoundException());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/shows/KOLKATA/11111111-1111-1111-1111-111111111111/2024-02-17")).andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(MockMvcResultMatchers.content().string("No Records Retrieved"));

    }


    @Test
    public void testControllerNotFoundInternationalization() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate parse = LocalDate.parse("2024-02-17", formatter);
        Mockito.when(this.showService.fetchShowsForMovie("KOLKATA", "11111111-1111-1111-1111-111111111111", parse)).thenThrow(new NoRecordFoundException());
        this.mockMvc.
                perform(MockMvcRequestBuilders
                        .get("/v1/shows/KOLKATA/11111111-1111-1111-1111-111111111111/2024-02-17")
                        .locale(new Locale("fr"))
                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Aucun enregistrement récupéré"));
    }

}