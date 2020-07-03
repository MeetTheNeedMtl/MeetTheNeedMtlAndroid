package com.example.qrcodescanner;

public enum LoginStatus {
    SUCCESSFUL,         //success
    UNSUCCESSFUL,       //incorrect credentials
    INVALID,            //username or password is empty
    ERROR               //error logging in
}