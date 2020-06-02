package com.mercadolibre.mutants_exam.repository;

import com.mercadolibre.mutants_exam.entity.HumanGender;
import com.mercadolibre.mutants_exam.entity.HumanGenderStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

@Primary
public abstract class AuditHumanGenderRepositoryDecorator implements IHumanGenderRepository {

    private IHumanGenderRepository delegateHumanGenderRepository;
    private IHumanGenderStatsRepository humanGenderStatsRepository;

    @Autowired
    public AuditHumanGenderRepositoryDecorator(
            @Qualifier("human_gender_repository_delegate") IHumanGenderRepository delegateHumanGenderRepository,
            IHumanGenderStatsRepository humanGenderStatsRepository
    ) {
        this.delegateHumanGenderRepository = delegateHumanGenderRepository;
        this.humanGenderStatsRepository = humanGenderStatsRepository;
    }

    @Override
    @Transactional
    public <S extends HumanGender> S save(@NonNull S entity) {
        // Save the HumanGender
        S saved = delegateHumanGenderRepository.save(entity);
        // Audit the increment of the corresponding detection being one more mutant or one more non-mutant
        Iterable<HumanGenderStat> stats = humanGenderStatsRepository.findAll();
        HumanGenderStat humanGenderStat;
        if (!stats.iterator().hasNext()) {
            humanGenderStat = new HumanGenderStat(0L, 0L);
        } else {
            humanGenderStat = stats.iterator().next();
        }
        if (entity.getGender().equals(HumanGender.Gender.MUTANT)) {
            humanGenderStat.setMutantCount(humanGenderStat.getMutantCount() + 1L);
        } else {
            humanGenderStat.setNonMutantCount(humanGenderStat.getNonMutantCount() + 1L);
        }
        humanGenderStatsRepository.save(humanGenderStat);
        // Finally retrieve the saved entity (HumanGender)
        return saved;
    }

}
