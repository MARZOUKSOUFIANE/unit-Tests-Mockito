package com.octo.exception;

public class MontantNegatif extends Exception {
    public MontantNegatif() {
        System.out.println("Le montant saisi est Negatif, Tranfert impossible !");
    }
}
