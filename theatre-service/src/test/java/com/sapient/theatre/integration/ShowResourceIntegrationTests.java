package com.sapient.theatre.integration;

import com.sapient.theatre.data.domain.AbstractAuditingEntity;
import com.sapient.theatre.data.domain.Screen;
import com.sapient.theatre.data.domain.Show;
import com.sapient.theatre.data.domain.Theatre;
import com.sapient.theatre.data.repository.ScreenRepository;
import com.sapient.theatre.data.repository.ShowRepository;
import com.sapient.theatre.data.repository.TheatreRepository;
import com.sapient.theatre.dto.ShowInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class ShowResourceIntegrationTests {

    @Autowired
    ServletWebServerApplicationContext webServerAppCtxt;



    @Autowired
    private ShowRepository showRepository;
    @Autowired  private TheatreRepository theatreRepository;
    @Autowired  private ScreenRepository screenRepository;

    @Test
    public void testControllerFetchLogic() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        int port = webServerAppCtxt.getWebServer().getPort();
        ShowInformation[] showInformations= testRestTemplate.getForObject("http://localhost:" + port+"/v1/shows/KOLKATA/" + movie1 + "/2024-02-21", ShowInformation[].class);
        Assertions.assertNotNull(showInformations);
        Assertions.assertEquals(1, showInformations.length);
        Assertions.assertEquals("S1T1C1", showInformations[0].screenName());
        Assertions.assertEquals("Theatre # 1", showInformations[0].theatreName());


    }




    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

    private UUID movie1;
    private UUID movie2;

    /**
     * Prepare the static data setup as follows:
     *   CITY 1: KOLKATA
     *   THEATRE 1 :
     *      SCREEN 1 - MOVIE 1 * 1 shows   ( 21-02-2024 @ 14.00 )
     *      SCREEN 2 - MOVIE 2 * 2 shows  ( 22-02-2024 @ 14.00 (Not available) and 20.00)
     *      SCREEN 3 - MOVIE 1 * 1 shows  ( 23-02-2024 @ 18.00 )
     *   THEATRE 2 :
     *      SCREEN 1 - MOVIE 1 * 2 shows  ( 24-02-2024 @ 14.00 and 20.00)
     *   CITY 1: BANGALORE
     *    THEATRE 1 :
     *      SCREEN 1 - MOVIE 1 * 2 shows   ( 22-02-2024 @ 14.00 and 20.00)
     *      SCREEN 2 - MOVIE 2 * 2 shows    ( 23-02-2024 @ 14.00 and 20.00)
     *      SCREEN 2 - MOVIE 1 * 2 shows    ( 24-02-2024 @ 14.00 and 20.00)
     *    THEATRE 2 :
     *      SCREEN 1 - MOVIE 2 * 2 shows   ( 23-02-2024 @ 14.00 and 20.00)
     */

    @BeforeEach
    void setUp() {


        final Theatre theatre1C1 = saveTheatreEntity("KOLKATA");
        final Theatre theatre2C1 = saveTheatreEntity("KOLKATA");

        final Screen screen1T1C1 = saveScreenEntity(theatre1C1, "S1T1C1");
        final Screen screen2T1C1 = saveScreenEntity(theatre1C1, "S2T1C1");
        final Screen screen3T1C1 = saveScreenEntity(theatre1C1, "S3T1C1");
        final Screen screen1T2C1 = saveScreenEntity(theatre2C1, "S1T2C1");

        movie1 = UUID.randomUUID();
        movie2 = UUID.randomUUID();
        final Show show1S1T1C1 = saveShow(screen1T1C1, movie1, "20240221140000", "20240221");
        final Show show1S2T1C1 = saveShow(screen2T1C1, movie2, "20240222140000", "20240222", "N");
        final Show show2S2T1C1 = saveShow(screen2T1C1, movie2, "20240222200000", "20240222");
        final Show show1S3T1C1 = saveShow(screen3T1C1, movie1, "20240223180000", "20240223");
        final Show show2S3T1C1 = saveShow(screen3T1C1, movie1, "20240224180000", "20240224");
        final Show show1S3T2C1 = saveShow(screen1T2C1, movie1, "20240224200000", "20240224");


        final Theatre theatre1C2 = saveTheatreEntity("BENGALURU");
        final Theatre theatre2C2 = saveTheatreEntity("BENGALURU");

        final Screen screen1T1C2 = saveScreenEntity(theatre1C2, "S1T1C2");
        final Screen screen2T1C2 = saveScreenEntity(theatre1C2, "S2T1C2");
        final Screen screen3T1C2 = saveScreenEntity(theatre1C2, "S3T1C2");
        final Screen screen1T2C2 = saveScreenEntity(theatre2C2, "S1T2C2");


        final Show show1S1T1C2 = saveShow(screen1T1C2, movie1, "20240221140000", "20240221");
        final Show show2S1T1C2 = saveShow(screen1T1C2, movie1, "20240221200000", "20240221");
        final Show show1S2T1C2 = saveShow(screen2T1C2, movie2, "20240222140000", "20240222");
        final Show show2S2T1C2 = saveShow(screen2T1C2, movie2, "20240222200000", "20240222");
        final Show show1S3T1C2 = saveShow(screen3T1C2, movie1, "20240224140000", "20240224");
        final Show show2S3T2C2 = saveShow(screen1T2C2, movie1, "20240222200000", "20240222");
        final Show show2S3T1C2 = saveShow(screen3T1C2, movie1, "20240224200000", "20240224");
        final Show show1S3T2C2 = saveShow(screen1T2C2, movie1, "20240222140000", "20240222");

    }




    private void mockAudit(AbstractAuditingEntity auditingEntity) {
        auditingEntity.setCreatedBy("TEST");
        auditingEntity.setLastModifiedBy("TEST");
        auditingEntity.setCreatedDate(LocalDate.now());
        auditingEntity.setLastModifiedDate(LocalDate.now());
    }

    private Screen saveScreenEntity(Theatre theatre, String name) {
        var screen = new Screen();
        screen.setId(UUID.randomUUID());
        screen.setScreenType("SINGLE");
        screen.setTotalSeating(100);
        screen.setTheatre(theatre);
        screen.setName(name);
        mockAudit(screen);

        return screenRepository.save(screen);
    }

    private Theatre saveTheatreEntity(String city) {
        var theatre = new Theatre();
        theatre.setId(UUID.randomUUID());
        theatre.setCity(city);
        theatre.setCountry("IN");
        theatre.setName("Theatre # 1");
        mockAudit(theatre);

        return theatreRepository.save(theatre);
    }


    private Show saveShow(Screen screen, UUID movieId, String startTime, String playingOn){
        return saveShow(screen, movieId, startTime, playingOn, "Y");
    }
    private Show saveShow(Screen screen, UUID movieId, String startTime, String playingOn, String availability){



        var show  = new Show();
        show.setId(UUID.randomUUID());
        show.setMovieId(movieId);
        show.setAvailability(availability);
        show.setScreen(screen);
        show.setStartTime(LocalDateTime.parse(startTime, timeFormat));
        show.setPlayingOn(LocalDate.parse(playingOn, dateFormat));
        mockAudit(show);
        final Show save = showRepository.save(show);
        return save;
    }


    private LocalDateTime getTime(String time) {
        return LocalDateTime.parse(time, timeFormat);
    }
    private LocalDate getDate(String myDate) {
        return LocalDate.parse(myDate, dateFormat);
    }





}
