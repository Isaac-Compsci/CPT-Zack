import java.io.*;
import java.util.*;

public class testcpt{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            printMenu();

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                selectThemeAndPlayGame(scanner);
            } else if (choice.equals("2")) {
                printMessage("High Scores feature is not implemented yet.");
            } else if (choice.equals("3")) {
                printMessage("Help: Guess the word by entering one letter at a time. You have limited attempts. Good luck!");
            } else if (choice.equals("4")) {
                printMessage("Exiting the application. Goodbye!");
                exit = true;
            } else {
                printMessage("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        printMessage("Welcome to Hangman!!\n1. Play Game\n2. High Scores\n3. Help\n4. Exit Application\nWhat do you choose:");
    }

    private static void printMessage(String message) {
        for (String line : message.split("\\n")) {
            System.out.println(line);
        }
    }

    private static void selectThemeAndPlayGame(Scanner scanner) {
        printMessage("Select a theme:\n1. Superheroes\n2. Animals\n3. Countries\nWhat do you choose:");
        String themeChoice = scanner.nextLine();

        String filename = null;
        if (themeChoice.equals("1")) {
            filename = "superheroes.txt";
        } else if (themeChoice.equals("2")) {
            filename = "animals.txt";
        } else if (themeChoice.equals("3")) {
            filename = "countries.txt";
        } else {
            printMessage("Invalid theme choice. Returning to main menu.");
            return;
        }

        Collection<String> words = loadWordsFromFile(filename);
        if (words == null || words.isEmpty()) {
            printMessage("Error: No words available for the selected theme.");
            return;
        }

        playGame(scanner, words);
    }

    private static void playGame(Scanner scanner, Collection<String> words) {
        String wordToGuess = getRandomWord(words);
        char[] wordArray = wordToGuess.toCharArray();
        char[] guessedWord = new char[wordToGuess.length()];
        Arrays.fill(guessedWord, '_');

        int maxAttempts = 6;
        int attemptsLeft = maxAttempts;
        boolean wordGuessed = false;
        Set<Character> guessedLetters = new HashSet<>();

        String[] hangmanStages = getHangmanStages();

        printMessage("Welcome to Hangman! Try to guess the word, one letter at a time.");

        while (attemptsLeft > 0 && !wordGuessed) {
            printMessage(hangmanStages[maxAttempts - attemptsLeft]);
            printMessage("Word: " + String.valueOf(guessedWord));
            printMessage("Guesses left: " + attemptsLeft);
            printMessage("What’s your next guess:");

            String input = scanner.nextLine().toLowerCase();

            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                printMessage("Please enter a single valid letter.");
                continue;
            }

            char guessedLetter = input.charAt(0);

            if (guessedLetters.contains(guessedLetter)) {
                printMessage("You already guessed that letter.");
                continue;
            }

            guessedLetters.add(guessedLetter);

            if (wordToGuess.indexOf(guessedLetter) >= 0) {
                printMessage("Good guess!");
                for (int i = 0; i < wordArray.length; i++) {
                    if (wordArray[i] == guessedLetter) {
                        guessedWord[i] = guessedLetter;
                    }
                }
            } else {
                printMessage("Wrong guess.");
                attemptsLeft--;
            }

            if (String.valueOf(guessedWord).equals(wordToGuess)) {
                wordGuessed = true;
            }
        }

        if (wordGuessed) {
            displayWinScreen(wordToGuess, scanner);
        } else {
            printMessage(hangmanStages[maxAttempts]);
            printMessage("\nGame over! The word was: " + wordToGuess);
        }
    }

    private static void displayWinScreen(String wordToGuess, Scanner scanner) {
        printMessage("DC Universe\n___________\n  |\t\t  |\n O\t\t  |\n/ | \t  |\n   \\	  |\n\t\t  |\n\t\t  |\n\t\t  |\n\t\t\nWord: " + wordToGuess +
                     "\n\nCongratulations!!\nYou won!\nPlay Again?(1)\nHome(2)\nWhat is your choice:");

        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            selectThemeAndPlayGame(scanner);
        } else if (choice.equals("2")) {
            printMenu();
        }
    }

    private static Collection<String> loadWordsFromFile(String filename) {
        Collection<String> words = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (Exception e) {
            return null;
        }
        return words;
    }

    private static String getRandomWord(Collection<String> words) {
        return new ArrayList<>(words).get(new Random().nextInt(words.size()));
    }

    private static String[] getHangmanStages() {
        return new String[] {
            "\n___________\n  |\t\t  |\n\n\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |",
            "\n___________\n  |\t\t  |\n O\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |",
            "\n___________\n  |\t\t  |\n O\t\t  |\n/ \t\t  |\n\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |",
            "\n___________\n  |\t\t  |\n O\t\t  |\n/ |\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |",
            "\n___________\n  |\t\t  |\n O\t\t  |\n/ | \t  |\n\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |",
            "\n___________\n  |\t\t  |\n O\t\t  |\n/ | \t  |\n /\t\t  |\n\t\t  |\n\t\t  |\n\t\t  |",
            "\n___________\n  |\t\t  |\n O\t\t  |\n/ | \t  |\n / \t  |\n\t\t  |\n\t\t  |\n\t\t  |"
        };
    }
}

