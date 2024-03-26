package com.g11.savingtrack.exception.account;

import com.g11.savingtrack.exception.base.NotFoundException;

public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException(){
        setMessage("Account not found");
    }
}
