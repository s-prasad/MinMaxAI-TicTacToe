package tictactoe;
import java.util.*;

public class Main {
    Scanner scanner = new Scanner(System.in);
    char[][] board = new char[3][3];
    String line;
    String gamestate;

    public static void main(String[] args) {
        Main program = new Main();
        program.start();
    }

/*    private void userInput() {
        int counter = 0;
        System.out.print("Enter cells: ");
        line = scanner.nextLine();
        line = line.replace("\"", "");

        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board.length; j++) {
                board[i][j] = line.charAt(counter);

                if (line.charAt(counter) == 'X') {
                    numX++;
                } else if (line.charAt(counter) == 'O') {
                    numO++;
                }

                counter++;
            }
        }
    }*/

    private void gameInitalize() {
        for (int i = 0; i< board.length; i++) {
            for (int j = 0; j< board.length; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private void nextInput(char XorO) {
        int x;
        int y;

        while (true) {

                System.out.println("Enter the coordinates: ");

            try {
                x = scanner.nextInt();
                x = x-1;
                y = scanner.nextInt();
                y = board.length-y;
                //System.out.println();

                if (x<0 || x>2 || y<0 || y>2) {
                    System.out.println("Coordinates should be from 1 to 3!");
                } else if (board[y][x] != ' ') {
                        System.out.println("This cell is occupied! Choose another one!");
                } else {
                    break;
                }

            } catch (InputMismatchException ex) {
                System.out.println("You should enter numbers!");
                scanner.next();
            }

        }

        board[y][x] = XorO;

    }

    private void easyAI(char XorO) {
        System.out.println("Making move level \"easy\"");
        randomMove(XorO);
    }

    private void midAI(char XorO) {
        char otherChar;
        char AIChar;

        if (XorO == 'X') {
            AIChar = 'X';
            otherChar = 'O';
        } else {
            AIChar = 'O';
            otherChar = 'X';
        }

        System.out.println("Making move level \"medium\"");

        //AI win move
        int[] winLocation = checkIfTwoInSet(AIChar);
        if (winLocation[0] > -1 && winLocation[1] > -1) {
            board[winLocation[0]][winLocation[1]] = AIChar;
            return;
        }

        //block opponent if needed.
        int[] blockLocation = checkIfTwoInSet(otherChar);
        if (blockLocation[0] > -1 && blockLocation[1] > -1) {
            board[blockLocation[0]][blockLocation[1]] = AIChar;
            return;
        }

        //random move
        randomMove(AIChar);
    }

    private void hardAI(char XorO) {
        int[] bestMove = new int[2];
        int bestScore = -1000;
        int currentScore = 0;

        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board.length; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = XorO;
                    currentScore = minimax(false, XorO, board);
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }

                    //clears the board of previous move.
                    board[i][j] = ' ';
                }
            }
        }

