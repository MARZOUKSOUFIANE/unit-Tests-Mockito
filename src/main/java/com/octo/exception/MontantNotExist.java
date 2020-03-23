package com.octo.exception;

public class MontantNotExist extends Exception {
    public MontantNotExist() {
        System.out.println("Le montant n'existe pas, Tranfert impossible !");
    }
}
