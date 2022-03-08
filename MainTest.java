package main;

import java.awt.Color;
import java.awt.Dimension;

public class MainTest {
	public static void main(String[] args) {
		//PixelArray pa = new PixelArray(20,50, "test");
		//simpleDesign();
		int width = 47;
		int height = 103;
		PixelArray pa = new PixelArray(height, width, "Test", Color.white, 1000, 500, false);
		Color[][] ARRAY = new Color[height][width];
		int count = 0;
		for(int i=0; i<height; i++) {
			for(int j=0; j<width; j++) {
				Color current = ARRAY[i][j];
				if (current == Color.white)
					ARRAY[i][j] = Color.black;
				else
					ARRAY[i][j] = Color.white;
			}
		}
		pa.debugMode(true);
		pa.updateArray(ARRAY);

	}
	
	public static void simpleDesign() {
		PixelArray pa = new PixelArray(50, 50, "Test", Color.white);
		Color[][] ARRAY = new Color[50][50];
		int count = 0;
		while(true) {
			for(int i=0; i<50; i++) {
				for(int j=0; j<50; j++) {
					Color current = ARRAY[i][j];
					if (current == Color.white)
						ARRAY[i][j] = Color.black;
					else
						ARRAY[i][j] = Color.white;
				}
			}
			pa.updateArray(ARRAY);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(count == 5) {
				pa.setPreferredSize(new Dimension(100,100));
			}
			if(count++ == 10) {
				pa.exit();
				break;
			}
			
		}
		
	}
	
	
}
