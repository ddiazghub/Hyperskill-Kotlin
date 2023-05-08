package bullscows;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Input the length of the secret code:");
            Scanner in = new Scanner(System.in);
            int n = in.nextInt();

            if (n == 0)
                throw new BullsAndCowsException.InvalidNumber(Integer.toString(n));

            System.out.println("Input the number of possible symbols in the code:");
            int m = in.nextInt();
            in.nextLine();

            if (n > 36 || m > 36)
                throw new BullsAndCowsException.MoreSymbolsThanPossible();
            else if (n > m)
                throw new BullsAndCowsException.NotEnoughSymbols(n, m);
            else {
                Game game = new Game(n, m);
                System.out.println("The secret is prepared: " + "*".repeat(n) + " (0-" + (m > 10 ? "9, a-" + ((char) ('a' + m - 11)) : ((char) ('0' + m - 1))) + ").");
                System.out.println("Okay, let's start a game!");

                int i = 1;
                Grade grade;

                do {
                    System.out.println("Turn " + i + ":");
                    grade = game.check(in.nextLine());
                    System.out.println(grade);
                } while (grade.bulls() < n);

                System.out.println("Congratulations! You guessed the secret code.");
            }
        } catch (BullsAndCowsException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: \"abc 0 -7\" isn't a valid number.");
        }
    }
}
