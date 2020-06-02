package com.mercadolibre.mutants_exam.service.detector;

import com.mercadolibre.mutants_exam.dto.DNASequence;
import com.mercadolibre.mutants_exam.entity.HumanGender;
import com.mercadolibre.mutants_exam.exception.BadDNASequenceException;
import com.mercadolibre.mutants_exam.repository.IHumanGenderRepository;
import com.mercadolibre.mutants_exam.service.audit.IAuditHumanGenderService;
import com.mercadolibre.mutants_exam.service.detector.CachingMutantDetectorServiceDecorator;
import com.mercadolibre.mutants_exam.service.detector.IMutantDetectorService;
import com.mercadolibre.mutants_exam.service.detector.MutantDetectorService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
class CachingMutantDetectorServiceDecoratorTest {

    @Test
    void isMutantReturnsTrueForMutantSequenceNotFoundInCache() throws BadDNASequenceException {
        // Arrange
        IMutantDetectorService mutantDetectorServiceMock = Mockito.mock(MutantDetectorService.class);
        IAuditHumanGenderService auditHumanGenderServiceMock = Mockito.mock(IAuditHumanGenderService.class);
        IHumanGenderRepository humanGenderRepositoryMock = Mockito.mock(IHumanGenderRepository.class);
        CachingMutantDetectorServiceDecorator sut = new CachingMutantDetectorServiceDecorator(
                mutantDetectorServiceMock,
                auditHumanGenderServiceMock,
                humanGenderRepositoryMock
        );
        DNASequence dnaSequence = new DNASequence(
                new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"}
        );
        HumanGender humanGender = new HumanGender(HumanGender.dnaSequenceToUUID(dnaSequence.getDna()), HumanGender.Gender.MUTANT);
        // Act
        Mockito.when(humanGenderRepositoryMock.getBydna(HumanGender.dnaSequenceToUUID(dnaSequence.getDna())))
                .thenReturn(Optional.empty());
        Mockito.when(mutantDetectorServiceMock.isMutant(dnaSequence)).thenReturn(Boolean.TRUE);
        Mockito.when(auditHumanGenderServiceMock.audit(humanGender)).thenReturn(humanGender);
        Boolean isMutant = sut.isMutant(dnaSequence);
        // Assert
        ArgumentCaptor<HumanGender> humanGenderArgumentCaptor = ArgumentCaptor.forClass(HumanGender.class);
        Mockito.verify(mutantDetectorServiceMock, Mockito.times(1)).isMutant(dnaSequence);
        Mockito.verify(auditHumanGenderServiceMock, Mockito.times(1)).audit(humanGenderArgumentCaptor.capture());
        assert humanGenderArgumentCaptor.getValue().getGender().equals(humanGender.getGender());
        assert humanGenderArgumentCaptor.getValue().getDna().equals(humanGender.getDna());
        assert isMutant.equals(Boolean.TRUE);
    }

    @Test
    void isMutantReturnsFalseForNonMutantSequenceNotFoundInCache() throws BadDNASequenceException {
        // Arrange
        IMutantDetectorService mutantDetectorServiceMock = Mockito.mock(MutantDetectorService.class);
        IAuditHumanGenderService auditHumanGenderServiceMock = Mockito.mock(IAuditHumanGenderService.class);
        IHumanGenderRepository humanGenderRepositoryMock = Mockito.mock(IHumanGenderRepository.class);
        CachingMutantDetectorServiceDecorator sut = new CachingMutantDetectorServiceDecorator(
                mutantDetectorServiceMock,
                auditHumanGenderServiceMock,
                humanGenderRepositoryMock
        );
        DNASequence dnaSequence = new DNASequence(
                new String[]{"ATGCGA", "CAGTGC", "TGCTGT", "AGAAGG", "TAGCTA", "TCACTG"}
        );
        HumanGender humanGender = new HumanGender(HumanGender.dnaSequenceToUUID(dnaSequence.getDna()), HumanGender.Gender.NON_MUTANT);
        // Act
        Mockito.when(humanGenderRepositoryMock.getBydna(HumanGender.dnaSequenceToUUID(dnaSequence.getDna())))
                .thenReturn(Optional.empty());
        Mockito.when(mutantDetectorServiceMock.isMutant(dnaSequence)).thenReturn(Boolean.FALSE);
        Mockito.when(auditHumanGenderServiceMock.audit(humanGender)).thenReturn(humanGender);
        Boolean isMutant = sut.isMutant(dnaSequence);
        // Assert
        ArgumentCaptor<HumanGender> humanGenderArgumentCaptor = ArgumentCaptor.forClass(HumanGender.class);
        Mockito.verify(mutantDetectorServiceMock, Mockito.times(1)).isMutant(dnaSequence);
        Mockito.verify(auditHumanGenderServiceMock, Mockito.times(1)).audit(humanGenderArgumentCaptor.capture());
        assert humanGenderArgumentCaptor.getValue().getGender().equals(humanGender.getGender());
        assert humanGenderArgumentCaptor.getValue().getDna().equals(humanGender.getDna());
        assert isMutant.equals(Boolean.FALSE);
    }

