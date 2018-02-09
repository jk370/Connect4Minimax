/**
 * Represents an entity of the game board.
 * 
 * @author Jordan Koulouris
 * @version 1.0
 * @release 12/01/2018
 * @see MyConnectN.java
 *
 */
public class Board {
	protected char[][] board;
	protected int gameWinAmount;
	public static final int BOARD_SPACE = 0;
	public static final int BOARD_HEIGHT = 6;
	public static final int BOARD_WIDTH = 7;
	
	/**
	 * Zero argument constructor - creates Board with N set to default of 4.
	 */
	public Board() {
		new Board(4);
	}

	/**
	 * Initialises the char board to appropriate size and sets game win amount.
	 */
	public Board(int N) {
		//Set N to default if out of range
		if (!(N >= 2 && N <= 7)) {
				N = 4;
			}
		this.board = new char[BOARD_HEIGHT][BOARD_WIDTH];
		this.gameWinAmount = N;
	}

	/**
	 * Accessor for the current board state.
	 * @return board
	 * 		Current state of the board
	 */
	public char[][] getBoard() {
		return board;
	}
	
	/**
	 * Sets the current state of the board
	 * @param board
	 * 		the board to set
	 */
	public void setBoard(char[][] board) {
		this.board = board;
	}
	
	/**
	 * Accessor to return 'N' - the current game win amount.
	 * @return gameWinAmount
	 */
	public int getGameWinAmount() {
		return gameWinAmount;
	}
	
	/**
	 * Prints out the current board state to the console.
	 */
	void printBoard() {
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			for (int column = 0; column < BOARD_WIDTH; column++) {
				char symbol = ' ';
				for(char counter : Counter.playerSymbols) {
					if(board[row][column] == counter) {
						symbol = counter;
					}
				}
				System.out.print("| " + symbol + " ");
			}
			System.out.println("|");
		}
		System.out.println("  1   2   3   4   5   6   7");
	}

	/**
	 * Places player symbol on board at position provided.
	 * @param player
	 * 		The player symbol making the turn.
	 * @param position
	 * 		The position on the board to place the counter.
	 * @return placed
	 * 		True if the counter has been placed, false if column is full.
	 */
	boolean placeCounter(char player, int position) {
		boolean placed = false;
		//Searches from bottom to find first empty position '0'.
		for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
			if (board[row][position - 1] == BOARD_SPACE) {
				board[row][position - 1] = player;
				placed = true;
				break;
			}
		}
		return placed;
	}

	/**
	 * Searches for winning line on board in any direction.
	 * @return hasWon
	 * 		returns true if line is >= game win amount in any direction.
	 */
	boolean checkWin(char player) {
		boolean hasWon = (checkHor(player) || checkVer(player) || checkDiag(player));
		return hasWon;
	}

	/**
	 * Searches for win in horizontal direction.
	 * @param player
	 * 		the player symbol that just made the turn.
	 * @return horWin
	 * 		returns true if there is a horizontal winning line on the board.
	 */
	private boolean checkHor(char player) {
		boolean horWin = false;
		int count = 0;
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			for (int column = 0; column < BOARD_WIDTH; column++) {
				if (board[row][column] == player) {
					count++;
					if (count >= gameWinAmount) {
						horWin = true;
					}
				} else {
					count = 0;
				}
			}
			count = 0;
		}
		return horWin;
	}

	/**
	 * Searches for win in vertical direction.
	 * @param player
	 * 		the player symbol that just made the turn.
	 * @return verWin
	 * 		returns true if there is a vertical winning line on the board.
	 */
	private boolean checkVer(char player) {
		boolean verWin = false;
		int count = 0;
		for (int column = 0; column < BOARD_WIDTH; column++) {
			for (int row = 0; row < BOARD_HEIGHT; row++) {
				if (board[row][column] == player) {
					count++;
					if (count >= gameWinAmount) {
						verWin = true;
					}
				} else {
					count = 0;
				}
			}
			count = 0;
		}
		return verWin;
	}

	/**
	 * Searches for a win in the diagonal direction.
	 * @param player
	 * 		the player symbol that just made the turn.
	 * @return diagWin
	 * 		returns true if there is a diagonal winning line on the board.
	 */
	private boolean checkDiag(char player) {
		//Search both directions of the diagonal separately.
		boolean diagWin = (checkAscDiag(player) || checkDesDiag(player));
		return diagWin;
	}

	/**
	 * Searches for diagonal win in the ascending direction '/'.
	 * @param player
	 * 		the player symbol that just made the turn.
	 * @return diagWin
	 * 		returns true if there is a diagonal winning line on the board.
	 */
	private boolean checkAscDiag(char player) {
		boolean diagWin = false;
		int count = 0;
		for (int columns = 0; columns <= BOARD_WIDTH-gameWinAmount; columns++) {
			for (int rows = BOARD_HEIGHT-1; rows >= gameWinAmount-1; rows--) {
				//First finds all symbols in location that can fit ascending diagonal
				if(board[rows][columns] == player) {
					int streak = 1;
					count++;
					
					//Searches ascending diagonal for symbol for gameWinAmount distance
					while (streak < gameWinAmount) {
						if(board[rows-streak][columns+streak] == player) {
							count++;
							streak++;
							if (count >= gameWinAmount) {
								diagWin = true;
							}
						} else {
							//Breaks if line not found
							count = 0;
							break;
						}
					}
				} else {
					count =0;
				}
			}
		}
		return diagWin;
	}

	/**
	 * Searches for diagonal win in the descending direction '\'.
	 * @param player
	 * 		the player symbol that just made the turn.
	 * @return diagWin
	 * 		returns true if there is a diagonal winning line on the board.
	 */
	private boolean checkDesDiag(char player) {
		boolean diagWin = false;
		int count = 0;
		for (int columns = BOARD_WIDTH-1; columns >= gameWinAmount-1; columns--) {
			for (int rows = BOARD_HEIGHT-1; rows >= gameWinAmount-1; rows--) {
				//First finds all symbols in location that can fit descending diagonal
				if(board[rows][columns] == player) {
					int streak = 1;
					count++;
					
					//Searches descending diagonal for symbol for gameWinAmount distance
					while (streak < gameWinAmount) {
						if(board[rows-streak][columns-streak] == player) {
							count++;
							streak++;
							if (count >= gameWinAmount) {
								diagWin = true;
							}
						} else {
							//Breaks if line not found
							count = 0;
							break;
						}
					}
				} else {
					count = 0;
				}
			}
		}
		return diagWin;
	}
	
	/**
	 * Checks if no more turns remain (with no winner).
	 * @return drawn
	 * 		Returns true if game has drawn.
	 */
	boolean checkDraw() {
		boolean drawn = true;
		//Search top line only for spaces
		for(int column = 0; column < BOARD_WIDTH; column++) {
			if(board[0][column] == BOARD_SPACE) {
				drawn = false;
			}
		}
		return drawn;
	}
}