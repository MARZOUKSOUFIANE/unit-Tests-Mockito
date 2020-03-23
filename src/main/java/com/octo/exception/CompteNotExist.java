package com.octo.exception;

public class CompteNotExist extends Exception {
    public CompteNotExist(String message) {
        System.out.println(message+ " , Tranfert impossible");
    }
}
