package com.blueribbon;

import com.blueribbon.comm.ApplyCouponResponse;
import com.blueribbon.comm.BaggageCheckInResponse;
import com.blueribbon.comm.IsTicketAvailableResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.test.util.AssertionErrors.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { FlightsService.class, FlightDAO.class})
class FlightsServiceTest {
    @Autowired
    private FlightsService flightsService;

    @Test
    public void isTicketAvailableTest(){
        // Positive.
        IsTicketAvailableResponse isTicketAvailableResponse = flightsService.isTicketAvailable(5l);
        assertTrue("wrong response", isTicketAvailableResponse.isTicketAvailable());

        // Negative.
        isTicketAvailableResponse = flightsService.isTicketAvailable(4l);
        assertFalse("wrong response", isTicketAvailableResponse.isTicketAvailable());
    }

    @Test
    public void baggageCheckInTest(){
        // Positive.
        BaggageCheckInResponse baggageCheckInResponse = flightsService.baggageCheckIn(77, "abc");
        assertEquals("wrong response", BaggageCheckInResponse.Status.OK, baggageCheckInResponse.getStatus());

        // Negative.
        baggageCheckInResponse = flightsService.baggageCheckIn(77, "ab");
        assertEquals("wrong response", BaggageCheckInResponse.Status.BAGGAGE_ID_NOT_FOUND, baggageCheckInResponse.getStatus());

        baggageCheckInResponse = flightsService.baggageCheckIn(76, "abc");
        assertEquals("wrong response", BaggageCheckInResponse.Status.WRONG_DESTINATION_ID, baggageCheckInResponse.getStatus());
    }

    @Test
    public void applyCouponTest(){
        // Positive.
        ApplyCouponResponse applyCouponResponse = flightsService.applyCoupon(3l, 5.99d);
        assertEquals("wrong response", ApplyCouponResponse.Status.OK, applyCouponResponse.getStatus());

        // Negative.
        applyCouponResponse = flightsService.applyCoupon(2l, 5.99d);
        assertEquals("wrong response", ApplyCouponResponse.Status.COUPON_ID_NOT_FOUND, applyCouponResponse.getStatus());

        applyCouponResponse = flightsService.applyCoupon(3l, 5.98d);
        assertEquals("wrong response", ApplyCouponResponse.Status.WRONG_PRICE, applyCouponResponse.getStatus());
    }

}