/**
 * Represents an entity of the game board used for Minimax search.
 * 
 * @author Jordan Koulouris
 * @version 1.0
 * @release 12/01/2018
 * @see Board.java
 *
 */
public class MinimaxBoard extends Board implements Cloneable {
	private boolean usable;
	
	/**
	 * Constructor calls superclass and sets usable.
	 */
	public MinimaxBoard () {
		super();
		this.usable = true;
	}
	
	/**
	 * Constructor to create MinimaxBoard from given game Board.
	 * @param board
	 * 		The game board to construct for search.
	 */
	public MinimaxBoard (Board board) {
		super(board.getGameWinAmount());
		this.usable = true;
		copyBoard(board);
	}
	
	/**
	 * Accessor to find if board can be used for evaluation.
	 * @return usable
	 * 		true if the board is usable
	 */
	public boolean isUsable() {
		return usable;
	}

	/**
	 * Sets the board to usable or not for evaluation.
	 * @param usable
	 * 		true if the board is usable
	 */
	public void setUsable(boolean usable) {
		this.usable = usable;
	}
	
	/**
	 * Sets char array 'board' to copy of given board
	 * @param original
	 * 		the original board to copy
	 */
	private void copyBoard(Board original) {
		int rows = Board.BOARD_HEIGHT;
		int columns = Board.BOARD_WIDTH;
		char[][] newBoard = new char[rows][columns];
		
		//Loop through and perform actions for each column
		for (int i = 0; i < columns; i++) {
			
			//Create clone of board variable (2D char array object) in Board class
			for (int j = 0; j < rows; j++) {
				//Clone one row array at a time.
				newBoard[j] = original.getBoard()[j].clone();
			}
			
			//Set board array
			setBoard(newBoard.clone());
		}
	}
	
	/**
	 * Finds the largest score of new streaks on the board.
	 * @param player
	 * 		the player ID to search
	 * @return maxStreak
	 * 		the score assigned to the game board for the player.
	 */
	int findStreak(char player) {
		int maxStreak = 0;

		// Search vertical
		maxStreak += verticalStreak(player);
		
		// Search horizontal
		maxStreak += horizontalStreak(player);
		
		// Search diagonal
		maxStreak += diagonalStreak(player);
		
		return maxStreak;
	}
	
	/**
	 * Searches verticals and assigns score for player streaks.
	 * @param player
	 * 		the player counter to search for
	 * @return vertStreak
	 * 		the vertical streak score for the player
	 */
	private int verticalStreak(char player) {
		int vertStreak = 0;
		int lineStreak = 0;
		int spaces = 0;
		
		for (int column = 0; column < BOARD_WIDTH; column++) {
			for (int row = BOARD_HEIGHT-1; row >= 0; row--) {
				if(board[row][column] == BOARD_SPACE) {
					spaces++;
					
				} else if (board[row][column] == player) {
					lineStreak++;
					
				} else {
					lineStreak = 0;
				}
			}
			if (gameWinAmount - lineStreak <= spaces && lineStreak > 1) {
				//Weight streaks of 3 or more
				if (lineStreak >= 3) {
					vertStreak += 3;
				}
				while (lineStreak > 0) {
					vertStreak++;
					lineStreak--;
				}
			}
			lineStreak = 0;
			spaces = 0;
		}
		return vertStreak;
	}
	
	/**
	 * Searches horizontal and assigns score for player streaks.
	 * @param player
	 * 		the player counter to search for
	 * @return horStreak
	 * 		the horizontal streak score for the player
	 */
	private int horizontalStreak(char player) {
		int horStreak = 0;
		int lineStreak = 0;
		int spaces = 0;
		
		for (int row = BOARD_HEIGHT-1; row >= 0; row--) {
			for (int column = 0; column < BOARD_WIDTH; column++) {
				if (board[row][column] == player) {
					lineStreak++;
					
				} else if (board[row][column] == BOARD_SPACE) {
					spaces++;
					
				} else {
					if (gameWinAmount - lineStreak <= spaces && lineStreak > 1) {
						//Weight streaks of 3 or more
						if (lineStreak >= 3) {
							horStreak += 3;
						}
						while (lineStreak > 0) {
							horStreak++;
							lineStreak--;
						}
					}
					lineStreak = 0;
					spaces = 0;
				}
			}
			if (gameWinAmount - lineStreak <= spaces && lineStreak > 1) {
				//Weight streaks of 3 or more
				if (lineStreak >= 3) {
					horStreak += 3;
				}
				while (lineStreak > 0) {
					horStreak++;
					lineStreak--;
				}
			}
			lineStreak = 0;
			spaces = 0;
		}
		return horStreak;
	}
	
