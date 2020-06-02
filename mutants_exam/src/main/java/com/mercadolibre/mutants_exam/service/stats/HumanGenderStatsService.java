package com.mercadolibre.mutants_exam.service.stats;

import com.mercadolibre.mutants_exam.dto.HumanGenderStats;
import com.mercadolibre.mutants_exam.entity.HumanGenderStat;
import com.mercadolibre.mutants_exam.repository.IHumanGenderStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanGenderStatsService implements IHumanGenderStatsService {

    private final IHumanGenderStatsRepository humanGenderStatsRepository;

    @Autowired
    public HumanGenderStatsService(IHumanGenderStatsRepository humanGenderStatsRepository) {
        this.humanGenderStatsRepository = humanGenderStatsRepository;
    }

    @Override
    public HumanGenderStats getStats() {
        HumanGenderStat humanGenderStat;
        Iterable<HumanGenderStat> humanGenderStats = humanGenderStatsRepository.findAll();

        if (!humanGenderStats.iterator().hasNext()) {
            return HumanGenderStats.NULL;
        }

        humanGenderStat = humanGenderStats.iterator().next();

        Long mutantCount = humanGenderStat.getMutantCount();
        Long nonMutantCount = humanGenderStat.getNonMutantCount();
        Double ratio = ((double)mutantCount / (mutantCount + nonMutantCount));

        return new HumanGenderStats(mutantCount, nonMutantCount, ratio);
    }
}
