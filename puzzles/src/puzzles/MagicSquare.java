package puzzles;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MagicSquare {
    private final int order;
    private final int nbrOfIntegers;

    public MagicSquare(final int order) {
        if (order <= 2) {
            throw new IllegalArgumentException("order should be greater than 2");
        }
        this.order = order;
        nbrOfIntegers = order * order;
    }

    public void solveUsingExhaustiveSearch() {
        solveUsingExhaustiveSearch(new ArrayList<>());
    }

    private void solveUsingExhaustiveSearch(final List<Integer> chosenNumbers) {
        if (chosenNumbers.size() == nbrOfIntegers) {
            checkForValidSolution(chosenNumbers);
            return;
        }

        final Set<Integer> numbers = buildNumbers(chosenNumbers);
        numbers.forEach(number -> {
            chosenNumbers.add(number);
            solveUsingExhaustiveSearch(chosenNumbers);
            chosenNumbers.remove(chosenNumbers.size() - 1);
        });
    }

    private Set<Integer> buildNumbers(final List<Integer> chosenNumbers) {
        final Set<Integer> numbers = new LinkedHashSet<>();
        for (int i = 1; i <= nbrOfIntegers; i++) {
            numbers.add(i);
        }
        numbers.removeAll(chosenNumbers);
        return numbers;
    }

    private void checkForValidSolution(final List<Integer> candidateSolution) {
        if (isValid(candidateSolution)) {
            System.out.println(candidateSolution);
        }
    }

    private boolean isValid(final List<Integer> candidateSolution) {
        int firstSum = 0;
        int corSumTopLeftToBottomRight = 0;
        int colSumTopRightToBottomLeft = 0;
        for (int i = 1; i <= order; i++) {
            int sumRow = 0;
            int sumColumn = 0;
            for (int y = 1; y <= order; y++) {
                sumRow += candidateSolution.get(calculateListIndex(i, y));
                sumColumn += candidateSolution.get(calculateListIndex(y, i));
            }

            if (firstSum == 0) {
                firstSum = sumRow;
            }

            if (firstSum != sumRow || firstSum != sumColumn) {
                return false;
            }

            corSumTopLeftToBottomRight += candidateSolution.get(calculateListIndex(i, i));
            colSumTopRightToBottomLeft += candidateSolution.get(calculateListIndex(i, order - i + 1));
        }

        return firstSum == corSumTopLeftToBottomRight && firstSum == colSumTopRightToBottomLeft;
    }

    private int calculateListIndex(final int rowIndex, final int columnIndex) {
        return (rowIndex - 1) * order + (columnIndex - 1);
    }

    public static void main(String[] args) {
        final MagicSquare magicSquare = new MagicSquare(3);
        magicSquare.solveUsingExhaustiveSearch();
    }
}
