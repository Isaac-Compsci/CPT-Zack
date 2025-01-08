import arc.*;

public class GitHub{
    public static void main(String[] args){
        Console con = new Console();
       
String themesFileName = "themes.txt";
        TextInputFile themesFile = new TextInputFile(themesFileName);

        // Read themes from file
        String[] themes = new String[10];
        int themeCount = 0;
        while (themesFile.eof() == false){
            themes[themeCount] = themesFile.readLine();
            themeCount++;
        }
        themesFile.close();
       
        // Main menu
        boolean play = true;
        while (play){
            con.println("Welcome to Hangman!");
            con.println("1. Play Game");
            con.println("2. Quit");
            con.print("Enter your choice: ");
            int choice = con.readInt();

            if (choice == 1){
playGame(con, themes, themeCount);
            } else if (choice == 2){
                con.println("Thanks for playing!");
                break;
            } else {
                con.println("Invalid choice. Try again.");
           
            }
            play = false;
        }
     }
    }
