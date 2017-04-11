package com.linuxgodmode.business.exception;

/**
 * Created by sandro on 10.04.17 for project androidapp.
 */

public class InitialisationFailException extends Exception {
    public InitialisationFailException() {
        super("The connection could not get initialized");
    }
}
