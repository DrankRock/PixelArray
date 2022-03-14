package gameOfLife;

import java.awt.Color;

public class GameOfLife {
	int[][] ARR = new int[][]{
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,1,0,0,0,0},
		{0,0,0,0,1,1,1,0,0,0},
		{0,0,0,0,0,1,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0,0},
	};
	
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
						//yeah, that's a dirty shortcut
							continue;
					}
				}
			}
		}
		return nNeigh;
	}
	
	public void nextArr() {
		int[][] nextARR = new int[10][10];
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
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
	
	public Color[][] toColor(){
		Color[][] ret = new Color[10][10];
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				if(ARR[i][j] == 0) {
					ret[i][j] = Color.white;
				} else {
					ret[i][j] = Color.black;
				}
			}
		}
		return ret;
	}
	
	
	
	public static void main(String[] args) {
		PixelArray pa = new PixelArray(10,10,"Game Of Life",Color.blue);
		pa.setPixelsBetween(1);
		
		GameOfLife GOL = new GameOfLife();
		
		while(true) {
			pa.updateArray(GOL.toColor());
			GOL.nextArr();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				continue;
			}
			

			
		}
		
	}
}
