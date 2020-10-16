package com.blueribbon;

import com.blueribbon.data.Baggage;
import com.blueribbon.data.Coupon;
import com.blueribbon.data.Ticket;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FlightDAO {

    public Map<Long, Ticket> getAllTickets(){
        Map<Long, Ticket> ticketMap = new HashMap<>();

        Ticket ticket = new Ticket(5l);
        ticketMap.put(ticket.getTicketId(), ticket);

        return ticketMap;
    }

    public Map<String, Baggage> getAllBaggages(){
        Map<String, Baggage> baggageMap = new HashMap<>();

        Baggage baggage = new Baggage("abc");
        baggage.setDestinationId(77);
        baggageMap.put(baggage.getBaggageId(), baggage);

        return baggageMap;
    }

    public Map<Long, Coupon> getAllCoupons(){
        Map<Long, Coupon> couponMap = new HashMap<>();

        Coupon coupon = new Coupon(3l);
        coupon.setPrice(5.99d);
        couponMap.put(coupon.getCouponId(), coupon);

        return couponMap;
    }
}
