package com.mercadolibre.mutants_exam.dto;

public class DNASequence {

    private String[] dna;

    public DNASequence() {}

    public DNASequence(final String[] dna) {
        this.dna = dna;
    }

    public String[] getDna() { return dna; }
}
