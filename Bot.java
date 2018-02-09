import java.util.Random;

/**
 * Represents a robot player of the game.
 * 
 * @author Jordan Koulouris
 * @version 1.0
 * @release 12/01/2018
 * @see Player.java
 *
 */
public class Bot extends Player {
	BotSearch search;
	Random turn;

	/**
	 * Calls Player constructor and initialises BotSearch and Random object.
	 */
	public Bot() {
		super();
		search = new BotSearch(counter.getSymbol());
		turn = new Random();
	}
	
	/**
	 * Gets next move of robot player via min-max.
	 * @param board
	 * 		the current state of the game board
	 * @return move
	 * 		column to place the counter in
	 */
	@Override
	int getNextMove(Board board) {
		int move;
		int depth = 6; //Depth of turns to search
		
		//Find best move using min-max search
		move = search.getBestColumn(board, depth);
		
		//Replace with random column should min-max return a full column
		while (board.getBoard()[0][move-1] != Board.BOARD_SPACE) {
			move = turn.nextInt(7) + 1;
		}
		return move;
	}
}