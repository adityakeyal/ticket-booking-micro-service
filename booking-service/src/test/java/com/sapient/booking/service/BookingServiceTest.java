package com.sapient.booking.service;

import com.sapient.booking.data.domain.Booking;
import com.sapient.booking.data.domain.SeatInventory;
import com.sapient.booking.data.repository.BookingRepository;
import com.sapient.booking.dto.BookingInfo;
import com.sapient.booking.enums.SeatAvailability;
import com.sapient.booking.exception.MultipleRecordsFoundException;
import com.sapient.booking.exception.NoRecordFoundException;
import com.sapient.booking.exception.UnableToLockSeatException;
import com.sapient.booking.service.booking.strategy.BookingStrategy;
import com.sapient.booking.service.external.ShowService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {




    @Mock private BookingRepository bookingRepository;
    @Mock private ShowService showService;
    @Spy private List<BookingStrategy> bookingStrategies = new ArrayList<>();

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
        Booking bookingResponse = this.bookingService.booking(new UUID(0,0), new BookingInfo(null,null));
        Assertions.assertEquals(booking, bookingResponse);
    }


    @Test public void testIncorrectShowProvided() {
        Mockito.when(this.bookingRepository.findById(new UUID(0,0))).thenReturn(Optional.empty());

        try{
            Booking bookingResponse = this.bookingService.booking(new UUID(0,0), new BookingInfo(SHOW_UUID,List.of()));
            Assertions.fail();
        }catch (NoRecordFoundException ex){
            Assertions.assertEquals("Exception.showInformation.notFound",ex.getMessage());
        }
    }

    @Test public void testIfMultipleStrategiesAreProvidedFIrstIsReturned() throws Exception{
        var SHOW_ID = new UUID(0,0);
        var BOOKING_ID = new UUID(0,0);
        var SEAT_ID_1 = new UUID(0,0);
        Mockito.when(this.bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.empty());
        final SeatInventory seatInventory = new SeatInventory();
        seatInventory.setShowId(UUID.randomUUID());
        seatInventory.setId(SEAT_ID_1);
        seatInventory.setAvailability(SeatAvailability.AVAILABLE);


        var strategy1=Mockito.mock(BookingStrategy.class);
        var strategy2=Mockito.mock(BookingStrategy.class);
        var strategy3=Mockito.mock(BookingStrategy.class);

        bookingStrategies.add(strategy1);
        bookingStrategies.add(strategy2);
        bookingStrategies.add(strategy3);
        Mockito.when(strategy1.isApplicable(Mockito.any())).thenReturn(Boolean.FALSE);
        Mockito.when(strategy2.isApplicable(Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.lenient().when(strategy3.isApplicable(Mockito.any())).thenReturn(Boolean.TRUE);


        Booking bookingResponse = this.bookingService.booking(BOOKING_ID, new BookingInfo(SHOW_ID, List.of(SEAT_ID_1)));
        Mockito.verify(strategy2, Mockito.atLeastOnce()).book(Mockito.any(), Mockito.any());
        Mockito.verify(strategy2, Mockito.atMost(1)).book(Mockito.any(), Mockito.any());
        Mockito.verify(strategy3, Mockito.never()).book(Mockito.any(), Mockito.any());


    }
    @Test public void testIfBookingIsUnsuccessful() throws Exception{

        var SHOW_ID = new UUID(0,0);
        var BOOKING_ID = new UUID(0,0);
        var SEAT_ID_1 = new UUID(0,0);
        Mockito.when(this.bookingRepository.findById(BOOKING_ID)).thenReturn(Optional.empty());
        final SeatInventory seatInventory = new SeatInventory();
        seatInventory.setShowId(UUID.randomUUID());
        seatInventory.setId(SEAT_ID_1);
        seatInventory.setAvailability(SeatAvailability.AVAILABLE);
//        Mockito.when(this.seatInventoryRepository.findAllById(List.of(SEAT_ID_1))).thenReturn(List.of(seatInventory));

        var strategy1=Mockito.mock(BookingStrategy.class);
        bookingStrategies.add(strategy1);
        Mockito.when(strategy1.isApplicable(Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(strategy1.book(Mockito.any(), Mockito.any())).thenThrow(new UnableToLockSeatException("UnableToLock.incorrectData"));


        try{
            Booking bookingResponse = this.bookingService.booking(BOOKING_ID, new BookingInfo(SHOW_ID, List.of(SEAT_ID_1)));
            Assertions.fail();
        }catch (UnableToLockSeatException ex){
            Assertions.assertEquals("UnableToLock.incorrectData",ex.getMessage());
        }


    }
    @Test public void testSeatsAreBlockedSuccessfully() {
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

        var strategy1=Mockito.mock(BookingStrategy.class);
        bookingStrategies.add(strategy1);

        var booking = new Booking();
        Mockito.when(strategy1.isApplicable(Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(strategy1.book(Mockito.any(), Mockito.any())).thenReturn(booking);

        Booking bookingResponse = this.bookingService.booking(BOOKING_ID, new BookingInfo(SHOW_ID, List.of(SEAT_ID_1, SEAT_ID_2)));
        Assertions.assertEquals(booking,bookingResponse);

    }




}