package com.sapient.theater.web.rest;


import com.sapient.theater.dto.ShowInformation;
import com.sapient.theater.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

/**
 * Rest Controller for managing {@link com.sapient.theater.data.domain.Show} domain
 */
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ShowResource {


    private final ShowService showService;



    /**
     * {@code GET  /shows/:city/:movieId/:playingOn} : get the shows for :movieId on :playingOn in :city
     *
     * @param city
     * @param movieId
     * @param playingOn
     * @return the {@link ShowInformation} with status {@code 200 (OK)} and with body the list of shows, or with status {@code 404 (Not Found)}
     */
    @GetMapping(value = "/shows/{city}/{movieId}/{playingOn}")
    public List<ShowInformation> getShowInformation(@PathVariable String  city,@PathVariable String movieId,@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate playingOn) throws Exception {
        return this.showService.fetchShowsForMovie(city, movieId, playingOn);
    }








}
