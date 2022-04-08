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
	int[][] ARR = new int[][]{
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,1,0,0,0,0},
		{0,0,1,0,1,1,1,0,0,0},
		{0,0,0,0,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
	};
	
	public GameOfLife(int n) {
		ARR = new int[n][n];
		for(int i=0; i<n; i++) {
			for(int j=0; j<n;j ++) {
				ARR[i][j] = 0;
			}
		}
		size = n;
	}
	
	public void startGame() {
		this.gameStart = true;
	}
	
	public int howManyNeighbors(int x, int y) {
		int nNeigh = 0;
		for(int i=-1; i<=1; i++) {
			for(int j=-1; j<=1; j++) {
				if(!(i == 0 && j == 0)) {
					try {
						if(ARR[x+i][y+j] == 1)
							nNeigh++;
					}
					catch(Exception e) {
						/*
						 * in case you're intrigued, this dirty shortcut is here
						 * so that if I reach outside of the array, I ignore it*/
							continue;
					}
				}
			}
		}
		return nNeigh;
	}
	
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
	
	public void update(int x, int y, int value) {
		this.ARR[y][x] = value; 
	}
	
	public void update(Point point,  int value) {
		this.ARR[(int) point.getX()][(int) point.getY()] = value; 
	}
	
	
	
	
	public static void main(String[] args) {
		int size = 50;
		PixelArray pa = new PixelArray(size,size,"Game Of Life",Color.white);
		pa.setPixelsBetween(1);
		boolean gameStart = false;	
		
		
		GameOfLife GOL = new GameOfLife(size);
		class mouseEventListener extends MouseAdapter{
			public void mouseReleased(MouseEvent arg0) {
				GOL.update(pa.pixelCoordsToArrayCoords(arg0.getPoint()), 1);
				pa.updateArray(GOL.toColor());
			}
		}
		System.out.println("Press space to start");
		class startEventListener extends KeyAdapter{
			public void keyReleased(KeyEvent arg0) {
				int keyPressed = arg0.getKeyCode();
				if(keyPressed == 32) {
					GOL.startGame();
				}
			}
		}
		
		pa.addMouseListener(new mouseEventListener());
		pa.getFrame().addKeyListener(new startEventListener());
		
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
