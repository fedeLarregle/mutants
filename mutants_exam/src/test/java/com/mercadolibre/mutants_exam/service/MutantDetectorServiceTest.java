package com.mercadolibre.mutants_exam.service;

import com.mercadolibre.mutants_exam.dto.DNASequence;
import com.mercadolibre.mutants_exam.exception.BadDNASequenceException;
import com.mercadolibre.mutants_exam.service.detector.MutantDetectorService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MutantDetectorServiceTest {

    @Test
    void isMutantReturnsTrueForMutantSequence() throws BadDNASequenceException {
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
    void isMutantReturnsFalseForNonMutantSequence() throws BadDNASequenceException {
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

    @Test
    void isMutantThrowsBadDNASequenceExceptionWhenNonNitrogenousBaseIsProvided() {
        // Arrange
        MutantDetectorService mutantDetectorService = new MutantDetectorService();
        DNASequence dnaSequence = new DNASequence(
                new String[]{"ATGCGA", "CAGTGC", "TGCTGT", "TGCTGT", "TGCTGT", "EEEEEE"}
        );
        // Act/ Assert
        BadDNASequenceException badDNASequenceException = assertThrows(
                BadDNASequenceException.class,
                () -> { mutantDetectorService.isMutant(dnaSequence); }
        );
        // Assert
        String expectedMessage = "DNA sequence contains non nitrogenous base: E";
        assertEquals(badDNASequenceException.getMessage(), expectedMessage);
    }

    @Test
    void isMutantThrowsBadDNASequenceExceptionWhenANonSquareDNASequenceIsProvided() {
        // Arrange
        MutantDetectorService mutantDetectorService = new MutantDetectorService();
        DNASequence dnaSequence = new DNASequence(
                new String[]{"ATGCGA", "CAGTGC", "TGCTGT", "TGCTGT", "TGCTGT"}
        );
        // Act/ Assert
        BadDNASequenceException badDNASequenceException = assertThrows(
                BadDNASequenceException.class,
                () -> { mutantDetectorService.isMutant(dnaSequence); }
        );
        // Assert
        String expectedMessage = "DNA sequence does not have a square shape.";
        assertEquals(badDNASequenceException.getMessage(), expectedMessage);
    }
}
