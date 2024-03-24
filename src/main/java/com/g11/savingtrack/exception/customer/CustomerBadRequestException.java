package com.g11.savingtrack.exception.customer;

import com.g11.savingtrack.exception.base.BadRequestException;

public class CustomerBadRequestException extends BadRequestException {
    public CustomerBadRequestException(String mes){
        setMessage(mes);
    }
}
