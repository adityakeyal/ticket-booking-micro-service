package com.sapient.booking.service.booking.strategy;

import com.sapient.booking.data.domain.Booking;
import com.sapient.booking.data.domain.BookingDetails;
import com.sapient.booking.data.domain.SeatInventory;
import com.sapient.booking.data.repository.BookingDetailsRepository;
import com.sapient.booking.data.repository.BookingRepository;
import com.sapient.booking.data.repository.SeatInventoryRepository;
import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.dto.ShowInformation;
import com.sapient.booking.enums.BookingStatus;
import com.sapient.booking.enums.PaymentStatus;
import com.sapient.booking.enums.SeatAvailability;
import com.sapient.booking.exception.UnableToLockSeatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Implementation for {@link BookingStrategy} which allows for booking in our own DB.
 * This is invoked for all theatre owners who use our own internal DB for managing the booking.
 * This is the default bean and has the lowest precedence and will always be applicable for all theatre owners.
 * Any custom implementation should have a higher precedence.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class DefaultBookingStrategy implements BookingStrategy {

    private final BookingDetailsRepository bookingDetailsRepository;
    private final SeatInventoryRepository seatInventoryRepository;
    private final BookingRepository bookingRepository;


    /**
     * Return true everytime since this is the default strategy
     * @param showInformation
     * @return
     */
    @Override
    public boolean isApplicable(ShowInformation showInformation) {
        //check if the current strategy is applicable for the owner
        return true;
    }

    @Override
    public Booking book(UUID bookingId, BookingInfo bookingInfo) {


        final List<SeatInventory> seatInformation = this.seatInventoryRepository.findAllById(bookingInfo.seatIds());

        // check that seats are available
        final long count = seatInformation.stream().filter(x -> x.getAvailability() != SeatAvailability.AVAILABLE).count();
        if(count > 0){
            log.error("Unable to book selected seats for booking id {} ", bookingId);
            throw new UnableToLockSeatException("UnableToLock.staleData");
        }

        // check that seats  are for same show
        final long countOfSeatsInDifferentShow = seatInformation.stream().filter(x -> !x.getShowId().equals(bookingInfo.showId())).count();
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
        booking.setShowId(bookingInfo.showId());
        booking.setStatus(BookingStatus.PAYMENT_PENDING);
        booking.setUserId(UUID.randomUUID()); //TODO : This should come from the Spring Security Context
        booking.setPaymentStatus(PaymentStatus.PENDING);

        bookingRepository.save(booking);
        log.debug("saved booking information for booking id {} ", bookingId);
        return booking;
    }
}
