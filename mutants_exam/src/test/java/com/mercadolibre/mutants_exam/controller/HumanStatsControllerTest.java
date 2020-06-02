package com.mercadolibre.mutants_exam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mercadolibre.mutants_exam.dto.HumanGenderStats;
import com.mercadolibre.mutants_exam.service.stats.IHumanGenderStatsService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HumanStatsController.class)
class HumanStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IHumanGenderStatsService humanGenderStatsService;

    @InjectMocks
    private HumanStatsController humanStatsController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(humanStatsController).build();
    }

    @Test
    public void isMutantReturnsZeroCountsForTheFirstTime() throws Exception {
        // Arrange
        HumanGenderStats humanGenderStats = new HumanGenderStats(0L, 0L, 0D);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        // Arrange/Act/Assert - I know it's an all in one this builder
        Mockito.when(humanGenderStatsService.getStats()).thenReturn(humanGenderStats);
        mockMvc.perform(MockMvcRequestBuilders.get("/stats"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectWriter.writeValueAsString(humanGenderStats)))
                .andReturn();
    }

    @Test
    public void isMutantReturnsExistingCounters() throws Exception {
        // Arrange
        HumanGenderStats humanGenderStats = new HumanGenderStats(5L, 2L, 0.71D);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

        // Arrange/Act/Assert - I know it's an all in one this builder
        Mockito.when(humanGenderStatsService.getStats()).thenReturn(humanGenderStats);
        mockMvc.perform(MockMvcRequestBuilders.get("/stats"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectWriter.writeValueAsString(humanGenderStats)))
                .andReturn();
    }

}
