package com.sapient.booking.service;

import com.sapient.booking.data.domain.Booking;
import com.sapient.booking.data.domain.BookingDetails;
import com.sapient.booking.data.domain.SeatInventory;
import com.sapient.booking.data.repository.BookingDetailsRepository;
import com.sapient.booking.data.repository.BookingRepository;
import com.sapient.booking.data.repository.SeatInventoryRepository;
import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.enums.BookingStatus;
import com.sapient.booking.enums.PaymentStatus;
import com.sapient.booking.enums.SeatAvailability;
import com.sapient.booking.exception.UnableToLockSeatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Service for {@link Booking} domain
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingDetailsRepository bookingDetailsRepository;
    private final SeatInventoryRepository seatInventoryRepository;


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
     * @param information
     * @return
     */
    @Transactional
    public Booking booking(BookingInfo information, UUID bookingId ) {

        //If this is a duplicate call, return the previous/existing booking
        final Optional<Booking> existingBooking = this.bookingRepository.findById(bookingId);
        if(existingBooking.isPresent()){
            log.debug("Duplicate request received for booking id {} " , bookingId);
            return existingBooking.get();
        }

        // atleast one seat should be selected
        if(information.seatIds().isEmpty()){
            log.error("No seats selected for booking id {} ", bookingId);
            throw new UnableToLockSeatException("UnableToLock.insufficientInformation");
        }

        final List<SeatInventory> seatInformation = this.seatInventoryRepository.findAllById(information.seatIds());

        // check that seats are available
        final long count = seatInformation.stream().filter(x -> x.getAvailability() != SeatAvailability.AVAILABLE).count();
        if(count > 0){
            log.error("Unable to book selected seats for booking id {} ", bookingId);
            throw new UnableToLockSeatException("UnableToLock.staleData");
        }

        // check that seats  are for same show
        final long countOfSeatsInDifferentShow = seatInformation.stream().filter(x -> !x.getShowId().equals(information.showId())).count();
        if(countOfSeatsInDifferentShow > 0){
            log.error("Unable to book selected seats for booking id {} ", bookingId);
            throw new UnableToLockSeatException("UnableToLock.incorrectData");
        }

        seatInformation.forEach(x->x.setAvailability(SeatAvailability.BLOCKED));
        final List<SeatInventory> seatInventories = this.seatInventoryRepository.saveAll(seatInformation);

        log.debug("blocked seats for booking id {} ", bookingId);

        final Set<BookingDetails> bookingDetails = seatInventories.stream().map(BookingDetails::new).collect(Collectors.toSet());

        this.bookingDetailsRepository.saveAll(bookingDetails);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setDetails(bookingDetails);
        booking.setShowId(information.showId());
        booking.setStatus(BookingStatus.PAYMENT_PENDING);
        booking.setUserId(UUID.randomUUID()); //TODO : This should come from the Spring Security Context
        booking.setPaymentStatus(PaymentStatus.PENDING);

        bookingRepository.save(booking);
        log.debug("saved booking information for booking id {} ", bookingId);

        return booking;
    }





}
