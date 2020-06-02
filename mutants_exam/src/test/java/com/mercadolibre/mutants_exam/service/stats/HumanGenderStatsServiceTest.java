package com.mercadolibre.mutants_exam.service.stats;

import com.mercadolibre.mutants_exam.dto.HumanGenderStats;
import com.mercadolibre.mutants_exam.entity.HumanGenderStat;
import com.mercadolibre.mutants_exam.repository.IHumanGenderStatsRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
class HumanGenderStatsServiceTest {

    @Test
    public void getStatsRetrievesExistingCounters() {
        // Arrange
        IHumanGenderStatsRepository humanGenderStatsRepositoryMock = Mockito.mock(IHumanGenderStatsRepository.class);
        IHumanGenderStatsService humanGenderStatsService = new HumanGenderStatsService(humanGenderStatsRepositoryMock);
        Iterable<HumanGenderStat> humanGenderStats = Arrays.asList(new HumanGenderStat(3L, 1L));
        // Act
        Mockito.when(humanGenderStatsRepositoryMock.findAll()).thenReturn(humanGenderStats);
        HumanGenderStats stats = humanGenderStatsService.getStats();
        // Assert
        assert stats.getCountHumanDna().equals(3L);
        assert stats.getCountMutantDna().equals(1L);
        assert stats.getRatio().equals(0.25D);
    }

    @Test
    public void getStatsRetrievesDefaultCountersForTheFirstTime() {
        // Arrange
        IHumanGenderStatsRepository humanGenderStatsRepositoryMock = Mockito.mock(IHumanGenderStatsRepository.class);
        IHumanGenderStatsService humanGenderStatsService = new HumanGenderStatsService(humanGenderStatsRepositoryMock);
        Iterable<HumanGenderStat> humanGenderStats = Arrays.asList();
        // Act
        Mockito.when(humanGenderStatsRepositoryMock.findAll()).thenReturn(humanGenderStats);
        HumanGenderStats stats = humanGenderStatsService.getStats();
        // Assert
        assert stats.getCountHumanDna().equals(HumanGenderStats.NULL.getCountHumanDna());
        assert stats.getCountMutantDna().equals(HumanGenderStats.NULL.getCountMutantDna());
        assert stats.getRatio().equals(HumanGenderStats.NULL.getRatio());
    }

}
