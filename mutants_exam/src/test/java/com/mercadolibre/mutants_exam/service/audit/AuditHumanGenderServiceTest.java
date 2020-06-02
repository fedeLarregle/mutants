package com.mercadolibre.mutants_exam.service.audit;

import com.mercadolibre.mutants_exam.dto.DNASequence;
import com.mercadolibre.mutants_exam.entity.HumanGender;
import com.mercadolibre.mutants_exam.entity.HumanGenderStat;
import com.mercadolibre.mutants_exam.repository.IHumanGenderRepository;
import com.mercadolibre.mutants_exam.repository.IHumanGenderStatsRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuditHumanGenderServiceTest {

    @Test
    void auditMutantIncrementsMutantCounter() {
        // Arrange
        IHumanGenderRepository humanGenderRepositoryMock = Mockito.mock(IHumanGenderRepository.class);
        IHumanGenderStatsRepository humanGenderStatsRepositoryMock = Mockito.mock(IHumanGenderStatsRepository.class);
        IAuditHumanGenderService auditHumanGenderService = new AuditHumanGenderService(
                humanGenderRepositoryMock,
                humanGenderStatsRepositoryMock
        );
        DNASequence dnaSequence = new DNASequence(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});
        HumanGender.Gender gender = HumanGender.Gender.MUTANT;
        HumanGender humanGender = new HumanGender(HumanGender.dnaSequenceToUUID(dnaSequence.getDna()), gender);

        // Act
        Mockito.when(humanGenderRepositoryMock.save(Mockito.any(HumanGender.class))).thenReturn(humanGender);
        auditHumanGenderService.audit(humanGender);
        // Assert
        ArgumentCaptor<HumanGenderStat> humanGenderStatArgumentCaptor = ArgumentCaptor.forClass(HumanGenderStat.class);
        Mockito.verify(humanGenderStatsRepositoryMock, Mockito.times(1)).save(humanGenderStatArgumentCaptor.capture());
        assert humanGenderStatArgumentCaptor.getValue().getMutantCount().equals(1L);
        assert humanGenderStatArgumentCaptor.getValue().getNonMutantCount().equals(0L);

        Mockito.verify(humanGenderRepositoryMock, Mockito.times(1)).save(Mockito.any(HumanGender.class));
    }

    @Test
    void auditNonMutantIncrementsNonMutantCounter() {
        // Arrange
        IHumanGenderRepository humanGenderRepositoryMock = Mockito.mock(IHumanGenderRepository.class);
        IHumanGenderStatsRepository humanGenderStatsRepositoryMock = Mockito.mock(IHumanGenderStatsRepository.class);
        IAuditHumanGenderService auditHumanGenderService = new AuditHumanGenderService(
                humanGenderRepositoryMock,
                humanGenderStatsRepositoryMock
        );
        DNASequence dnaSequence = new DNASequence(new String[]{"ATGCGA", "CAGTGC", "TGCTGT", "AGAAGG", "TAGCTA", "TCACTG"});
        HumanGender.Gender gender = HumanGender.Gender.NON_MUTANT;
        HumanGender humanGender = new HumanGender(HumanGender.dnaSequenceToUUID(dnaSequence.getDna()), gender);

        // Act
        Mockito.when(humanGenderRepositoryMock.save(Mockito.any(HumanGender.class))).thenReturn(humanGender);
        auditHumanGenderService.audit(humanGender);
        // Assert
        ArgumentCaptor<HumanGenderStat> humanGenderStatArgumentCaptor = ArgumentCaptor.forClass(HumanGenderStat.class);
        Mockito.verify(humanGenderStatsRepositoryMock, Mockito.times(1)).save(humanGenderStatArgumentCaptor.capture());
        assert humanGenderStatArgumentCaptor.getValue().getMutantCount().equals(0L);
        assert humanGenderStatArgumentCaptor.getValue().getNonMutantCount().equals(1L);

        Mockito.verify(humanGenderRepositoryMock, Mockito.times(1)).save(Mockito.any(HumanGender.class));
    }

}
