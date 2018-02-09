/**
 * Game mechanics and main function for Connect N game.
 * 
 * @author Jordan Koulouris
 * @version 1.0
 * @release 12/01/2018
 */
public class MyConnectN {

	/**
	 * Main function to receive and validate input from command line and launch game
	 * @param args
	 * 		input received from command line
	 */
	public static void main(String[] args) {
		int N = 4;
		try {
			N = Integer.parseInt(args[0]);
			//Set N to default if out of range
			if (!(N >= 2 && N <= 7)) {
				N = 4;
			}
		} catch (Exception e) {
			// Do nothing if exception is thrown - N remains at default of 4
		}
		new MyConnectN(N);
	}

	private Board board;
	private Player[] players;
	
	/**
	 * Zero argument constructor - launches game with N set to default of 4.
	 */
	public MyConnectN() {
		new MyConnectN(4);
	}
	
	/**
	 * Constructor initialising board and players, then starting game.
	 */
	public MyConnectN(int N) {
		board = new Board(N);
		addPlayers(3);
		startMessage(N);
		playGame();
	}

	/**
	 * Adds set number of players to 'players' array to allow game play.
	 * @param playerNumber
	 * 		the number of players to add
	 */
	private void addPlayers(int playerNumber) {
		players = new Player[playerNumber];
		for (int i = 0; i < playerNumber; i++) {
			if (i == 0) {
				//One human player - always first
				players[i] = new Human();
			} else {
				players[i] = new Bot();
			}
		}
	}
	
	/**
	 * Runs game in loop until win conditions met.
	 */
	private void playGame() {
		board.printBoard();
		String end = "";
		boolean win = false;
		
		while (!win) {
			for (Player player : players) {
				char symbol = player.counter.getSymbol();
				
				//Take move and place counter
				int move = player.getNextMove(board);
				board.placeCounter(symbol, move);
								
				//Check for end game conditions - win or draw
				if (board.checkWin(symbol)) {
					win = true;
					
					//If symbol is the human player
					if (symbol == players[0].counter.getSymbol()) {
						end = "Won";
					} else {
						end = "Lost";
					}
					break;
					
				} else if (board.checkDraw()) {
					win = true;
					end = "Drawn";
					break;
				}
			}
			//Print board once per round (due to only one human turn per round)
			board.printBoard();
		}
		System.out.println("Game Over! You Have " + end + "!!!");
		System.exit(0);
	}

	/**
	 * Prints start game messages and instructions to console.
	 */
	public void startMessage(int N) {
		System.out.println("Welcome to Connect " + N);
		System.out.println("There are 3 players red, yellow and blue");
		System.out.println("Player 1 is Red, Player 2 is Yellow and Player 3 is Blue");
		System.out.println("To play the game type in the number of the column you want to drop you counter in");
		System.out.println("A player wins by connecting " + N +" counters in a row - vertically, horizontally or diagonally");
		System.out.println("");
	}
}