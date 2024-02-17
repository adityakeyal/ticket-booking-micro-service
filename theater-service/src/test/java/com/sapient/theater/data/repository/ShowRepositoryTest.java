package com.sapient.theater.data.repository;

import com.sapient.theater.data.domain.AbstractAuditingEntity;
import com.sapient.theater.data.domain.Screen;
import com.sapient.theater.data.domain.Show;
import com.sapient.theater.data.domain.Theater;
import com.sapient.theater.dto.ShowInformation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Database Test for {@link ShowRepository}
 */
@RunWith(SpringRunner.class)
@DataJpaTest
class ShowRepositoryTest {

    @Autowired  private ShowRepository showRepository;
    @Autowired  private TheaterRepository theaterRepository;
    @Autowired  private ScreenRepository screenRepository;

    private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

    private UUID movie1;
    private UUID movie2;

    /**
     * Prepare the static data setup as follows:
     *   CITY 1: KOLKATA
     *   THEATER 1 :
     *      SCREEN 1 - MOVIE 1 * 1 shows   ( 21-02-2024 @ 14.00 )
     *      SCREEN 2 - MOVIE 2 * 2 shows  ( 22-02-2024 @ 14.00 (Not available) and 20.00)
     *      SCREEN 3 - MOVIE 1 * 1 shows  ( 23-02-2024 @ 18.00 )
     *   THEATER 2 :
     *      SCREEN 1 - MOVIE 1 * 2 shows  ( 24-02-2024 @ 14.00 and 20.00)
     *   CITY 1: BANGALORE
     *    THEATER 1 :
     *      SCREEN 1 - MOVIE 1 * 2 shows   ( 22-02-2024 @ 14.00 and 20.00)
     *      SCREEN 2 - MOVIE 2 * 2 shows    ( 23-02-2024 @ 14.00 and 20.00)
     *      SCREEN 2 - MOVIE 1 * 2 shows    ( 24-02-2024 @ 14.00 and 20.00)
     *    THEATER 2 :
     *      SCREEN 1 - MOVIE 2 * 2 shows   ( 23-02-2024 @ 14.00 and 20.00)
     */

    @BeforeEach
    void setUp() {

        final Theater theater1C1 = saveTheaterEntity("KOLKATA");
        final Theater theater2C1 = saveTheaterEntity("KOLKATA");

        final Screen screen1T1C1 = saveScreenEntity(theater1C1);
        final Screen screen2T1C1 = saveScreenEntity(theater1C1);
        final Screen screen3T1C1 = saveScreenEntity(theater1C1);
        final Screen screen1T2C1 = saveScreenEntity(theater2C1);

        movie1 = UUID.randomUUID();
        movie2 = UUID.randomUUID();
        final Show show1S1T1C1 = saveShow(screen1T1C1, movie1, "20240221140000", "20240221");
        final Show show1S2T1C1 = saveShow(screen2T1C1, movie2, "20240222140000", "20240222", "N");
        final Show show2S2T1C1 = saveShow(screen2T1C1, movie2, "20240222200000", "20240222");
        final Show show1S3T1C1 = saveShow(screen3T1C1, movie1, "20240223180000", "20240223");
        final Show show2S3T1C1 = saveShow(screen3T1C1, movie1, "20240224180000", "20240224");
        final Show show1S3T2C1 = saveShow(screen1T2C1, movie1, "20240224200000", "20240224");


        final Theater theater1C2 = saveTheaterEntity("BENGALURU");
        final Theater theater2C2 = saveTheaterEntity("BENGALURU");

        final Screen screen1T1C2 = saveScreenEntity(theater1C2);
        final Screen screen2T1C2 = saveScreenEntity(theater1C2);
        final Screen screen3T1C2 = saveScreenEntity(theater1C2);
        final Screen screen1T2C2 = saveScreenEntity(theater2C2);


        final Show show1S1T1C2 = saveShow(screen1T1C2, movie1, "20240221140000", "20240221");
        final Show show2S1T1C2 = saveShow(screen1T1C2, movie1, "20240221200000", "20240221");
        final Show show1S2T1C2 = saveShow(screen2T1C2, movie2, "20240222140000", "20240222");
        final Show show2S2T1C2 = saveShow(screen2T1C2, movie2, "20240222200000", "20240222");
        final Show show1S3T1C2 = saveShow(screen3T1C2, movie1, "20240224140000", "20240224");
        final Show show2S3T2C2 = saveShow(screen1T2C2, movie1, "20240222200000", "20240222");
        final Show show2S3T1C2 = saveShow(screen3T1C2, movie1, "20240224200000", "20240224");
        final Show show1S3T2C2 = saveShow(screen1T2C2, movie1, "20240222140000", "20240222");

    }

    @AfterEach
    void tearDown() {
    }



    @Test
    void findShowsByCityByMovie1(){
        final Optional<List<ShowInformation>> city = showRepository.findShowsByCityByMoviePlayingOn("KOLKATA", movie1,getDate("20240223"));
        final List<ShowInformation> cityShows = city.orElseThrow();
        Assertions.assertNotNull(cityShows);
        Assertions.assertEquals(1,cityShows.size());
    }

    @Test
    void findShowsByCityByMovie1AnotherDate(){
        final Optional<List<ShowInformation>> city = showRepository.findShowsByCityByMoviePlayingOn("KOLKATA", movie1,getDate("20240224"));
        final List<ShowInformation> cityShows = city.orElseThrow();
        Assertions.assertNotNull(cityShows);
        Assertions.assertEquals(2,cityShows.size());
    }


    @Test
    void findShowsByAnotherCityByMovie2(){
        final Optional<List<ShowInformation>> city = showRepository.findShowsByCityByMoviePlayingOn("BENGALURU", movie2,getDate("20240222"));
        final List<ShowInformation> cityShows = city.orElseThrow();
        Assertions.assertNotNull(cityShows);
        Assertions.assertEquals(2,cityShows.size());
    }


    @Test
    void findShowsNoShowsOnDate(){
        final Optional<List<ShowInformation>> city = showRepository.findShowsByCityByMoviePlayingOn("BENGALURU", movie1,getDate("20240227"));
        final List<ShowInformation> cityShows = city.orElseThrow();
        Assertions.assertNotNull(cityShows);
        Assertions.assertEquals(0,cityShows.size());
    }

    @Test
    void findShowsWithLimitedAvailability(){
        final Optional<List<ShowInformation>> city = showRepository.findShowsByCityByMoviePlayingOn("KOLKATA", movie2,getDate("20240222"));
        final List<ShowInformation> cityShows = city.orElseThrow();
        Assertions.assertNotNull(cityShows);
        Assertions.assertEquals(1,cityShows.size());
    }



    private void mockAudit(AbstractAuditingEntity auditingEntity) {
        auditingEntity.setCreatedBy("TEST");
        auditingEntity.setLastModifiedBy("TEST");
        auditingEntity.setCreatedDate(LocalDate.now());
        auditingEntity.setLastModifiedDate(LocalDate.now());
    }

    private Screen saveScreenEntity(Theater theater) {
        var screen = new Screen();
        screen.setId(UUID.randomUUID());
        screen.setScreenType("SINGLE");
        screen.setTotalSeating(100);
        screen.setTheater(theater);
        mockAudit(screen);

        return screenRepository.save(screen);
    }

    private Theater saveTheaterEntity(String city) {
        var theater = new Theater();
        theater.setId(UUID.randomUUID());
        theater.setCity(city);
        theater.setCountry("IN");
        theater.setName("Theater # 1");
        mockAudit(theater);

        return theaterRepository.save(theater);
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