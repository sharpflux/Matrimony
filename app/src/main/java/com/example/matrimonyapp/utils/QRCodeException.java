package com.example.matrimonyapp.utils;
public class QRCodeException extends Exception {

    public QRCodeException(String message) {
        super(message);
    }

    public QRCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public QRCodeException(Throwable cause) {
        super(cause);
    }
}