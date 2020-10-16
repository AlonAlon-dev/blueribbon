package com.blueribbon;

import com.blueribbon.comm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A flights rest controller.
 * Accepts requests coming from clients.
 */
@RestController
public class FlightsController {

    @Autowired
    private FlightsService flightsService;

    /**
     * Checks if a ticket is available.
     * @param isTicketAvailableRequest The request.
     * @return A IsTicketAvailableResponse object.
     */
    @PostMapping("/ticket")
    public IsTicketAvailableResponse isTicketAvailable(@RequestBody IsTicketAvailableRequest isTicketAvailableRequest){
        return flightsService.isTicketAvailable(isTicketAvailableRequest.getTicketId());
    }

    /**
     * Tries to check in.
     * @param baggageCheckInRequest The request.
     * @return A BaggageCheckInResponse object.
     */
    @PostMapping("/baggage")
    public BaggageCheckInResponse baggageCheckIn(@RequestBody BaggageCheckInRequest baggageCheckInRequest){
        return flightsService.baggageCheckIn(baggageCheckInRequest.getDestinationId(), baggageCheckInRequest.getBaggageId());
    }

    /**
     * Tries to apply a coupon.
     * @param applyCouponRequest The request.
     * @return A ApplyCouponResponse object.
     */
    @PostMapping("/coupon")
    public ApplyCouponResponse applyCoupon(@RequestBody ApplyCouponRequest applyCouponRequest){
        return flightsService.applyCoupon(applyCouponRequest.getCouponId(), applyCouponRequest.getPrice());
    }
}

