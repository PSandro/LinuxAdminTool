package com.linuxgodmode.business.exception;

/**
 * Created by sandro on 10.04.17 for project androidapp.
 */

public class ConnectionAlreadyInitializedException extends Exception {
    public ConnectionAlreadyInitializedException() {
        super("The connection has already been initialized");
    }
}
