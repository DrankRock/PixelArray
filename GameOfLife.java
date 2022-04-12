package gameOfLife;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.Point;

public class GameOfLife {
	int size;
	boolean gameStart = false;
	int[][] ARR;
	
	/**
	 * Initialize to an array filled with 0
	 * @param n length and width of board (square)
	 */
	public GameOfLife(int n) {
		ARR = new int[n][n];
		for(int i=0; i<n; i++) {
			for(int j=0; j<n;j ++) {
				ARR[i][j] = 0;
			}
		}
		size = n;
	}
	
	/**
	 * Activates the game loop.
	 */
	public void startGame() {
		this.gameStart = true;
	}
	
	/**
	 * Determines the number of alive neighbors for a given point.
	 * @param x Point's x
	 * @param y Point's y
	 * @return an int containing the number of alive neihbors
	 */
	public int howManyNeighbors(int x, int y) {
		int nNeigh = 0;
		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {
				if(!(i == 0 && j == 0)) {
					try {
						if(ARR[x+i][y+j] == 1)
							nNeigh++;
					}
					/* if you're intrigued, this dirty shortcut is here
					 * so that if I reach outside of the array, I ignore it*/
					catch(Exception e) {
							continue;
					}
				}
			}
		}
		return nNeigh;
	}
	
	/**
	 * updates the Array to the next Array, taking into consideration
	 * Conway's Game of Life's rules.
	 * Conway's GoL states that : 
	 *    - an alive cell with 2 or 3 alive neighbors will live,
	 *    - an alive cell without 2 or 3 alive neighbors will die,
	 *    - a dead cell with exactly 3 alive neighbors will live,
	 *    - a dead cell without exactly 3 alive neighbors will stay dead.
	 * 
	 */
	public void nextArr() {
		if(gameStart) {
			int[][] nextARR = new int[this.size][this.size];
			for(int i=0; i<this.size; i++) {
				for(int j=0; j<this.size; j++) {
					int n = howManyNeighbors(i,j);
					if(ARR[i][j] == 1) {
						if(n==2 || n==3) {
							nextARR[i][j] = 1;
						} else {
							nextARR[i][j] = 0;
						}
					} else if(n==3) {
						nextARR[i][j] = 1;
					} else {
						nextARR[i][j] = 0;
					}
				}
			}
			ARR = nextARR;
		}
	}
	
	/**
	 * Translate an integer array with 0 and 1s to a Color Array with White and Black.
	 * @return the Color Array.
	 */
	public Color[][] toColor(){
		Color[][] ret = new Color[this.size][this.size];
		for(int i=0;i<this.size;i++) {
			for(int j=0;j<this.size;j++) {
				if(ARR[i][j] == 0) {
					ret[i][j] = Color.white;
				} else {
					ret[i][j] = Color.black;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Update the value of a cell with a new value
	 * @param point the point pointing to the cell
	 * @param value the new value (0 or 1)
	 */
	public void update(Point point,  int value) {
		if(value == 0 || value == 1)
			this.ARR[(int) point.getX()][(int) point.getY()] = value; 
		else
			throw new IllegalArgumentException("Value must be 0 or 1");
	}
	
	
	
	/**
	 * Main using the game of life class and PixelArray demonstrating keyboard and mouse input.
	 * @param args
	 */
	public static void main(String[] args) {
		int size = 50;
		PixelArray pa = new PixelArray(size,size,"Game Of Life",Color.white);
		pa.setPixelsBetween(1);
		
		GameOfLife GOL = new GameOfLife(size);
		
		/*
		 * This is an easy way to use click events. 
		 * A MouseAdapter is a class specially made to know where clicks happen. 
		 * mouseReleased does something when the right click has happened. Not when you click, but right after
		 * the click has happened.
		 * This mouseEventListener will be later added to pixelArray pa using pa.addMouseListener(..) (see below)
		 */
		class mouseEventListener extends MouseAdapter{
			public void mouseReleased(MouseEvent arg0) {
				/*
				 * The method pa.pixelCoordsToArrayCoords gets a point in pixels
				 * being the exact pixel clicked by the user, and it returns the cell
				 * clicked by the user. 
				 * Example : if each cell is composed of 10 pixels, separated by 1 gray pixel, 
				 * pa.pixelCoordsToArrayCoords(new Point(100,39) will return Point(9,3) (please remember that array starts at [0,0]
				 */
				GOL.update(pa.pixelCoordsToArrayCoords(arg0.getPoint()), 1);
				pa.updateArray(GOL.toColor());
			}
		}
		
		/*
		 * It works just like mouseAdapter, but checking for keyboard input instead
		 * of click coordinates. You can find the keyCode by printing the keyPressed.
		 */
		System.out.println("Press space to start");
		class startEventListener extends KeyAdapter{
			public void keyReleased(KeyEvent arg0) {
				int keyPressed = arg0.getKeyCode();
				//keyCode 32 is the space bar. 
				if(keyPressed == 32) {
					GOL.startGame();
				}
			}
		}
		
		/*
		 * Add both listeners to PixelArray. The keyboard needs to be added to pa's frame
		 * for it to work.
		 */
		pa.addMouseListener(new mouseEventListener());
		pa.getFrame().addKeyListener(new startEventListener());
		
		/*
		 * Game Loop. It is started immediately even though spacebar hasn't been pressed to
		 * update the array with the new cells added on click. 
		 * nextArr() doesn't work while GOL.gameStart == false.
		 * When spacebar is pressed, startEventListener sees it and starts the game.
		 */
		while(true) {
			pa.updateArray(GOL.toColor());
			GOL.nextArr();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				continue;
			}
		}
	}
	
	
}
