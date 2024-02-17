package com.sapient.theatre.web.rest;


import com.sapient.theatre.dto.ShowInformation;
import com.sapient.theatre.service.ShowService;
import com.sapient.theatre.data.domain.Show;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

/**
 * Rest Controller for managing {@link Show} domain
 */
@Tag(name = "Show API", description = "API related to shows")
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
    @Operation(
            summary = "Get shows for a movie running in a city on a particular date",
            description = "gets the shows for a movie running in a selected city for a particular date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "invalid body content" ,  content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "invalid body content" ,  content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))})
    })
    @GetMapping(value = "/shows/{city}/{movieId}/{playingOn}")
    public List<ShowInformation> getShowInformation(@PathVariable String  city,@PathVariable String movieId,@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate playingOn) throws Exception {
        return this.showService.fetchShowsForMovie(city, movieId, playingOn);
    }








}
