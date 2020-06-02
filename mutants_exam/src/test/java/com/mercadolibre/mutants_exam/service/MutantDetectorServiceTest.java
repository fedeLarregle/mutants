package com.mercadolibre.mutants_exam.service;

import com.mercadolibre.mutants_exam.dto.DNASequence;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MutantDetectorServiceTest {

    @Test
    void isMutantReturnsTrueForMutantSequence() {
        // Arrange
        MutantDetectorService mutantDetectorService = new MutantDetectorService();
        DNASequence dnaSequence = new DNASequence(
                new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"}
        );
        // Act
        Boolean isMutant = mutantDetectorService.isMutant(dnaSequence);
        // Assert
        assertEquals(isMutant, Boolean.TRUE);
    }

    @Test
    void isMutantReturnsFalseForNonMutantSequence() {
        // Arrange
        MutantDetectorService mutantDetectorService = new MutantDetectorService();
        DNASequence dnaSequence = new DNASequence(
                new String[]{"ATGCGA", "CAGTGC", "TGCTGT", "AGAAGG", "TAGCTA", "TCACTG"}
        );
        // Act
        Boolean isMutant = mutantDetectorService.isMutant(dnaSequence);
        // Assert
        assertEquals(isMutant, Boolean.FALSE);
    }
}