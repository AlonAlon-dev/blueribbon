package com.blueribbon;

import com.blueribbon.cache.Cache;
import com.blueribbon.comm.ApplyCouponResponse;
import com.blueribbon.comm.BaggageCheckInResponse;
import com.blueribbon.comm.IsTicketAvailableResponse;
import com.blueribbon.data.Baggage;
import com.blueribbon.data.Coupon;
import com.blueribbon.data.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.StampedLock;

/**
 * Holds all the cases available for the server.
 */
@Service
public class FlightsService {
    private static final Logger log = LogManager.getLogger(FlightsService.class);

    @Autowired
    private FlightDAO flightDAO;

    private StampedLock ticketsLock;
    private StampedLock baggagesLock;
    private StampedLock couponsLock;

    private Map<Long, Ticket> tickets;
    private Map<String, Baggage> baggages;
    private Map<Long, Coupon> coupons;

    private Cache<String, IsTicketAvailableResponse> isTicketAvailableCache;
    private Cache<String, BaggageCheckInResponse> baggageCheckInResponseCache;
    private Cache<String, ApplyCouponResponse> applyCouponResponseCache;

    @PostConstruct
    private void postConstruct() {
        ticketsLock = new StampedLock();
        baggagesLock = new StampedLock();
        couponsLock = new StampedLock();

        loadTickets();
        loadBaggages();
        loadCoupons();
    }

    /**
     * Loads all the tickets from the db and stores them in a map.
     */
    public void loadTickets(){
        Map<Long,Ticket> loadedTickets = flightDAO.getAllTickets();
        long stamp = ticketsLock.writeLock();
        try {
            tickets = loadedTickets;
            log.info(tickets.size() + " tickets loaded");
        } finally {
            ticketsLock.unlock(stamp);
        }
    }

    /**
     * Get a ticket by its id.
     * @param id The ticket id.
     * @return A ticket object.
     */
    public Ticket getTicketById(long id) {
        long stamp = ticketsLock.readLock();
        try {
            return tickets.get(id);
        }finally {
            ticketsLock.unlock(stamp);
        }
    }

    /**
     * Loads all the baggages from the db and stores them in a map.
     */
    public void loadBaggages(){
        Map<String,Baggage> loadedBaggages = flightDAO.getAllBaggages();
        long stamp = baggagesLock.writeLock();
        try {
            baggages = loadedBaggages;
            log.info(baggages.size() + " baggages loaded");
        } finally {
            baggagesLock.unlock(stamp);
        }
    }

    /**
     * Get a baggage by its id.
     * @param id The baggage id.
     * @return A baggage object.
     */
    public Baggage getBaggageById(String id) {
        long stamp = baggagesLock.readLock();
        try {
            return baggages.get(id);
        }finally {
            baggagesLock.unlock(stamp);
        }
    }

    /**
     * Loads all the coupons from the db and stores them in a map.
     */
    public void loadCoupons(){
        Map<Long,Coupon> loadedCoupons = flightDAO.getAllCoupons();
        long stamp = couponsLock.writeLock();
        try {
            coupons = loadedCoupons;
            log.info(coupons.size() + " coupons loaded");
        } finally {
            couponsLock.unlock(stamp);
        }
    }

    /**
     * Get a coupon by its id.
     * @param id The coupon id.
     * @return A coupon object.
     */
    public Coupon getCouponById(long id) {
        long stamp = couponsLock.readLock();
        try {
            return coupons.get(id);
        }finally {
            couponsLock.unlock(stamp);
        }
    }

    /**
     * Checks if a ticket is available.
     * @param ticketId The ticket id.
     * @return A IsTicketAvailableResponse object.
     */
    public IsTicketAvailableResponse isTicketAvailable(long ticketId){
        // Try getting a ticket with the requested ticket id.
        Ticket ticket = getTicketById(ticketId);

        // Prepare the response.
        IsTicketAvailableResponse isTicketAvailableResponse = new IsTicketAvailableResponse();
        isTicketAvailableResponse.setTicketAvailable(ticket != null);
        return isTicketAvailableResponse;
    }

    /**
     * Tries to check in.
     * @param destinationId The destination id.
     * @param baggageId The baggage id.
     * @return A BaggageCheckInResponse object.
     */
    public BaggageCheckInResponse baggageCheckIn(long destinationId, String baggageId){
        // Try getting a baggage with the requested baggage id.
        Baggage baggage = getBaggageById(baggageId);

        // Make sure that the baggage is found.
        if(baggage == null){
            BaggageCheckInResponse baggageCheckInResponse = new BaggageCheckInResponse();
            baggageCheckInResponse.setStatus(BaggageCheckInResponse.Status.BAGGAGE_ID_NOT_FOUND);
            return baggageCheckInResponse;
        }

        // Make sure that the destination id is equal.
        if(baggage.getDestinationId() != destinationId){
            BaggageCheckInResponse baggageCheckInResponse = new BaggageCheckInResponse();
            baggageCheckInResponse.setStatus(BaggageCheckInResponse.Status.WRONG_DESTINATION_ID);
            return baggageCheckInResponse;
        }

        // Prepare the response.
        BaggageCheckInResponse baggageCheckInResponse = new BaggageCheckInResponse();
        baggageCheckInResponse.setStatus(BaggageCheckInResponse.Status.OK);
        return baggageCheckInResponse;
    }

    /**
     * Tries to apply a coupon.
     * @param couponId The coupon id.
     * @param price The price id.
     * @return A ApplyCouponResponse object.
     */
    public ApplyCouponResponse applyCoupon(long couponId, double price){
        // Try getting a coupon with the requested coupon id.
        Coupon coupon = getCouponById(couponId);

        // Make sure that the coupon is found.
        if(coupon == null){
            ApplyCouponResponse applyCouponResponse = new ApplyCouponResponse();
            applyCouponResponse.setStatus(ApplyCouponResponse.Status.COUPON_ID_NOT_FOUND);
            return applyCouponResponse;
        }

        // Make sure that the price is equal.
        if(Math.abs(coupon.getPrice() - price) > 0.0001d ){
            ApplyCouponResponse applyCouponResponse = new ApplyCouponResponse();
            applyCouponResponse.setStatus(ApplyCouponResponse.Status.WRONG_PRICE);
            return applyCouponResponse;
        }

        // Calculate the discounted price.
        int randomDiscountPercentageIndex = ThreadLocalRandom.current().nextInt(3);
        int randomDiscountPercentage = new int[]{10, 50, 60}[randomDiscountPercentageIndex];
        double priceAfterDiscount = coupon.getPrice() * ((100 - randomDiscountPercentage)/100d);

        // Prepare the response.
        ApplyCouponResponse applyCouponResponse = new ApplyCouponResponse();
        applyCouponResponse.setStatus(ApplyCouponResponse.Status.OK);
        applyCouponResponse.setPriceAfterDiscount(priceAfterDiscount);
        return applyCouponResponse;
    }
}