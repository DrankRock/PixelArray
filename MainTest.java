package main;

import java.awt.Color;
import java.util.Random;

public class MainTest {
	public static void main(String[] args) {
		//PixelArray pa = new PixelArray(20,50, "test");
		simpleDesign();

	}
	
	public static void simpleDesign() {
		PixelArray pa = new PixelArray(50, 50, "Test", Color.white);
		Color[][] ARRAY = new Color[50][50];
		Random r;
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
			
		}
		
	}
}
