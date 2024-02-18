package com.sapient.booking.service.booking.strategy;

import com.sapient.booking.data.domain.Booking;
import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.dto.ShowInformation;

import java.util.UUID;

/**
 * A strategy to book tickets. The default implementation will allow for booking in our own DB.
 * For any theatre owner who have their own IT systems, a new strategy needs to be implemented.
 *
 */
public interface BookingStrategy {

    /**
     * The accepted method works on the show information. If a strategy is applied for a theatre owner, then this method should return true.
     * @param showInformation
     * @return
     */
    boolean isApplicable(ShowInformation showInformation);

    /**
     * Method used to book a seat
     *
     * @param bookingId
     * @param bookingInfo
     * @return
     */
    Booking book(UUID bookingId, BookingInfo bookingInfo);

}
