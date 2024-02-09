import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Reasoning {
	static int timesShapeGuessed = 0;
	static int timesColourGuessed = 0;
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		final int LENGHT_BOARD = 9;
		final int MIN_PLAYER_RANGE = 2;
		final int MAX_PLAYER_RANGE = 6;
		int maxPlayers = 0;
		intro();
		do {
			System.out.print("Number of players (Min: 2, MAX: 6) :  ");
			if(scan.hasNextInt()) {
				maxPlayers = scan.nextInt();
			}else {
				System.out.println("Invalid Input. Try again.");
			}
		}while (maxPlayers < MIN_PLAYER_RANGE || maxPlayers > MAX_PLAYER_RANGE);
		
		Shape[] boardGame = new Shape[LENGHT_BOARD ];
		Player[] playerBoard = new Player[maxPlayers];
		Player[] playerScore = new Player[maxPlayers];
		
		for(int i = 0; i < boardGame.length; i++) {
			boardGame[i] = new Shape();
		}
		for(int i = 0; i < playerBoard.length; i++) {
			playerBoard[i] = new Player();
			playerScore[i] = new Player();
		}
		
		loadData(boardGame);
		//randomise 0 to 8 numbers to attribute a pattern to each player
		initialisePlayer(playerBoard, boardGame);
		boolean continueGame = true;
		int count = 0;
		int startPlayer = getRandom(maxPlayers) + 1;	
		//cheat(playerBoard);
		do {
			System.out.println("____Players in game____\n");
			int nPlayers = playersInGame(playerScore);
			System.out.println("_______________________");
			if (nPlayers == 1) {
				System.out.println("\nCongratulations!\nYou won the game!");
				break;
			}
			System.out.print("\nPlayer " + (startPlayer++) + " will start guessing! \n");
			int playerTurn = startPlayer-2;
			System.out.print("Which player are you going to guess? ");
			if(scan.hasNextInt()) {
				int playerToBeGuessed = scan.nextInt();
				startPlayer = playerToBeGuessed;
				playerToBeGuessed--;
				
				System.out.print("\nShape: ");
				String guessShape = scan.next();
				
				System.out.print("Colour: ");
				String guessColour = scan.next();
				
				int checkMatchResponse = checkMatch(playerScore,playerBoard, guessShape, guessColour, playerToBeGuessed, playerTurn);
				nPlayers = playersInGameV2(playerScore);
				
				if((checkMatchResponse == 2) && (nPlayers > 1)){
					System.out.print("Player " + (playerToBeGuessed + 1) + " was eliminated!\nPlease, choose another player to start guessing: ");
					if(scan.hasNextInt()) {
						startPlayer = scan.nextInt();
						System.out.println();
					}else {
						System.out.println("Invalid input. Insert an Integer.");
					}
				}
			}else {
				System.out.println("Invalid input. Insert an Integer.");
				continueGame = false;
			}
		}while(continueGame);
		
	}
	public static void intro() {
		System.out.println("_________________________________________________");
		System.out.println("         Welcome to the Reasoning 3% Game!     ");
		System.out.println("                                               ");
		System.out.println("      Number of Players: You choose (MAX 6)    ");
		System.out.println("        Shapes: Triangle, Square, Circle       ");
		System.out.println("           Colours: Red, Blue, Green           ");
		System.out.println("                                               ");
		System.out.println("   If pattern is guess, players is eliminated  ");
		System.out.println(" If one of the pattern is guessed, lose 1 life ");
		System.out.println("                                               ");
		System.out.println("                Let the best win!              ");
		System.out.println("_________________________________________________\n\n");
		
		
	}
	public static int verifyLife(Player[] playerDetails, int indexPlayer) {
		return playerDetails[indexPlayer].getLife();
	}
	
	public static int checkMatch(Player[] playerInfo,Player[] playerDetails, String shape, String colour, int playerIndex, int playerWhoGuessed) {
		int response;
		final int MATCH_BOTH = 2;
		final int MATCH_ONCE = 1;
		final int NOT_A_MATCH = 0;
		final int ZERO_LIFE = 0;
		
		if((playerDetails[playerIndex].getShape().equals(shape)) && (playerDetails[playerIndex].getColour().equals(colour))) {
			playerInfo[playerIndex].setLife(0);
			response = MATCH_BOTH;
		}
		else if(playerDetails[playerIndex].getShape().equals(shape)){
			playerInfo[playerIndex].setShape(shape);
			timesShapeGuessed++;
			if(timesShapeGuessed > 1) {
				playerInfo[playerWhoGuessed].setLife();
			}
			response = MATCH_ONCE;
		}
		else if(playerDetails[playerIndex].getColour().equals(colour)){
			response = MATCH_ONCE;
			playerInfo[playerIndex].setColour(colour);
			timesColourGuessed++;
			if(timesColourGuessed > 1) {
				playerInfo[playerWhoGuessed].setLife();
			}
		}
		else { response = NOT_A_MATCH;
			if(playerInfo[playerWhoGuessed].getLife() != ZERO_LIFE) {
				playerInfo[playerWhoGuessed].setLife();
			}
		}
			return response;
		
	}
	
	public static void loadData(Shape[] boardGame)  {
		try {
            Scanner loadData = new Scanner(new BufferedReader(new FileReader("data/shapes.txt")));
            String dataLine;
            int fileLineCounter = 0;
            final int SHAPE_INDEX = 0;
            final int COLOUR_INDEX = 1;
            while (loadData.hasNext()) {
                dataLine = loadData.nextLine();
                String[] fileDetails = dataLine.split(" ");
                boardGame[fileLineCounter].setShape(fileDetails[SHAPE_INDEX]);
                boardGame[fileLineCounter].setColour(fileDetails[COLOUR_INDEX]);
                fileLineCounter++;
            }
        } catch (IOException e) {
            System.out.print("An error has occurred! Please, try again!\n" + e + "\n");
        }
	}
	public static Boolean verifyIfExists(Player[] playerBoard, String shape, String colour) {
		for (Player p : playerBoard){
			if((p.getShape().equals(shape)) && (p.getColour().equals(colour))) {
				return true;
			}
		}
		return false;
	}
	
	public static int getRandom(int maxValue) {
		Random random = new Random();
		int randomNumber = random.nextInt(maxValue);
		return randomNumber;
	}
	
	public static void initialisePlayer(Player[] playerBoard, Shape[] boardGame) {
		int playerCounter = 0;
		final String EMPTY = "E";
		final int MAX_SHAPES = 9;
		
		while (playerCounter < playerBoard.length) {
			int randomShape = getRandom(MAX_SHAPES);
			if(!(verifyIfExists(playerBoard, boardGame[randomShape].getShape(), boardGame[randomShape].getColour()))) {
				if((playerBoard[playerCounter].getShape().equals(EMPTY))) {
					playerBoard[playerCounter].setShape(boardGame[randomShape].getShape());
					playerBoard[playerCounter].setColour(boardGame[randomShape].getColour());
					playerCounter++;
				}
			}
		}
	}
	
	public static int playersInGame(Player[] playerScore) {
		int playerCount = 1;
		int countIfWinner = 0;
		for(Player p : playerScore) {
			if(p.getLife() != 0){
				System.out.println("Player " + (playerCount) + " -> Life:" + p.getLife());
				if(!(p.getShape().equals("E"))) {
					System.out.println("  -> Shape: " + p.getShape());
				}else if (!(p.getColour().equals("E"))) {
					System.out.println("  -> Colour: " + p.getColour());
				}
				countIfWinner++;
			}
			playerCount++;
		}
		return countIfWinner;
	}
	
	public static int playersInGameV2(Player[] playerScore) {
		int playerCount = 0;
		for(Player p : playerScore) {
			if(p.getLife() != 0){
				playerCount++;
			}
		}
		return playerCount;
	}
	
	public static void cheat(Player[] playerBoard) {
		int playerCount = 1;
		for(Player p : playerBoard) {
			System.out.println("Player " + playerCount);
			System.out.println("   -> Shape: " + p.getShape());
			System.out.println("   -> Colour: " + p.getColour());
			System.out.println("   -> Life:" + p.getLife() + "\n");
			playerCount++;
		}
	}
	
}
