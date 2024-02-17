package com.sapient.booking.web.rest;

import com.google.gson.Gson;
import com.sapient.booking.data.domain.Booking;
import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.enums.BookingStatus;
import com.sapient.booking.exception.UnableToLockSeatException;
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

        final String json = gson.toJson(new BookingInfo(null, null));
        this.mockMvc.
                perform(MockMvcRequestBuilders
                        .post("/v1/booking/11111111-1111-1111-1111-111111111111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .locale(new Locale("en"))
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().is(422))
                ;

            Mockito.verify(this.bookingService, Mockito.never()).booking(Mockito.any(),Mockito.any());

    }


    @Test
    public void testControllerInsufficientData() throws Exception {

        Gson gson = new Gson();

        final String json = gson.toJson(new BookingInfo(UUID.randomUUID(), List.of()));



        Mockito.when(this.bookingService.booking(Mockito.any(),Mockito.any())).thenThrow(new UnableToLockSeatException("UnableToLock.insufficientInformation"));

        this.mockMvc.
                perform(MockMvcRequestBuilders
                        .post("/v1/booking/11111111-1111-1111-1111-111111111111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .locale(new Locale("en"))
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().is(422))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("UnableToLock.insufficientInformation"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Cannot perform booking. Insufficient information provided"));
    }
    @Test
    public void testControllerIncorrectData() throws Exception {

        Gson gson = new Gson();

        final String json = gson.toJson(new BookingInfo(UUID.randomUUID(), List.of()));



        Mockito.when(this.bookingService.booking(Mockito.any(),Mockito.any())).thenThrow(new UnableToLockSeatException("UnableToLock.incorrectData"));

        this.mockMvc.
                perform(MockMvcRequestBuilders
                        .post("/v1/booking/11111111-1111-1111-1111-111111111111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .locale(new Locale("en"))
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().is(422))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("UnableToLock.incorrectData"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Incorrect combination of seats provided"));
    }
    @Test
    public void testControllerInsufficientStaleData() throws Exception {

        Gson gson = new Gson();

        final String json = gson.toJson(new BookingInfo(UUID.randomUUID(), List.of()));



        Mockito.when(this.bookingService.booking(Mockito.any(),Mockito.any())).thenThrow(new UnableToLockSeatException("UnableToLock.staleData"));

        this.mockMvc.
                perform(MockMvcRequestBuilders
                        .post("/v1/booking/11111111-1111-1111-1111-111111111111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .locale(new Locale("en"))
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().is(422))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("UnableToLock.staleData"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("The seats you are trying to book may not be available. Please refresh and try again."));
    }



    @Test
    public void testControllerSuccess() throws Exception {

        Gson gson = new Gson();

        final String json = gson.toJson(new BookingInfo(UUID.randomUUID(), List.of()));

        Booking booking = new Booking(); booking.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        booking.setStatus(BookingStatus.CONFIRMED);

        Mockito.when(this.bookingService.booking(Mockito.any(),Mockito.any())).thenReturn(booking);

        this.mockMvc.
                perform(MockMvcRequestBuilders
                        .post("/v1/booking/11111111-1111-1111-1111-111111111111")
                        .contentType(MediaType.APPLICATION_JSON)
                        .locale(new Locale("en"))
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("11111111-1111-1111-1111-111111111111"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CONFIRMED"));


    }

}
