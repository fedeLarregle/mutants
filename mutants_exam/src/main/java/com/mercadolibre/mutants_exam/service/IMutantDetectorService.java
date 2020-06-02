package com.mercadolibre.mutants_exam.service;

import com.mercadolibre.mutants_exam.dto.DNASequence;
import com.mercadolibre.mutants_exam.exception.BadDNASequenceException;

public interface IMutantDetectorService {
    Boolean isMutant(DNASequence dnaSequence) throws BadDNASequenceException;
}
