package com.mercadolibre.mutants_exam.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "Human_Gender_Stats")
public class HumanGenderStat {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long nonMutantCount;

    @NotNull
    private Long mutantCount;

    public HumanGenderStat() {}

    public HumanGenderStat(Long nonMutantCount, Long mutantCount) {
        this.nonMutantCount = nonMutantCount;
        this.mutantCount = mutantCount;
    }

    public Long getNonMutantCount() {
        return nonMutantCount;
    }

    public void setNonMutantCount(Long nonMutantCount) {
        this.nonMutantCount = nonMutantCount;
    }

    public Long getMutantCount() {
        return mutantCount;
    }

    public void setMutantCount(Long mutantCount) {
        this.mutantCount = mutantCount;
    }
}
