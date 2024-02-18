package com.sapient.booking.service;

import com.sapient.booking.data.domain.Booking;
import com.sapient.booking.data.repository.BookingRepository;
import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.dto.ShowInformation;
import com.sapient.booking.exception.MultipleRecordsFoundException;
import com.sapient.booking.exception.NoRecordFoundException;
import com.sapient.booking.service.booking.strategy.BookingStrategy;
import com.sapient.booking.service.external.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Service for {@link Booking} domain
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;


    private final  ShowService showService;


    private final List<BookingStrategy> bookingStrategies;


    /**
     * Booking operation.
     * The steps are as follows:
     * <ol>
     * <li> Validate that the request is valid </li>
     * <li> Validate that the required seats are available  </li>
     * <li> Update the seat information to blocked  </li>
     * <li> Create a booking information and booking details object  </li>
     * <li> Return the booking with payment pending status  </li>
     *</ol>
     *
     * @param bookingInfo
     * @return
     */
    @Transactional
    public Booking booking(UUID bookingId, BookingInfo bookingInfo ) {

        //If this is a duplicate call, return the previous/existing booking
        final Optional<Booking> existingBooking = this.bookingRepository.findById(bookingId);
        if(existingBooking.isPresent()){
            log.debug("Duplicate request received for booking id {} " , bookingId);
            return existingBooking.get();
        }

        // fetch the show id from another service
        final ShowInformation showInformation = showService.getShow(bookingInfo.showId());
        final Optional<BookingStrategy> optionalBookingStrategy = bookingStrategies.stream().filter(bookingStrategy -> bookingStrategy.isApplicable(showInformation)).findFirst();
        final BookingStrategy bookingStrategy = optionalBookingStrategy.orElseThrow( () -> new NoRecordFoundException("Exception.showInformation.notFound"));
        return bookingStrategy.book(bookingId, bookingInfo);
    }





}
