import arc.*;
import java.util.*;

public class HangmanGame {

    public static void main(String[] args) {
        Console con = new Console();

        String leaderboardFileName = "leaderboard.txt";
        String themesFileName = "themes.txt";

        TextInputFile themesFile = new TextInputFile(themesFileName);
        String[] themes = new String[10]; // Max 10 themes for simplicity
        int themeCount = 0;

        while (!themesFile.eof()) {
            themes[themeCount] = themesFile.readLine();
            themeCount++;
        }
        themesFile.close();

        while (true) {
            con.clear();
            con.println("|---------------------|");
            con.println("| Welcome to Hangman! |");
            con.println("|---------------------|");
            con.println("| 1. Play Game        |");
            con.println("| 2. Leaderboard      |");
            con.println("| 3. Help             |");
            con.println("| 4. Secret Option    |");
            con.println("| 5. Quit             |");
            con.println("|---------------------|");
            con.print("Enter your choice: ");

            int choice = con.readInt();

            if (choice == 1) {
                con.clear();
                playGame(con, themes, themeCount, leaderboardFileName);
            } else if (choice == 2) {
                con.clear();
                showLeaderboard(con, leaderboardFileName);
            } else if (choice == 3) {
                con.clear();
                showHelp(con);
            } else if (choice == 4) {
                con.clear();
                secretOption(con);
            } else if (choice == 5) {
                con.clear();
                con.println("Thanks for playing!");
                break;
            } else {
                con.clear();
                con.println("Invalid choice. Try again.");
            }
        }
    }

    private static void playGame(Console con, String[] themes, int themeCount, String leaderboardFileName) {
        con.println("Available themes:");
        for (int i = 0; i < themeCount; i++) {
            con.println((i + 1) + ". " + themes[i]);
        }
        con.print("Choose a theme (enter the number): ");

        int themeChoice = con.readInt();

        if (themeChoice < 1 || themeChoice > themeCount) {
            con.clear();
            con.println("Invalid choice. Returning to main menu.");
            con.sleep(2000);
            return;
        }

        String themeFileName = themes[themeChoice - 1] + ".txt";
        TextInputFile themeFile = new TextInputFile(themeFileName);

        String[] words = new String[100]; // Max 100 words for simplicity
        int wordCount = 0;
        while (!themeFile.eof()) {
            words[wordCount] = themeFile.readLine().toUpperCase();
            wordCount++;
        }
        themeFile.close();

        String secretWord = words[(int) (Math.random() * wordCount)];
        playHangman(con, secretWord, leaderboardFileName);
    }

    private static void playHangman(Console con, String secretWord, String leaderboardFileName) {
        int attemptsLeft = 6;
        char[] revealedWord = new char[secretWord.length()];
        for (int i = 0; i < revealedWord.length; i++) {
            revealedWord[i] = '_';
        }

        int points = 0;
        boolean cheatUsed = false;

        con.println("Let's play Hangman!");

        while (attemptsLeft > 0) {
            con.clear();
            con.println("Word: " + new String(revealedWord));
            con.println("Attempts left: " + attemptsLeft);
            con.print("Guess a letter or the whole word: ");

            String guess = con.readLine().toUpperCase();

            if (guess.equals("STATITAN")) {
                cheatUsed = true;
                attemptsLeft++;
                con.println("Cheat activated! You get an extra guess.");
                con.sleep(2000);
                continue;
            }

            if (guess.equals(secretWord)) {
                points = attemptsLeft * 100;
                con.clear();
                con.println("Congratulations! You guessed the word: " + secretWord);
                con.println("You earned " + points + " points!");
                updateLeaderboard(con, leaderboardFileName, points);
                return;
            } else if (guess.length() == 1) {
                boolean found = false;
                for (int i = 0; i < secretWord.length(); i++) {
                    if (secretWord.charAt(i) == guess.charAt(0)) {
                        revealedWord[i] = guess.charAt(0);
                        found = true;
                    }
                }

                if (!found) {
                    attemptsLeft--;
                    revealRandomLetter(secretWord, revealedWord);
                    con.println("Wrong guess! A letter has been revealed.");
                } else {
                    con.println("Good guess!");
                }

                if (new String(revealedWord).equals(secretWord)) {
                    points = attemptsLeft * 100;
                    con.clear();
                    con.println("Congratulations! You guessed the word: " + secretWord);
                    con.println("You earned " + points + " points!");
                    updateLeaderboard(con, leaderboardFileName, points);
                    return;
                }
            } else {
                attemptsLeft--;
                revealRandomLetter(secretWord, revealedWord);
                con.println("Wrong guess! A letter has been revealed.");
            }

            drawHangman(con, 6 - attemptsLeft);
        }

        con.clear();
        con.println("Game over! The word was: " + secretWord);
    }

