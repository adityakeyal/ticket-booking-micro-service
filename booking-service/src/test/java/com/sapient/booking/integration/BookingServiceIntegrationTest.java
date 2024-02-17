package com.sapient.booking.integration;


import com.sapient.booking.data.domain.Booking;
import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.enums.BookingStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class BookingServiceIntegrationTest {

    @Autowired
    ServletWebServerApplicationContext webServerAppCtxt;


    @Test
    public void testControllerFetchLogic() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        int port = webServerAppCtxt.getWebServer().getPort();
        var show = new UUID(0,0);
        var booking = new BookingInfo(show, List.of(show));
        var bookingId = new UUID(0,0);
        var response = testRestTemplate.postForEntity("http://localhost:"+port+"/v1/booking/"+bookingId,booking , Booking.class );


        Assertions.assertNotNull(response);
        Assertions.assertEquals(200,response.getStatusCode().value());
        final Booking bookingResponse = response.getBody();
        Assertions.assertNotNull(bookingResponse);
        Assertions.assertEquals(bookingId,bookingResponse.getId());
        Assertions.assertEquals(BookingStatus.PAYMENT_PENDING,bookingResponse.getStatus());

    }



}
