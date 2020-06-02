package com.mercadolibre.mutants_exam.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HumanGenderStats {

    public static final HumanGenderStats NULL = new HumanGenderStats(0L, 0L, 0D);

    @JsonProperty("count_mutant_dna")
    private final Long countMutantDna;
    @JsonProperty("count_human_dna")
    private final Long countHumanDna;
    private final Double ratio;

    public HumanGenderStats(final Long countMutantDna, final Long countHumanDna, final Double ratio) {
        this.countMutantDna = countMutantDna;
        this.countHumanDna = countHumanDna;
        this.ratio = ratio;
    }

    public Long getCountMutantDna() {
        return countMutantDna;
    }

    public Long getCountHumanDna() {
        return countHumanDna;
    }

    public Double getRatio() {
        return ratio;
    }
}
