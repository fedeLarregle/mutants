package com.mercadolibre.mutants_exam.controller;

import com.mercadolibre.mutants_exam.dto.DNASequence;
import com.mercadolibre.mutants_exam.exception.BadDNASequenceException;
import com.mercadolibre.mutants_exam.service.detector.IMutantDetectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class MutantDetectorController {
    @Autowired
    private IMutantDetectorService mutantDetectorService;

    @RequestMapping(value = "/mutant", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity isMutant(@RequestBody DNASequence dna) {
        Boolean isMutant = Boolean.FALSE;
        try {
            isMutant = this.mutantDetectorService.isMutant(dna);
        } catch (BadDNASequenceException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        // We might want to change the non-mutant return status (HttpStatus.FORBIDDEN) for a better alternative
        return isMutant ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

}
