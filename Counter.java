/**
 * Represents a counter entity assigned to each player.
 * 
 * @author Jordan Koulouris
 * @version 1.0
 * @release 12/01/2018
 * @see Player.java
 *
 */
public class Counter {
	public static final char[] playerSymbols = {'r', 'y', 'b'};
	private static int playerCount;
	private char symbol;
	
	/**
	 * Constructor increments static player count and assigns unique symbol.
	 */
	public Counter() {
		playerCount++;
		assignCounter();
	}
	
	/**
	 * Accessor for counter symbol.
	 * @return symbol
	 */
	public char getSymbol() {
		return this.symbol;
	}
	
	/**
	 * Assigns unique player symbol from playerSymbols array.
	 */
	private void assignCounter() {
		if (playerCount <= playerSymbols.length) {
			this.symbol = playerSymbols[playerCount-1];
		} else {
			//End programme gracefully if too many players added.
			System.out.println("Error: Too many players added.");
			System.exit(0);
		}
	}
}