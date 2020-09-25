package com.example.qrcodescanner;

/*
 * Copyright (C) 2020 Daniel Bucci
 * This file is subject to the terms and conditions defined in
 * file "LICENSE.txt", which is part of this source code package.
 */

public enum LoginStatus {
    SUCCESSFUL,         //success
    UNSUCCESSFUL,       //incorrect credentials
    INVALID,            //username or password is empty
    ERROR               //error logging in
}