package com.mercadolibre.mutants_exam.exception;

public class BadDNASequenceException extends Exception {

    public BadDNASequenceException() { super(); }

    public BadDNASequenceException(String message) {
        super(message);
    }
}
