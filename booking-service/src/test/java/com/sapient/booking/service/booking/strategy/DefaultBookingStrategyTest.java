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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class DefaultBookingStrategyTest {

    @Mock  BookingDetailsRepository bookingDetailsRepository;
    @Mock  SeatInventoryRepository seatInventoryRepository;
    @Mock  BookingRepository bookingRepository;

    @InjectMocks private DefaultBookingStrategy defaultBookingStrategy;
    private UUID bookingUUID = UUID.randomUUID();
    private UUID showUUID = UUID.randomUUID();
    private UUID seatId1 = UUID.randomUUID();
    private UUID seatId2 = UUID.randomUUID();


    @Test
    void testAccepted(){
        final boolean accepted = defaultBookingStrategy.isApplicable(new ShowInformation(null, null, null, null));
        Assertions.assertTrue(accepted);
    }



    @Test
    void testIfAllSelectedSeatsAreNotAvailable(){

        final SeatInventory seatInventory1 = new SeatInventory();
        seatInventory1.setAvailability(SeatAvailability.AVAILABLE);
        final SeatInventory seatInventory2 = new SeatInventory();
        seatInventory2.setAvailability(SeatAvailability.BLOCKED);
        Mockito.when(seatInventoryRepository.findAllById(Mockito.any())).thenReturn(List.of(seatInventory1,seatInventory2));

        try{
            defaultBookingStrategy.book(bookingUUID,new BookingInfo(showUUID, List.of(seatId1,seatId2)));
            Assertions.fail();
        }catch (UnableToLockSeatException ex){
            Assertions.assertNotNull(ex);
            Assertions.assertEquals("UnableToLock.staleData",ex.getMessage());
        }
    }

    @Test void testIfShowIsInADifferentShowThanTheSelectedSeats(){
        final SeatInventory seatInventory1 = new SeatInventory();
        seatInventory1.setAvailability(SeatAvailability.AVAILABLE);
        seatInventory1.setShowId(showUUID);
        final SeatInventory seatInventory2 = new SeatInventory();
        seatInventory2.setAvailability(SeatAvailability.AVAILABLE);
        seatInventory2.setShowId(UUID.randomUUID());
        Mockito.when(seatInventoryRepository.findAllById(Mockito.any())).thenReturn(List.of(seatInventory1,seatInventory2));

        try{
            defaultBookingStrategy.book(bookingUUID,new BookingInfo(showUUID, List.of(seatId1,seatId2)));
            Assertions.fail();
        }catch (UnableToLockSeatException ex){
            Assertions.assertNotNull(ex);
            Assertions.assertEquals("UnableToLock.incorrectData",ex.getMessage());
        }


    }

    @Test void testSuccessfulBooking(){
        final SeatInventory seatInventory1 = new SeatInventory();
        seatInventory1.setId(seatId1);
        seatInventory1.setAvailability(SeatAvailability.AVAILABLE);
        seatInventory1.setShowId(showUUID);
        final SeatInventory seatInventory2 = new SeatInventory();
        seatInventory2.setId(seatId2);
        seatInventory2.setAvailability(SeatAvailability.AVAILABLE);
        seatInventory2.setShowId(showUUID);
        Mockito.when(seatInventoryRepository.findAllById(Mockito.any())).thenReturn(List.of(seatInventory1,seatInventory2));
        Mockito.when(seatInventoryRepository.saveAll(Mockito.any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);


        final Booking book = defaultBookingStrategy.book(bookingUUID, new BookingInfo(showUUID, List.of(seatId1, seatId2)));
        Assertions.assertNotNull(book);
        Assertions.assertEquals(bookingUUID, book.getId());
        Assertions.assertEquals(BookingStatus.PAYMENT_PENDING, book.getStatus());
        Assertions.assertEquals(PaymentStatus.PENDING, book.getPaymentStatus());
        Assertions.assertEquals(showUUID, book.getShowId());

        Mockito.verify(bookingRepository).save(ArgumentMatchers.argThat(booking -> {
            Assertions.assertEquals(bookingUUID,booking.getId());
            Assertions.assertEquals(showUUID, booking.getShowId());
            Assertions.assertEquals(BookingStatus.PAYMENT_PENDING, booking.getStatus());
            Assertions.assertNull(booking.getPaymentId());
            return true;
        }));

        Mockito.verify(bookingDetailsRepository).saveAll(ArgumentMatchers.argThat(details -> {
            final List<String> detailsUUIDString = StreamSupport.stream(details.spliterator(), false).map(BookingDetails::getSeat).map(SeatInventory::getId)
                    .map(UUID::toString).collect(Collectors.toList());

            assertThat(List.of(seatId1.toString(), seatId2.toString())).hasSameElementsAs(detailsUUIDString);

            return true;
        }));


        Mockito.verify(seatInventoryRepository).saveAll(ArgumentMatchers.argThat(details -> {
            final List<String> detailsUUIDString = StreamSupport.stream(details.spliterator(), false).map(SeatInventory::getId)
                    .map(UUID::toString).collect(Collectors.toList());


            assertThat(List.of(seatId1.toString(), seatId2.toString())).hasSameElementsAs(detailsUUIDString);

            Assertions.assertTrue(StreamSupport.stream(details.spliterator(), false).map(SeatInventory::getAvailability)
                    .allMatch(x -> x == SeatAvailability.BLOCKED));
            return true;
        }));


    }


}