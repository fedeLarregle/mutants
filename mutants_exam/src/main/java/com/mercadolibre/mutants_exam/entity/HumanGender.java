package com.mercadolibre.mutants_exam.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity(name = "Human_Gender")
public class HumanGender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dna", length = 16, unique = true, nullable = false)
    private UUID dna;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    public HumanGender() {}

    public HumanGender(final UUID dna, final Gender gender) {
        this.dna = dna;
        this.gender = gender;
    }

    public Gender getGender() { return this.gender; }

    public UUID getDna() { return this.dna; }

    public static UUID dnaSequenceToUUID(String[] dnaSequence) {
        StringBuilder builder = new StringBuilder();
        for (String s : dnaSequence)
            builder.append(s);
        return UUID.nameUUIDFromBytes(builder.toString().getBytes());
    }

    public static enum Gender {
        MUTANT,
        NON_MUTANT
    }

}
