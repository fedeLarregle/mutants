package com.mercadolibre.mutants_exam.service.audit;

import com.mercadolibre.mutants_exam.entity.HumanGender;
import com.mercadolibre.mutants_exam.entity.HumanGenderStat;
import com.mercadolibre.mutants_exam.repository.IHumanGenderRepository;
import com.mercadolibre.mutants_exam.repository.IHumanGenderStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuditHumanGenderService implements IAuditHumanGenderService {

    private final IHumanGenderRepository humanGenderRepository;
    private final IHumanGenderStatsRepository humanGenderStatsRepository;

    @Autowired
    public AuditHumanGenderService(
            IHumanGenderRepository humanGenderRepository,
            IHumanGenderStatsRepository humanGenderStatsRepository
    ) {
        this.humanGenderRepository = humanGenderRepository;
        this.humanGenderStatsRepository = humanGenderStatsRepository;
    }

    @Override
    @Transactional
    public HumanGender audit(HumanGender humanGender) {
        // Save the HumanGender
        HumanGender saved = humanGenderRepository.save(humanGender);
        // Audit the increment of the corresponding detection being one more mutant or one more non-mutant
        Optional<HumanGenderStat> optionalHumanGenderStat = humanGenderStatsRepository.findById(1L);
        HumanGenderStat humanGenderStat = optionalHumanGenderStat.orElse(new HumanGenderStat(0L, 0L));
        if (humanGender.getGender().equals(HumanGender.Gender.MUTANT)) {
            humanGenderStat.setMutantCount(humanGenderStat.getMutantCount() + 1L);
        } else {
            humanGenderStat.setNonMutantCount(humanGenderStat.getNonMutantCount() + 1L);
        }
        humanGenderStatsRepository.save(humanGenderStat);
        // Finally retrieve the saved entity (HumanGender)
        return saved;
    }
}
