package challenges;

/* Solution: DFS, tree, caching */

import java.util.*;

public class PolynomialCoefficients {

    private final Map<Monomial, Integer> nbrOfLeavesMap = new HashMap<Monomial, Integer>();

    public static void main(String args[]) {
        PolynomialCoefficients pc = new PolynomialCoefficients();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line1 = sc.nextLine();
            String line2 = sc.nextLine();

            // Read in, and parse the monomial specified as an input to the program.
            Monomial monomial = pc.getMonomial(line1, line2);

            // Calculate the coefficient.
            int coefficient = pc.getCoefficient(monomial);

            System.out.println(coefficient);
        }
    }

    private Monomial getMonomial(String line1, String line2) {
        StringTokenizer st = new StringTokenizer(line1);
        // skip first one which is represents the power, as it is not needed for the calculation of the coefficient.
        st.nextToken();
        int numberOfElts = Integer.parseInt(st.nextToken());
        int powers[] = new int[numberOfElts];
        st = new StringTokenizer(line2);
        int i = 0;
        while (st.hasMoreTokens()) {
            powers[i++] = Integer.parseInt(st.nextToken());
        }

        return new Monomial(powers);
    }

    /**
     * The algorithm will start with a start monomial where each variable is taken to the power of 0. For each variable
     * the algorithm verifies whether it may increase the power by 1, and going one level deeper until the target
     * monomial is reached. Each time the search backtracks the number of leaves of the current non-leaf node is cached.
     * The leaves represent monomials which are the same as the target monomial. Counting the number of leaves gives us
     * the coefficient of the target monomial. Caching is important, otherwise the search space will be too large, and
     * it will take too long before a solution is found.
     *
     * @param monomial the monomial of the expanded polynomial
     *
     * @return the coefficient of the monomial
     */
    private int getCoefficient(Monomial monomial) {
        Monomial startMonomial;
        int powers[] = new int[monomial.getNumberOfVariables()];
        startMonomial = new Monomial(powers);

        nbrOfLeavesMap.clear();
        return getNbrOfLeaves(startMonomial, monomial);
    }

    private int getNbrOfLeaves(Monomial m, Monomial targetMonomial) {
        int totalNbrOfLeaves = 0;

        boolean isLeaf = true;
        for (int i = 0; i < m.getNumberOfVariables(); i++) {
            if (m.getPower(i) + 1 <= targetMonomial.getPower(i)) {
                Monomial cloned = new Monomial(m);
                cloned.increasePower(i);
                Integer nbrLeaves = nbrOfLeavesMap.get(cloned);
                if (nbrLeaves == null) {
                    totalNbrOfLeaves += getNbrOfLeaves(cloned, targetMonomial);
                } else {
                    totalNbrOfLeaves += nbrLeaves;
                }

                isLeaf = false;
            }
        }

        if (isLeaf) {
            return 1;
        }

        nbrOfLeavesMap.put(m, totalNbrOfLeaves);
        return totalNbrOfLeaves;
    }

    private static class Monomial {
        int[] powers;

        public Monomial(int[] powers) {
            this.powers = powers;
        }

        public Monomial(Monomial m) {
            powers = Arrays.copyOf(m.powers, m.getNumberOfVariables());
        }

        public void increasePower(int index) {
            powers[index]++;
        }

        public int getPower(int index) {
            return powers[index];
        }

        public int getNumberOfVariables() {
            return powers.length;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (o.getClass() != Monomial.class) {
                return false;
            }

            Monomial that = (Monomial) o;

            if (this.getNumberOfVariables() != that.getNumberOfVariables()) {
                return false;
            }

            for (int i = 0; i < this.getNumberOfVariables(); i++) {
                if (this.powers[i] != that.powers[i]) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(powers);
        }
    }
}
