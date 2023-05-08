package bullscows;

public record Grade(int bulls, int cows) {
    public String toString() {
        if (bulls > 0 && cows > 0) {
            return "Grade: " + bulls + " bull(s) and " + cows + " cow(s).";
        } else if (bulls > 0) {
            return "Grade: " + bulls + " bull(s).";
        } else if (cows > 0) {
            return "Grade: " + cows + " cow(s).";
        } else return "Grade: None.";
    }
}