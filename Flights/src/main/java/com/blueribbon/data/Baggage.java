package com.blueribbon.data;

public class Baggage {
    private String baggageId;
    private long destinationId;

    public Baggage(String baggageId) {
        this.baggageId = baggageId;
    }

    public String getBaggageId() {
        return baggageId;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }
}
