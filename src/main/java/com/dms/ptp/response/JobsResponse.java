package com.dms.ptp.response;

/**
 * JobsResponse
 */
public class JobsResponse {

    private boolean success;

    public JobsResponse(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
