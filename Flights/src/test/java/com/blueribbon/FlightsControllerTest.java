package com.blueribbon;

import com.blueribbon.comm.ApplyCouponRequest;
import com.blueribbon.comm.BaggageCheckInRequest;
import com.blueribbon.comm.BaggageCheckInResponse;
import com.blueribbon.comm.IsTicketAvailableRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FlightsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void isTicketAvailableTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        IsTicketAvailableRequest isTicketAvailableRequest = new IsTicketAvailableRequest();
        isTicketAvailableRequest.setTicketId(5l);

        this.mockMvc.perform(post("/ticket")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(isTicketAvailableRequest)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketAvailable").value(true));
    }

    @Test
    public void baggageCheckInTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        BaggageCheckInRequest baggageCheckInRequest = new BaggageCheckInRequest();
        baggageCheckInRequest.setDestinationId(77);
        baggageCheckInRequest.setBaggageId("abc");

        this.mockMvc.perform(post("/baggage")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(baggageCheckInRequest)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(BaggageCheckInResponse.Status.OK.toString()));
    }

    @Test
    public void applyCouponTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ApplyCouponRequest applyCouponRequest = new ApplyCouponRequest();
        applyCouponRequest.setCouponId(3l);
        applyCouponRequest.setPrice(5.99d);

        this.mockMvc.perform(post("/coupon")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applyCouponRequest)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(BaggageCheckInResponse.Status.OK.toString()));
    }
}