    private static void revealRandomLetter(String secretWord, char[] revealedWord) {
        int index;
        do {
            index = (int) (Math.random() * secretWord.length());
        } while (revealedWord[index] != '_');
        revealedWord[index] = secretWord.charAt(index);
    }

    private static void drawHangman(Console con, int stage) {
        String[] hangmanStages = {
            """
              -----
              |   |
                  |
                  |
                  |
                  |
            """,
            """
              -----
              |   |
              O   |
                  |
                  |
                  |
            """,
            """
              -----
              |   |
              O   |
              |   |
                  |
                  |
            """,
            """
              -----
              |   |
              O   |
             /|   |
                  |
                  |
            """,
            """
              -----
              |   |
              O   |
             /|\\  |
                  |
                  |
            """,
            """
              -----
              |   |
              O   |
             /|\\  |
             /    |
                  |
            """,
            """
              -----
              |   |
              O   |
             /|\\  |
             / \\  |
                  |
            """
        };
        con.println(hangmanStages[stage]);
    }

    private static void showHelp(Console con) {
        con.clear();
        con.println("HOW TO PLAY HANGMAN:");
        con.println("1. Choose a theme.");
        con.println("2. Try to guess the secret word one letter at a time.");
        con.println("3. You can also try to guess the whole word.");
        con.println("4. You have 6 incorrect guesses before the game ends.");
        con.println("5. Each wrong guess reveals a letter from the secret word.");
        con.println("6. You earn points based on the number of guesses left.");
        con.println("Press Enter to return to the main menu.");
        con.readLine();
    }

    private static void showLeaderboard(Console con, String leaderboardFileName) {
        con.clear();
        con.println("LEADERBOARD:");
        List<String[]> leaderboard = new ArrayList<>();

        try {
            TextInputFile file = new TextInputFile(leaderboardFileName);
            while (!file.eof()) {
                String[] entry = file.readLine().split(",");
                leaderboard.add(entry);
            }
            file.close();

            leaderboard.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));

            con.println("Names                  |   Points");
            for (int i = 0; i < leaderboard.size(); i++) {
                String[] entry = leaderboard.get(i);
                con.println((i + 1) + ")  " + padRight(entry[0], 20) + "|    " + entry[1]);
            }
        } catch (Exception e) {
            con.println("No leaderboard data available.");
        }
        con.println("Press Enter to return to the main menu.");
        con.readLine();
    }

    private static void updateLeaderboard(Console con, String leaderboardFileName, int points) {
        con.print("Enter your name for the leaderboard: ");
        String name = con.readLine();

        try {
            TextOutputFile file = new TextOutputFile(leaderboardFileName, true);
            file.println(name + "," + points);
            file.close();
        } catch (Exception e) {
            con.println("Error updating leaderboard.");
        }

        con.println("Your name has been added to the leaderboard!");
        con.println("Press Enter to return to the main menu.");
        con.readLine();
    }

    private static void secretOption(Console con) {
        con.print("Enter the secret password: ");
        String password = con.readLine();

        if ("statitan".equalsIgnoreCase(password)) {
            con.clear();
            con.println("Why do programmers prefer dark mode?");
            con.println("Because light attracts bugs!");
        } else {
            con.clear();
            con.println("Wrong password. Try again next time!");
        }
        con.println("Press Enter to return to the main menu.");
        con.readLine();
    }

    private static String padRight(String text, int length) {
        return String.format("%-" + length + "s", text);
    }
}

