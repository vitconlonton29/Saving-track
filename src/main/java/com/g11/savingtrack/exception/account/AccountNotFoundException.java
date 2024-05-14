package com.g11.savingtrack.exception.account;

import com.g11.savingtrack.exception.base.NotFoundException;
import lombok.Data;

@Data
public class AccountNotFoundException extends NotFoundException {
    private String mess;

    public AccountNotFoundException(){
        setMessage("Account not found");
        setMess("Account not found");
    }
}
