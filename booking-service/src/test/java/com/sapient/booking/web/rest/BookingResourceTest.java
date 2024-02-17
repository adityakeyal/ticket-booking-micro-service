package com.sapient.booking.web.rest;

import com.google.gson.Gson;
import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@WebMvcTest(BookingResource.class)
class BookingResourceTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean private BookingService bookingService;

    @Test
    public void testControllerInvalidInput() throws Exception {

        Gson gson = new Gson();

        final String json = gson.toJson(new BookingInfo(new UUID(0,0), null));
        this.mockMvc.
                perform(MockMvcRequestBuilders
                        .post("/v1/booking/11111111-1111-1111-1111-111111111111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .locale(new Locale("en"))
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(422))
                ;

            Mockito.verify(this.bookingService, Mockito.never()).booking(Mockito.any(),Mockito.any());

    }

}
