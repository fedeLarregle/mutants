package com.mercadolibre.mutants_exam.service;

import com.mercadolibre.mutants_exam.dto.DNASequence;

public interface IMutantDetectorService {
    Boolean isMutant(DNASequence dnaSequence);
}
