package com.g11.savingtrack.exception.employee;

import com.g11.savingtrack.exception.base.BadRequestException;

public class UsernamePasswordException extends BadRequestException {
    public  UsernamePasswordException(){
        setMessage("Username or password incorrect");
    }
}
