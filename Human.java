import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Represents a human player of the game.
 * 
 * @author Jordan Koulouris
 * @version 1.0
 * @release 12/01/2018
 * @see Player.java
 *
 */
public class Human extends Player {
	private BufferedReader input;

	/**
	 * Calls Player constructor. Initialises BufferedReader for turn.
	 */
	public Human() {
		super();
		input = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * Reads user input until a valid number is given.
	 * @param board
	 * 		the current state of the game board
	 * @return position
	 * 		the validated position to place the counter in.
	 */
	@Override
	int getNextMove(Board board) {
		int position = 0;
		boolean valid = false;
		String turn = null;
		
		while (!valid) {
			try {
				//Take input and attempt parse into integer
				turn = input.readLine();
				position = Integer.parseInt(turn);
				
				//Validate input
				if(checkInput(board, position)) {
					valid = true;
				} else {
					System.out.println("Please enter a valid column.");
				}
				
			} catch (Exception e) {
				System.out.println("Please enter a valid column.");
			}
		}
		return position;
	}

	/**
	 * Checks whether input received is a valid column.
	 * @param position
	 * 		Column given by user.
	 * @return valid
	 * 		returns true if input is a valid column.
	 */
	private boolean checkInput(Board board, int position) {
		boolean valid = false;
		
		//Make sure position is in range and column is not full
		if (position >= 1 && position <= 7 && board.getBoard()[0][position-1] == Board.BOARD_SPACE) {
			valid = true;
		}
		return valid;
	}
}