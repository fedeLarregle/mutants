package com.mercadolibre.mutants_exam.service.detector;

import com.mercadolibre.mutants_exam.dto.DNASequence;
import com.mercadolibre.mutants_exam.exception.BadDNASequenceException;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service("mutant_detector_service_delegate")
public class MutantDetectorService implements IMutantDetectorService {

    public Boolean isMutant(DNASequence dnaSequence) throws BadDNASequenceException {
        final String[] sequences = {"AAAA", "TTTT", "CCCC", "GGGG"};
        final Set<Character> nitrogenousBases = new HashSet<Character>() {{ add('A'); add('T'); add('C'); add('G');}};

        final Set<SequencePosition> sequencePositions = new HashSet<>();

        String[] dna = dnaSequence.getDna();
        int sequenceCounter = 0;
        boolean isMutant = false;

        validateDNASequence(dna, nitrogenousBases);

        outer:
        for (int i = 0; i < dna.length; i++)
            for (int j = 0; j < dna[i].length(); j++)

                for (String sequence : sequences) {

                    Optional<SequencePosition> sequencePosition = foundSequence(dna, i, j, sequence);
                    if (sequencePosition.isPresent()) {
                        if (!sequencePositions.contains(sequencePosition.get()))
                            sequenceCounter++;

                        sequencePositions.add(sequencePosition.get());
                        if (sequenceCounter > 1) {
                            isMutant = true;
                            break outer;
                        }
                    }
                }

        return isMutant;
    }

    private void validateDNASequence(String[] dna, Set<Character> nitrogenousBases) throws BadDNASequenceException {
        for (int i = 0; i < dna.length; i++)
            for (int j = 0; j < dna[i].length(); j++) {
                if (dna.length != dna[i].length())
                    throw new BadDNASequenceException("DNA sequence does not have a square shape.");
                if (!nitrogenousBases.contains(dna[i].charAt(j)))
                    throw new BadDNASequenceException("DNA sequence contains non nitrogenous base: " + dna[i].charAt(j));
            }
    }

    // This function searches in all 8-direction from point
    // (row, col) in dnaMatrix[][] the given sequence
    private Optional<SequencePosition> foundSequence(String[] dna, int row, int col, String sequence) {
        Optional<SequencePosition> sequencePosition = Optional.empty();

        // For searching in all 8 direction
        final int[] x = { -1, -1, -1, 0, 0, 1, 1, 1 };
        final int[] y = { -1, 0, 1, -1, 1, -1, 0, 1 };

        // If first character of word doesn't match with
        // given starting point in grid.
        if (dna[row].charAt(col) != sequence.charAt(0))
            return sequencePosition;

        int len = sequence.length();

        // Search sequence in all 8 directions
        // starting from (row,col)
        for (int dir = 0; dir < 8; dir++)
        {
            // Initialize starting point
            // for current direction
            int k, rd = row + x[dir], cd = col + y[dir];

            // First character is already checked,
            //  match remaining characters
            for (k = 1; k < len; k++)
            {
                // If out of bound break
                if (rd >= dna.length || rd < 0 || cd >= dna[0].length() || cd < 0)
                    break;

                // If not matched, break
                if (dna[rd].charAt(cd) != sequence.charAt(k))
                    break;

                // Moving in particular direction
                rd += x[dir];
                cd += y[dir];
            }

            // If all character matched, then value of must
            // be equal to length of sequence
            if (k == len) {
                sequencePosition = Optional.of(
                        new SequencePosition(
                                new Pair<>(row, col),
                                new Pair<>(row + ((len - 1) * x[dir]), col + ((len - 1) * y[dir]))
                        )
                );
                return sequencePosition;
            }
        }
        return sequencePosition;
    }

    public static class SequencePosition {
        private final Pair<Integer, Integer> start;
        private final Pair<Integer, Integer> end;

        SequencePosition(final Pair<Integer, Integer> start, final Pair<Integer, Integer> end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SequencePosition that = (SequencePosition) o;
            return (Objects.equals(start, that.start) &&
                    Objects.equals(end, that.end)) ||
                    (Objects.equals(start, that.end) &&
                            Objects.equals(end, that.start));
        }

        @Override
        public int hashCode() {
            return (start.hashCode() * end.hashCode());
        }
    }

}