        System.out.println("Making move level \"hard\"");
        board[bestMove[0]][bestMove[1]] = XorO;

    }

    private Integer minimax(boolean isMax, char player, char[][] board) {
        String gamestate = checkGameState(board);
        char opponent = (player == 'X') ? 'O':'X';

        //Sets 'worst possible score' of -1000 if maximizing or 1000 if minimizing.
        int best = (isMax ? -1000 : 1000);

        //checks if game is finished or not. If finished, checks if game was a draw (returns 0 points),
        //or if if player won (returns 10 points) or if opponent won (returns -10 points).
        if (!gamestate.equals("Game not finished")) {
            if (gamestate.equals("Draw")) {
                return 0;
            } else if ((player == 'X' && gamestate.equals("X wins")) || (player == 'O' && gamestate.equals("O wins"))) {
                return 10;
            } else {
                return -10;
            }

        }


        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board.length; j++) {
                if (board[i][j] == ' ') {
                    if (isMax) {
                        board[i][j] = player;
                        best = Math.max(best, minimax(!isMax, player, board));
                    } else {
                        board[i][j] = opponent;
                        best = Math.min(best, minimax(!isMax, player, board));
                    }

                    //clears the board of previous move.
                    board[i][j] = ' ';
                }
            }
        }

        return best;
    }



    private void randomMove(char XorO) {
        Random random = new Random();
        while (true) {
            int x = random.nextInt(3);
            int y = random.nextInt(3);

            if (board[y][x] == ' ') {
                board[y][x] = XorO;
                break;
            }
        }
    }

    //Checks if any row, column, or diagonal have two of the same characters (X or O) with one empty space.
    //Returns position of empty space if found. Otherwise, returns [-1, -1].
    private int[] checkIfTwoInSet (char toCheck) {
        int emptyLocation;
        int numOftoCheck;

        //check horizontal
        for (int i = 0; i< board.length; i++) {
            emptyLocation = -1;
            numOftoCheck = 0;

            for (int j = 0; j< board.length; j++) {
                if (board[i][j] == ' ') {
                    emptyLocation = j;
                } else if (board[i][j] == toCheck) {
                    numOftoCheck++;
                }
            }

            if(numOftoCheck == 2 && emptyLocation > -1) {
                return (new int[]{i, emptyLocation});
            }
        }

        //check vertical
        for (int i = 0; i< board.length; i++) {
            emptyLocation = -1;
            numOftoCheck = 0;

            for (int j = 0; j< board.length; j++) {
                if (board[j][i] == ' ') {
                    emptyLocation = j;
                } else if (board[j][i] == toCheck) {
                    numOftoCheck++;
                }
            }

            if(numOftoCheck == 2 && emptyLocation > -1) {
                return (new int[]{emptyLocation, i});
            }
        }

        //check diagonal left to right
        emptyLocation = -1;
        numOftoCheck = 0;
        for (int i = 0; i< board.length; i++) {
            if (board[i][i] == ' ') {
                emptyLocation = i;
            } else if (board[i][i] == toCheck) {
                numOftoCheck++;
            }

            if(numOftoCheck == 2 && emptyLocation > -1) {
                return (new int[]{emptyLocation, emptyLocation});
            }
        }

        //check diagonal right to left
        emptyLocation = -1;
        numOftoCheck = 0;
        for (int i = 0; i< board.length; i++) {

            if (board[i][board.length-i-1] == ' ') {
                emptyLocation = i;
            } else if (board[i][board.length-i-1] == toCheck) {
                numOftoCheck++;
            }

            if(numOftoCheck == 2 && emptyLocation > -1) {
                return (new int[]{emptyLocation, board.length-emptyLocation-1});
            }
        }

        return new int[]{-1, -1};
    }


    private String checkGameState(char[][] board) {
        boolean Xwins = false;
        boolean Owins = false;
        int numX=0;
        int numO=0;

        for (int i=0; i<board.length; i++) {
            for (int j=0; j<board.length; j++) {
                if (board[i][j]=='X') {
                    numX++;
                } else if (board[i][j]=='O') {
                    numO++;
                }
            }
        }

/*        if (Math.abs(numX - numO) >= 2) {
            gamestate = "Impossible";
            return true;
        }*/

        //check horizontal
        for (int i = 0; i< board.length; i++) {
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                if (board[i][0] == 'X') {
                    Xwins = true;
                }

                if (board[i][0] == 'O') {
                    Owins = true;
                }

            }
        }

        //check vertical
        for (int i = 0; i< board.length; i++) {
            if (board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                if (board[0][i] == 'X') {
                    Xwins = true;
                }

                if (board[0][i] == 'O') {
                    Owins = true;
                }

            }
        }

        //check diagonal left to right
        if (board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            if (board[0][0] == 'X') {
                Xwins = true;
            }

            if (board[0][0] == 'O') {
                Owins = true;
            }

        }

        //check diagonal right to left
        if (board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            if (board[0][2] == 'X') {
                Xwins = true;
            }

            if (board[0][2] == 'O') {
                Owins = true;
            }

        }

        if (Xwins) {
            return "X wins";
        } else if (Owins){
            return "O wins";
        } else if (numO + numX < 9) {
            return "Game not finished";
        } else {
                return "Draw";
            }
    }

    private void printResult() {
        System.out.println("---------");

        for (int i = 0; i< board.length; i++) {
            System.out.print("| ");
            for (int j = 0; j< board.length; j++) {
                System.out.print(board[i][j]+" ");
            }
            System.out.println("|");
        }

        System.out.print("---------");
        System.out.println();
    }

    private void start() {
        //menu
        while(true) {
            boolean readyToPlay = true;
            System.out.println("Input command: ");
            String userCommand = scanner.nextLine();

            if (userCommand.toLowerCase().equals("exit")) {
                break;
            }

            String[] commands = userCommand.split(" ");

            if (commands[0].toLowerCase().equals("start")) {
                String p1 = null;
                String p2 = null;

                switch (commands[1]) {
                    case "easy":
                        p1 = "easyAI";
                        break;
                    case "medium":
                        p1 = "mediumAI";
                        break;
                    case "hard":
                        p1 = "hardAI";
                        break;
                    case "user":
                        p1 = "user";
                        break;
                     default:
                         System.out.println("Bad parameters!");
                         readyToPlay = false;
                }

                switch (commands[2]) {
                    case "easy":
                        p2 = "easyAI";
                        break;
                    case "medium":
                        p2 = "mediumAI";
                        break;
                    case "hard":
                        p2 = "hardAI";
                        break;
                    case "user":
                        p2 = "user";
                        break;
                    default:
                        System.out.println("Bad parameters!");
                        readyToPlay = false;
                }

                if (readyToPlay) {
                    gameInitalize();
                    run(p1, p2);
                }

                //scanner.nextLine();

            } else {
                System.out.println("Bad parameters!");
                readyToPlay = false;
            }

        }

    }

    private  void run(String p1, String p2) {
        printResult();
        String gamestate;
        while (checkGameState(board).equals("Game not finished")) {
            if (p1.equals("user")) {
                    nextInput('X');
                } else if (p1.equals("easyAI")){
                    easyAI('X');
            } else if (p1.equals("mediumAI")) {
                midAI('X');
            } else if (p1.equals("hardAI")) {
                hardAI('X');
            }
            printResult();

            gamestate = checkGameState(board);
            if (!gamestate.equals("Game not finished")) {
                System.out.println(gamestate);
                break;
            }

            if (p2.equals("user")) {
                nextInput('O');
            } else if (p2.equals("easyAI")) {
                easyAI('O');
            } else if (p2.equals("mediumAI")) {
                midAI('O');
            } else if (p2.equals("hardAI")) {
                hardAI('O');
            }
            printResult();

            gamestate = checkGameState(board);
            if (!gamestate.equals("Game not finished")) {
                System.out.println(gamestate);
                break;
            }
        }
    }

}