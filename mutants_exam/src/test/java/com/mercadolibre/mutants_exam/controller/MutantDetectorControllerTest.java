package com.mercadolibre.mutants_exam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mercadolibre.mutants_exam.dto.DNASequence;
import com.mercadolibre.mutants_exam.exception.BadDNASequenceException;
import com.mercadolibre.mutants_exam.service.detector.IMutantDetectorService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MutantDetectorController.class)
class MutantDetectorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMutantDetectorService mutantDetectorServiceMock;

    @InjectMocks
    private MutantDetectorController mutantDetectorController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(mutantDetectorController).build();
    }

    @Test
    public void isMutantReturns200OKGivenAMutantDNASequence() throws Exception {
        // Arrange
        DNASequence dnaSequence = new DNASequence(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        // Arrange/Act/Assert - I know it's an all in one this builder
        Mockito.when(mutantDetectorServiceMock.isMutant(Mockito.any(DNASequence.class))).thenReturn(Boolean.TRUE);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(dnaSequence))
        )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn();
    }

    @Test
    public void isMutantReturns403FORBIDDENGivenANonMutantDNASequence() throws Exception {
        // Arrange
        DNASequence dnaSequence = new DNASequence(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"});
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        // Arrange/Act/Assert - I know it's an all in one this builder
        Mockito.when(mutantDetectorServiceMock.isMutant(Mockito.any(DNASequence.class))).thenReturn(Boolean.FALSE);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(dnaSequence))
        )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()))
                .andReturn();
    }

    @Test
    public void isMutantReturns404GivenADNASequenceWithNonNitrogenousBases() throws Exception {
        // Arrange
        DNASequence dnaSequence = new DNASequence(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "EEEEEE"});
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        // Arrange/Act/Assert - I know it's an all in one this builder
        Mockito.when(mutantDetectorServiceMock.isMutant(Mockito.any(DNASequence.class)))
                .thenThrow(new BadDNASequenceException("DNA sequence contains non nitrogenous base: E"));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(dnaSequence))
        )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

    @Test
    public void isMutantReturns404GivenADNASequenceWithNonSquareShape() throws Exception {
        // Arrange
        DNASequence dnaSequence = new DNASequence(new String[]{"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG"});
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        // Arrange/Act/Assert - I know it's an all in one this builder
        Mockito.when(mutantDetectorServiceMock.isMutant(Mockito.any(DNASequence.class)))
                .thenThrow(new BadDNASequenceException("DNA sequence does not have a square shape."));

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(dnaSequence))
        )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                .andReturn();
    }

}
