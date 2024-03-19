package com.g11.savingtrack.exception.passbook;

import com.g11.savingtrack.exception.base.ConflictException;

public class PassbookAlreadyWithdaw extends ConflictException {
    public PassbookAlreadyWithdaw(){
        setStatus(409);
        setMessage("Passbook already withdraw");}
}
