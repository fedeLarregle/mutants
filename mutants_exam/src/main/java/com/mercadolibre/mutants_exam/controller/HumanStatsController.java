package com.mercadolibre.mutants_exam.controller;

import com.mercadolibre.mutants_exam.dto.HumanGenderStats;
import com.mercadolibre.mutants_exam.service.stats.IHumanGenderStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HumanStatsController {

    private final IHumanGenderStatsService humanGenderStatsService;

    @Autowired
    public HumanStatsController(IHumanGenderStatsService humanGenderStatsService) {
        this.humanGenderStatsService = humanGenderStatsService;
    }

    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<HumanGenderStats> getStats() {
        return ResponseEntity.ok(this.humanGenderStatsService.getStats());
    }
}
