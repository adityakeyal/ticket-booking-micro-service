package com.sapient.booking.service;

import com.sapient.booking.data.domain.Booking;
import com.sapient.booking.data.domain.SeatInventory;
import com.sapient.booking.data.repository.BookingDetailsRepository;
import com.sapient.booking.data.repository.BookingRepository;
import com.sapient.booking.data.repository.SeatInventoryRepository;
import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.enums.BookingStatus;
import com.sapient.booking.enums.SeatAvailability;
import com.sapient.booking.exception.UnableToLockSeatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {


    @Mock private BookingRepository bookingRepository;
    @Mock private BookingDetailsRepository bookingDetailsRepository;
    @Mock private SeatInventoryRepository seatInventoryRepository;

    @InjectMocks private BookingService bookingService;

    final static UUID  SHOW_UUID = new UUID(0,0);
    private UUID seatUUID;

    @BeforeEach
    public void setup(){

    }


    @Test
    public void testDuplicateBookingId() throws Exception{
        final Booking booking = new Booking();
        Mockito.when(this.bookingRepository.findById(new UUID(0,0))).thenReturn(Optional.of(booking));
        Booking bookingResponse = this.bookingService.booking(new BookingInfo(null,null), new UUID(0,0));
        Assertions.assertEquals(booking, bookingResponse);
    }


    @Test public void testNoSeatProvided() throws Exception{

        Mockito.when(this.bookingRepository.findById(new UUID(0,0))).thenReturn(Optional.empty());
        try{
            Booking bookingResponse = this.bookingService.booking(new BookingInfo(SHOW_UUID,List.of()), new UUID(0,0));
            Assertions.fail();
        }catch (UnableToLockSeatException ex){
            Assertions.assertEquals("UnableToLock.insufficientInformation",ex.getMessage());
        }
    }

    @Test public void testIncorrectSeatAndShowMapped() throws Exception{
        var SHOW_ID = new UUID(0,0);
        var BOOKING_ID = new UUID(0,0);
        var SEAT_ID_1 = new UUID(0,0);
        Mockito.when(this.bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.empty());
        final SeatInventory seatInventory = new SeatInventory();
        seatInventory.setShowId(UUID.randomUUID());
        seatInventory.setId(SEAT_ID_1);
        seatInventory.setAvailability(SeatAvailability.AVAILABLE);
        Mockito.when(this.seatInventoryRepository.findAllById(List.of(SEAT_ID_1))).thenReturn(List.of(seatInventory));

        try{
            Booking bookingResponse = this.bookingService.booking(new BookingInfo(SHOW_ID, List.of(SEAT_ID_1)), BOOKING_ID);
            Assertions.fail();
        }catch (UnableToLockSeatException ex){
            Assertions.assertEquals("UnableToLock.incorrectData",ex.getMessage());
        }

    }
    @Test public void testNotAllSeatsAvailable() throws Exception{
        var SHOW_ID = new UUID(0,0);
        var BOOKING_ID = new UUID(0,0);
        var SEAT_ID_1 = new UUID(0,0);
        var SEAT_ID_2 = UUID.randomUUID();
        Mockito.when(this.bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.empty());
        final SeatInventory seatInventory = new SeatInventory();
        seatInventory.setShowId(SHOW_ID);
        seatInventory.setId(SEAT_ID_1);
        seatInventory.setAvailability(SeatAvailability.AVAILABLE);
        final SeatInventory seatInventory2 = new SeatInventory();
        seatInventory2.setShowId(SHOW_ID);
        seatInventory2.setId(SEAT_ID_2);
        seatInventory2.setAvailability(SeatAvailability.BLOCKED);
        Mockito.when(this.seatInventoryRepository.findAllById(List.of(SEAT_ID_1, SEAT_ID_2))).thenReturn(List.of(seatInventory, seatInventory2));

        try{
            Booking bookingResponse = this.bookingService.booking(new BookingInfo(SHOW_ID, List.of(SEAT_ID_1, SEAT_ID_2)), BOOKING_ID);
            Assertions.fail();
        }catch (UnableToLockSeatException ex){
            Assertions.assertEquals("UnableToLock.staleData",ex.getMessage());
        }


    }
    @Test public void testSeatsAreBlockedSuccessfully() throws Exception{
        var SHOW_ID = new UUID(0,0);
        var BOOKING_ID = new UUID(0,0);
        var SEAT_ID_1 = new UUID(0,0);
        var SEAT_ID_2 = UUID.randomUUID();
        Mockito.when(this.bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.empty());
        final SeatInventory seatInventory = new SeatInventory();
        seatInventory.setShowId(SHOW_ID);
        seatInventory.setId(SEAT_ID_1);
        seatInventory.setAvailability(SeatAvailability.AVAILABLE);
        final SeatInventory seatInventory2 = new SeatInventory();
        seatInventory2.setShowId(SHOW_ID);
        seatInventory2.setId(SEAT_ID_2);
        seatInventory2.setAvailability(SeatAvailability.AVAILABLE);
        Mockito.when(this.seatInventoryRepository.findAllById(List.of(SEAT_ID_1, SEAT_ID_2))).thenReturn(List.of(seatInventory, seatInventory2));
        Booking bookingResponse = this.bookingService.booking(new BookingInfo(SHOW_ID, List.of(SEAT_ID_1, SEAT_ID_2)), BOOKING_ID);
        Mockito.verify(this.seatInventoryRepository).saveAll(ArgumentMatchers.argThat(seatInventories -> {
            seatInventories.forEach( x-> Assertions.assertEquals(SeatAvailability.BLOCKED, x.getAvailability()));
            return true;
        }));
    }
    @Test public void testBookingDetailsMatchesAllSeatsSelected() throws Exception{

        var SHOW_ID = new UUID(0,0);
        var BOOKING_ID = new UUID(0,0);
        var SEAT_ID_1 = new UUID(0,0);
        var SEAT_ID_2 = UUID.randomUUID();
        Mockito.when(this.bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.empty());
        final SeatInventory seatInventory = new SeatInventory();
        seatInventory.setShowId(SHOW_ID);
        seatInventory.setId(SEAT_ID_1);
        seatInventory.setAvailability(SeatAvailability.AVAILABLE);
        final SeatInventory seatInventory2 = new SeatInventory();
        seatInventory2.setShowId(SHOW_ID);
        seatInventory2.setId(SEAT_ID_2);
        seatInventory2.setAvailability(SeatAvailability.AVAILABLE);
        Mockito.when(this.seatInventoryRepository.findAllById(List.of(SEAT_ID_1, SEAT_ID_2))).thenReturn(List.of(seatInventory, seatInventory2));
        Mockito.when(this.seatInventoryRepository.saveAll(Mockito.any())).thenAnswer(i->i.getArguments()[0]);
        Booking bookingResponse = this.bookingService.booking(new BookingInfo(SHOW_ID, List.of(SEAT_ID_1, SEAT_ID_2)), BOOKING_ID);
        Mockito.verify(this.bookingDetailsRepository).saveAll(ArgumentMatchers.argThat(bookingDetails -> {

            Assertions.assertNotNull(bookingDetails);
            List<SeatInventory> seats = new ArrayList<>();
            bookingDetails.forEach( bd -> seats.add(bd.getSeat()));
            Assertions.assertEquals(2, seats.size());

            if(!((seats.get(0).getId().equals(seatInventory.getId()) || seats.get(1).getId().equals(seatInventory.getId()) )&&
                    (seats.get(0).getId().equals(seatInventory2.getId()) || seats.get(1).getId().equals(seatInventory2.getId()) ))){
                Assertions.fail();
            }

            return true;
        }));


    }
    @Test public void testBookingInformation() throws Exception{

        var SHOW_ID = new UUID(0,0);
        var BOOKING_ID = new UUID(0,0);
        var SEAT_ID_1 = new UUID(0,0);
        var SEAT_ID_2 = UUID.randomUUID();
        Mockito.when(this.bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.empty());
        final SeatInventory seatInventory = new SeatInventory();
        seatInventory.setShowId(SHOW_ID);
        seatInventory.setId(SEAT_ID_1);
        seatInventory.setAvailability(SeatAvailability.AVAILABLE);
        final SeatInventory seatInventory2 = new SeatInventory();
        seatInventory2.setShowId(SHOW_ID);
        seatInventory2.setId(SEAT_ID_2);
        seatInventory2.setAvailability(SeatAvailability.AVAILABLE);
        Mockito.when(this.seatInventoryRepository.findAllById(List.of(SEAT_ID_1, SEAT_ID_2))).thenReturn(List.of(seatInventory, seatInventory2));
        Mockito.when(this.seatInventoryRepository.saveAll(Mockito.any())).thenAnswer(i->i.getArguments()[0]);

        Booking bookingResponse = this.bookingService.booking(new BookingInfo(SHOW_ID, List.of(SEAT_ID_1, SEAT_ID_2)), BOOKING_ID);

        Mockito.verify(bookingRepository).save(ArgumentMatchers.argThat( booking -> {
            Assertions.assertEquals(BOOKING_ID,booking.getId());
            Assertions.assertEquals(SHOW_ID, booking.getShowId());
            Assertions.assertEquals(BookingStatus.PAYMENT_PENDING, booking.getStatus());
            Assertions.assertNull(booking.getPaymentId());
            return true;
        }));

        Assertions.assertEquals(BOOKING_ID,bookingResponse.getId());
        Assertions.assertEquals(SHOW_ID, bookingResponse.getShowId());
        Assertions.assertEquals(BookingStatus.PAYMENT_PENDING, bookingResponse.getStatus());
        Assertions.assertNull(bookingResponse.getPaymentId());




    }


}