	/**
	 * Searches diagonals and assigns score for player streaks.
	 * @param player
	 * 		the player counter to search for
	 * @return result
	 * 		the diagonal streak score for the player
	 */
	private int diagonalStreak(char player) {
		int result = 0;
		
		//Search and score both diagonals
		result += searchAscDiag(player);
		result += searchDescDiag(player);
		return result;
	}
	
	/**
	 * Searches ascending diagonal for streaks and assigns score.
	 * @param player
	 * 		the player to evaluate for streaks
	 * @return diagStreak
	 * 		the score for the respective diagonal streaks
	 */
	private int searchAscDiag (char player) {
		int diagStreak = 0;
		int spaces = 0;
		int lineStreak = 0;
		
		// Search ascending diagonals
		for (int row = BOARD_HEIGHT-1; row >= 0; row--){
			for (int col = 0; col < BOARD_WIDTH; col++) {
				if(board[row][col] == player || board[row][col] == BOARD_SPACE) {
					int searchDepth = 0;
					
					//Guard for array index out of bounds exception
					while (row - searchDepth >= 0 && col + searchDepth < BOARD_WIDTH) {
						if (board[row-searchDepth][col+searchDepth] == player) {
							lineStreak++;
						} else if (board[row-searchDepth][col+searchDepth] == BOARD_SPACE) {
							spaces++;
						} else {
							break;
						}
						searchDepth++;
					}
					//Assign score for streak
					if (gameWinAmount - lineStreak <= spaces && lineStreak > 1) {
						while (lineStreak > 0) {
							diagStreak++;
							lineStreak--;
						}
					}
					//Reset variables
					lineStreak = 0;
					spaces = 0;
				}
			}
		}
		return diagStreak;
	}
	
	/**
	 * Searches descending diagonal for streaks and assigns score.
	 * @param player
	 * 		the player to evaluate for streaks
	 * @return diagStreak
	 * 		the score for the respective diagonal streak
	 */
	private int searchDescDiag(char player) {
		int diagStreak = 0;
		int spaces = 0;
		int lineStreak = 0;
		
		// Search descending diagonals
		for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				if (board[row][col] == player || board[row][col] == BOARD_SPACE) {
					int searchDepth = 0;

					// Guard for array index out of bounds exception
					while (row - searchDepth >= 0 && col - searchDepth >= 0) {
						if (board[row - searchDepth][col - searchDepth] == player) {
							lineStreak++;
						} else if (board[row - searchDepth][col - searchDepth] == BOARD_SPACE) {
							spaces++;
						} else {
							break;
						}
						searchDepth++;
					}

					// Assign score for streak
					if (gameWinAmount - lineStreak <= spaces && lineStreak > 1) {
						while (lineStreak > 0) {
							diagStreak++;
							lineStreak--;
						}
					}
					// Reset variables
					lineStreak = 0;
					spaces = 0;
				}
			}
		}
		return diagStreak;
	}
	
	/**
	 * Check if the board has reached an end game state.
	 * @return end
	 * 		true if the board is in an end game state
	 */
	boolean endGame() {
		boolean end = false;
		
		//Check if the game has drawn - no turns left
		if (checkDraw()) {
			end = true;
		}
		
		//Check if any player has won
		for (char counter : Counter.playerSymbols) {
			if (checkWin(counter)) {
				end = true;
			}
		}
		return end;
	}
	
	/**
	 * Supports cloning of MinimaxBoard.
	 */
	public MinimaxBoard clone() throws CloneNotSupportedException {
		return (MinimaxBoard) super.clone();
	}
}