package com.g11.savingtrack.exception.account;

import com.g11.savingtrack.exception.base.BadRequestException;

public class IncomeNotEnoughtMoney extends BadRequestException {
    private String mes;

    public String getMes() {
        return mes;
    }

    public  IncomeNotEnoughtMoney(){
        this.mes = "số dư của bạn không đủ";
    }

}
