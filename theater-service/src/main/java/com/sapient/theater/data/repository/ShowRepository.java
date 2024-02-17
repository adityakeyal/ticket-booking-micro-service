package com.sapient.theater.data.repository;

import com.sapient.theater.data.domain.Show;
import com.sapient.theater.dto.ShowInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link Show} entity
 */
@Repository
public interface ShowRepository extends JpaRepository<Show, UUID> {


    /**
     * Fetch all shows which belong to a city are screening a particular movie,
     * and have a specified showing date and has availability
     *
     * @param city
     * @param movieId
     * @param showDate
     * @return
     */
    @Query("select new com.sapient.theater.dto.ShowInformation(s.screen.theater.name, s.screen.name, s.startTime) from Show s where s.screen.theater.city = :city and s.playingOn=:showDate and s.movieId=:movieId and s.availability='Y' " )
    Optional<List<ShowInformation>> findShowsByCityByMoviePlayingOn(String city, UUID movieId, LocalDate showDate);

}
