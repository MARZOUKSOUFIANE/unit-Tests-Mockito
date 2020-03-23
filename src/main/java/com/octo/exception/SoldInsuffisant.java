package com.octo.exception;

public class SoldInsuffisant extends Exception {
    public SoldInsuffisant() {
        System.out.println("Le sold du compte Emetteur est insuffisant, Tranfert impossible !");
    }
}