    @Test
    void isMutantReturnsTrueForMutantSequenceFoundInCache() throws BadDNASequenceException {
        // Arrange
        IMutantDetectorService mutantDetectorServiceMock = Mockito.mock(MutantDetectorService.class);
        IAuditHumanGenderService auditHumanGenderServiceMock = Mockito.mock(IAuditHumanGenderService.class);
        IHumanGenderRepository humanGenderRepositoryMock = Mockito.mock(IHumanGenderRepository.class);
        CachingMutantDetectorServiceDecorator sut = new CachingMutantDetectorServiceDecorator(
                mutantDetectorServiceMock,
                auditHumanGenderServiceMock,
                humanGenderRepositoryMock
        );
        DNASequence dnaSequence = new DNASequence(
                new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"}
        );
        HumanGender humanGender = new HumanGender(HumanGender.dnaSequenceToUUID(dnaSequence.getDna()), HumanGender.Gender.MUTANT);
        // Act
        Mockito.when(humanGenderRepositoryMock.getBydna(HumanGender.dnaSequenceToUUID(dnaSequence.getDna())))
                .thenReturn(Optional.of(humanGender));
        Mockito.when(mutantDetectorServiceMock.isMutant(dnaSequence)).thenReturn(Boolean.TRUE);
        Mockito.when(auditHumanGenderServiceMock.audit(humanGender)).thenReturn(humanGender);
        Boolean isMutant = sut.isMutant(dnaSequence);
        // Assert
        Mockito.verify(mutantDetectorServiceMock, Mockito.never()).isMutant(dnaSequence);
        Mockito.verify(auditHumanGenderServiceMock, Mockito.never()).audit(humanGender);
        assert isMutant.equals(Boolean.TRUE);
    }

    @Test
    void isMutantReturnsFalseForNonMutantSequenceFoundInCache() throws BadDNASequenceException {
        // Arrange
        IMutantDetectorService mutantDetectorServiceMock = Mockito.mock(MutantDetectorService.class);
        IAuditHumanGenderService auditHumanGenderServiceMock = Mockito.mock(IAuditHumanGenderService.class);
        IHumanGenderRepository humanGenderRepositoryMock = Mockito.mock(IHumanGenderRepository.class);
        CachingMutantDetectorServiceDecorator sut = new CachingMutantDetectorServiceDecorator(
                mutantDetectorServiceMock,
                auditHumanGenderServiceMock,
                humanGenderRepositoryMock
        );
        DNASequence dnaSequence = new DNASequence(
                new String[]{"ATGCGA", "CAGTGC", "TGCTGT", "AGAAGG", "TAGCTA", "TCACTG"}
        );
        HumanGender humanGender = new HumanGender(HumanGender.dnaSequenceToUUID(dnaSequence.getDna()), HumanGender.Gender.NON_MUTANT);
        // Act
        Mockito.when(humanGenderRepositoryMock.getBydna(HumanGender.dnaSequenceToUUID(dnaSequence.getDna())))
                .thenReturn(Optional.of(humanGender));
        Mockito.when(mutantDetectorServiceMock.isMutant(dnaSequence)).thenReturn(Boolean.FALSE);
        Mockito.when(auditHumanGenderServiceMock.audit(humanGender)).thenReturn(humanGender);
        Boolean isMutant = sut.isMutant(dnaSequence);
        // Assert
        Mockito.verify(mutantDetectorServiceMock, Mockito.never()).isMutant(dnaSequence);
        Mockito.verify(auditHumanGenderServiceMock, Mockito.never()).audit(humanGender);
        assert isMutant.equals(Boolean.FALSE);
    }
}
