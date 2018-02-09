/**
 * Logic to perform min-max algorithm and find best next turn for robot player.
 * 
 * @author Jordan Koulouris
 * @version 1.0
 * @release 12/01/2018
 * @see Bot.java
 *
 */
public class BotSearch {
	private MinimaxBoard[] children;
	private MinimaxBoard board;
	private int bestColumn;
	private char callingPlayer;
	
	/**
	 * Constructor sets calling player.
	 * @param callingPlayer
	 * 		the counter of the player who created the object.
	 */
	public BotSearch(char callingPlayer) {
		this.callingPlayer = callingPlayer;
	}
	
	/**
	 * Returns best column for player after performing min-max search.
	 * @param startingBoard
	 * 		Current state of the game board
	 * @param depth
	 * 		Depth of turns to search
	 * @return bestColumn
	 * 		the column leading to the highest scoring board
	 */
	int getBestColumn(Board startingBoard, int depth) {
		board = new MinimaxBoard(startingBoard);
		
		try {
			// Perform min-max search always starting with calling player
			minimax(board, depth, callingPlayer);
			
		} catch (CloneNotSupportedException e) {
			//End programme gracefully if exception is thrown - error in code
			e.printStackTrace();
			System.exit(0);
		}
		return bestColumn;
	}

	/**
	 * Performs min-max algorithm to find best possible move.
	 * 
	 * @param board
	 * 		Current state of the game board for searching
	 * @param depth
	 * 		Remaining depth of turns to search
	 * @param player
	 * 		player who's turn it is on the current method call
	 * 
	 * @return bestValue
	 * 		the value with the least possible losses
	 * 
	 * @throws CloneNotSupportedException
	 * 		Exception thrown if clone attempted on class that is not supported
	 * 
	 */
	private int minimax(MinimaxBoard board, int depth, char player) throws CloneNotSupportedException {
		this.board = board;
		char nextPlayer = nextPlayer(player);
		int bestValue; //Highest score for this turn
		int column = 0; //Column tracker
		int bestTurn = -1; //Highest scoring column for this turn
		
		// Check if leaf node has been reached
		if (depth == 0 || board.endGame() || !board.isUsable()) {
			bestValue = evaluateBoard();
			
		} else if (player == callingPlayer) {
			// Check if player is the original caller - maximising player
			bestValue = Integer.MIN_VALUE;
			createChildren(player);

			// Recursively call function for each child (board state)
			for (MinimaxBoard newBoard : children) {
				column++;
				int child = minimax(newBoard, depth-1, nextPlayer);
				child += weightColumn(column);
				bestValue = Math.max(bestValue, child);
				
				// Save column if value is chosen
				if (bestValue == child) bestTurn = column;
			}
			
		} else {
			// Check if player is not the original caller - minimising player
			// Minus max weighting to prevent overflow
			bestValue = Integer.MAX_VALUE - weightColumn((board.getBoard()[0].length+1)/2);
			createChildren(player);
			
			// Recursively call function for each child (board state)
			for (MinimaxBoard newBoard : children) {
				column++;
				int child = minimax(newBoard, depth-1, nextPlayer);
				child += weightColumn(column);
				bestValue = Math.min(bestValue, child);
				
				// Save column if value is chosen
				if (bestValue == child) bestTurn = column;
			}
		}
		bestColumn = bestTurn;
		return bestValue;
	}
	
	/**
	 * Creates clones of current board and places player counter in each column.
	 * @param currentPlayer
	 * 		the counter for the current player to place in the columns
	 * @throws CloneNotSupportedException
	 * 		Exception if clone is attempted on class where clone is not supported.
	 */
	private void createChildren(char currentPlayer) throws CloneNotSupportedException {
		int rows = Board.BOARD_HEIGHT;
		int columns = Board.BOARD_WIDTH;
		char[][] newBoard = new char[rows][columns];
		children = new MinimaxBoard[columns];
		
		//Loop through and perform actions for each column
		for (int i = 0; i < columns; i++) {
			
			//Create clone of board variable (2D char array object) in Board class
			for (int j = 0; j < rows; j++) {
				//Clone one row array at a time.
				newBoard[j] = board.getBoard()[j].clone();
			}
			
			//Create clone of Board and set board array
			children[i] = (MinimaxBoard) board.clone();
			children[i].setBoard(newBoard.clone());
			
			//Place counter at new location (receives columns 1-7 not 0-6, hence i+1)
			if (!children[i].placeCounter(currentPlayer, i+1)) {
				//Do not use board if counter cannot be placed - false returned
				children[i].setUsable(false);
			}
		}
	}
	
	/**
	 * Evaluates state of board at leaf nodes.
	 * @return score
	 * 		Heuristic score of the board related to the calling player
	 */
	private int evaluateBoard() {
		int score = 0;
		boolean end = false;
		
		//Minimum score for leaf nodes that did not place a counter
		if (!board.isUsable()) {
			score = Integer.MIN_VALUE;
			end = true;
			
		} else if (board.checkDraw()) { 
			//Keep score at 0 for draw - do not evaluate
			end = true;
			
		} else {
			// Check if any player has won the game
			for (char counter : Counter.playerSymbols) {
				if (board.checkWin(counter) && counter == callingPlayer) {
					//Max score if calling player has won the game (minus max weighting to prevent overflow)
					score = Integer.MAX_VALUE - weightColumn((board.getBoard()[0].length+1)/2);
					end = true;
					
				} else if (board.checkWin(counter) && counter != callingPlayer) {
					//Minimum score if any other player has won
					score = Integer.MIN_VALUE;
					end = true;
				}
			}
		}

		// Else find the best streak for the calling player
		if (end == false) {
			score = board.findStreak(callingPlayer);
		}
		return score;
	}

	/**
	 * Finds the next player counter to take a turn.
	 * @param currentPlayer
	 * 		the player who has taken the current turn
	 * @return nextPlayer
	 * 		the counter for the next player
	 */
	private char nextPlayer(char currentPlayer) {
		char nextPlayer = ' ';
		
		//Loop through player counters to find next player
		for (int i = 0; i < Counter.playerSymbols.length; i++) {
			if (Counter.playerSymbols[i] == currentPlayer && i != Counter.playerSymbols.length - 1) {
				nextPlayer = Counter.playerSymbols[i + 1];
				
			} else if (Counter.playerSymbols[i] == currentPlayer && i == Counter.playerSymbols.length - 1) {
				//Move back to start of array when end is reached
				nextPlayer = Counter.playerSymbols[0];	
			}
		}
		return nextPlayer;
	}

	/**
	 * Provides column weighting to prioritise centre columns when board scores are equal.
	 * @param currentColumn
	 * 		The chosen column to weight
	 * @return weighting
	 * 		The weighting score of the column
	 */
	private int weightColumn(int currentColumn) {
		int weighting = 0;
		int columnNumber = (Board.BOARD_WIDTH)+1; //Plus one as columns are input as 1-7, not 0-6.
		
		if (currentColumn == columnNumber/2) {
			//Middle column
			weighting = 3;
	
		} else if (currentColumn >= (columnNumber/2)-1 && currentColumn <= (columnNumber/2)+1) {
			//One away from middle
			weighting = 2;
			
		} else if (currentColumn >= (columnNumber/2)-2 && currentColumn < (columnNumber/2)+2) {
			//Two away from middle
			weighting = 1;
		}
		return weighting;
	}
}