package com.linuxgodmode.business.exception;

/**
 * Created by sandro on 10.04.17 for project androidapp.
 */

public class ConnectionNotInitializedException extends Exception {
    public ConnectionNotInitializedException() {
        super("The Connection has not been initialized");
    }
}
