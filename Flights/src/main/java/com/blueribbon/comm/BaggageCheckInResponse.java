package com.blueribbon.comm;

public class BaggageCheckInResponse {
    public enum Status {
        OK,
        BAGGAGE_ID_NOT_FOUND,
        WRONG_DESTINATION_ID
    }

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
