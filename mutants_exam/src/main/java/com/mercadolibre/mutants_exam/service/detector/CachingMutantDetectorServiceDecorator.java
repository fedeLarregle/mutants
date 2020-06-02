package com.mercadolibre.mutants_exam.service.detector;

import com.mercadolibre.mutants_exam.dto.DNASequence;
import com.mercadolibre.mutants_exam.entity.HumanGender;
import com.mercadolibre.mutants_exam.exception.BadDNASequenceException;
import com.mercadolibre.mutants_exam.repository.IHumanGenderRepository;
import com.mercadolibre.mutants_exam.service.audit.IAuditHumanGenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@Service
public class CachingMutantDetectorServiceDecorator implements IMutantDetectorService {

    private final IMutantDetectorService delegateMutantDetectorService;
    private final IAuditHumanGenderService auditHumanGenderService;
    private final IHumanGenderRepository humanGenderRepository;

    @Autowired
    public CachingMutantDetectorServiceDecorator(
            @Qualifier("mutant_detector_service_delegate") IMutantDetectorService delegateMutantDetectorService,
            IAuditHumanGenderService auditHumanGenderService,
            IHumanGenderRepository humanGenderRepository
    ) {
        this.delegateMutantDetectorService = delegateMutantDetectorService;
        this.auditHumanGenderService = auditHumanGenderService;
        this.humanGenderRepository = humanGenderRepository;
    }

    @Override
    public Boolean isMutant(DNASequence dnaSequence) throws BadDNASequenceException {
        // Look for the presence of a HumanGender with the given DNA sequence
        Optional<HumanGender> humanGender = humanGenderRepository.getBydna(
                HumanGender.dnaSequenceToUUID(dnaSequence.getDna())
        );
        // If HumanGender found return if it's MUTANT or NON_MUTANT, there's no need to re-evaluate the DNA sequence
        if (humanGender.isPresent())
            return humanGender.get().getGender().equals(HumanGender.Gender.MUTANT) ? Boolean.TRUE : Boolean.FALSE;

        // Evaluate the DNA sequence
        Boolean isMutant = this.delegateMutantDetectorService.isMutant(dnaSequence);

        // Save the results for future requests
        HumanGender.Gender gender = isMutant ? HumanGender.Gender.MUTANT : HumanGender.Gender.NON_MUTANT;
        HumanGender newHumanGender = new HumanGender(HumanGender.dnaSequenceToUUID(dnaSequence.getDna()), gender);
        auditHumanGenderService.audit(newHumanGender);

        return isMutant;
    }
}
