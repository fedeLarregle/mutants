package com.mercadolibre.mutants_exam.service.detector;

import com.mercadolibre.mutants_exam.dto.DNASequence;
import com.mercadolibre.mutants_exam.exception.BadDNASequenceException;

public interface IMutantDetectorService {
    Boolean isMutant(DNASequence dnaSequence) throws BadDNASequenceException;
}
