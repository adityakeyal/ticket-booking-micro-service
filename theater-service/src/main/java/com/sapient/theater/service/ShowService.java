package com.sapient.theater.service;

import com.sapient.theater.data.repository.ShowRepository;
import com.sapient.theater.dto.ShowInformation;
import com.sapient.theater.exception.NoRecordFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing show related information.
 */
@Service
@Slf4j
public class ShowService {

    private ShowRepository showRepository;

    public ShowService(@Autowired ShowRepository showRepository) {
        this.showRepository = showRepository;
    }


    /**
     * Retrieve the shows running for a movie based on the below parameters:
     * @param city - The city
     * @param movieId - The movie being played
     * @param showPlayedOn - The date on which the show is being played
     * @return - List of {@link ShowInformation}
     * @throws {@link NoRecordFoundException}- If no movies are showing it throws an exception
     */
    public List<ShowInformation> fetchShowsForMovie(String city, String movieId, LocalDate showPlayedOn) throws NoRecordFoundException {

        final Optional<List<ShowInformation>> availableShows = this.showRepository.findShowsByCityByMoviePlayingOn(city, UUID.fromString(movieId), showPlayedOn);

        if(availableShows.isEmpty()){
            log.debug("No shows available for ", city, movieId, showPlayedOn);
            throw new NoRecordFoundException();
        }

        return availableShows.get();

    }







}
