package com.synechron.synehire.exception;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No Internet connectivity found";
    }

}