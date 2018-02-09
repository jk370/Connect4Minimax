/**
 * Abstract class representing a player entity and associated methods and attributes.
 * 
 * @author Jordan Koulouris
 * @version 1.0
 * @release 12/01/2018
 * @see MyConnectN.java
 *
 */
public abstract class Player {
	public Counter counter;
	
	/**
	 * Constructor setting unique counter to player.
	 */
	public Player() {
		this.counter = new Counter();
	}
	
	/**
	 * Abstract method to ensure all player types define getNextMove.
	 * @param board
	 * 		the current state of the game board
	 * @return
	 * 		integer defining which column to place the counter.
	 */
	abstract int getNextMove(Board board);
}