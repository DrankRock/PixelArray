package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * Simple way to draw a 2D Array as a screen of colored pixels in Java using default libraries
 * Heavily inspired from "PixelScreen" that I could find nowhere
 * Made to respond to my specific needs, but I can update if needed
 * @author Matvei Pavlov
 * @version 0.1
 * @since 02 Feb 2022
 *
 */

public class PixelArray extends JPanel{
	//necessary for execution
	private static final long serialVersionUID = -6342058807695314872L;
	private int ARRAY_HEIGHT;
	private int ARRAY_WIDTH;
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private int _nPixelsBetweenHeight;
	private int _nPixelsBetweenWidth;
	private JFrame _frame;
	private JPanel _latest;
	private Color[][] ARRAY;
	private boolean _DEBUG = false;
	ExecutorService service = Executors.newFixedThreadPool(10);
	
	@SuppressWarnings("unused")
	public static final Map<String, Color> strToCol = initMap();
	
	public PixelArray() {
		this(50, 50, "Test !", Color.white);
	}
	
	public PixelArray(int SIZE) {
		this(SIZE, SIZE, "test", Color.white);
	}
	
	public PixelArray(Color[][] array, String title) {
		this(array.length,array[0].length, title, Color.white);
		updateArray(array);
		dPrint("Creating array of height : "+array.length+" and width "+array[0].length, _DEBUG);
	}
	
	public PixelArray(int HEIGHT, int WIDTH, String title, Color color) {
		if(HEIGHT < 0 || WIDTH < 0 || HEIGHT == 0 && WIDTH == 0) {
			throw new IllegalArgumentException("Incorrect array size");
			
		}
		this._nPixelsBetweenHeight = 1;
		this._nPixelsBetweenWidth = 1;
		this.ARRAY_HEIGHT = HEIGHT;
		this.ARRAY_WIDTH = WIDTH;
		this.ARRAY = new Color[HEIGHT][WIDTH];
		for(int i=0; i<HEIGHT;i++) {
			for(int j=0;j<WIDTH;j++) {
				ARRAY[i][j] = color;
			}
		}
		this.frameStart(title);
	}
	
	@Override
    public void paint(Graphics g) {
		SCREEN_WIDTH = getSize().width;
		SCREEN_HEIGHT = getSize().height;
        //System.out.println("w : "+SCREEN_WIDTH+"; h : "+SCREEN_HEIGHT);
        paintArray(g);
    }
	
	public void frameStart(String title) {
		_latest = this;
		_frame = new JFrame();
		_frame.setTitle(title);
		_frame.getContentPane().setPreferredSize(new Dimension(500,500));
		_frame.setResizable(true);
		_frame.setLocationRelativeTo(null);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setBackground(Color.lightGray);
		_frame.add(_latest);
		_frame.pack();
		_frame.setVisible(true);
	}
	
	public void frameSetVisible(boolean bool) {
		_frame.setVisible(bool)
	}
	
	private void paintArray(Graphics g) {
		dPrint("private call to paintArray()", _DEBUG);
		Graphics2D g2d = (Graphics2D) g;
		int sizeCol = (SCREEN_WIDTH/ARRAY_WIDTH)- _nPixelsBetweenWidth;
    	int sizeRow = (SCREEN_HEIGHT/ARRAY_HEIGHT)- _nPixelsBetweenHeight;
    	dPrint("size Column : "+sizeCol+", size Row : "+sizeRow, _DEBUG);
    	for(int i=0; i<ARRAY_HEIGHT; i++) {
			for(int j=0; j<ARRAY_WIDTH; j++) {
		    	g.setColor(ARRAY[i][j]);
		    	int varY = (i*_nPixelsBetweenWidth+(i)*sizeRow);
		    	int varX = (j*_nPixelsBetweenHeight+(j)*sizeCol);
		    	g2d.fillRect(varX,varY,sizeCol,sizeRow);
		    	dPrint("fillrect starting at "+varX+","+varY+" to "+ARRAY[i][j], _DEBUG);
		    	}
			dPrint("",_DEBUG);
		}
	}
	
	private void showAgain() {
		dPrint("private call to showAgain()", _DEBUG);
		_frame.remove(_latest);
		_latest = this;
		_frame.add(_latest);
		_frame.revalidate();
		_frame.repaint();
		_frame.pack();
	}
	
	private static Map<String, Color> initMap() {
        Map<String, Color> map = new HashMap<>();
        map.put("black", Color.black);
        map.put("BLACK", Color.BLACK);
        map.put("blue", Color.blue);
        map.put("BLUE", Color.BLUE);
        map.put("cyan", Color.cyan);
        map.put("CYAN", Color.CYAN);
        map.put("DARK_GRAY", Color.DARK_GRAY);
        map.put("darkGray", Color.darkGray);
        map.put("gray", Color.gray);
        map.put("GRAY", Color.GRAY);
        map.put("green", Color.green);
        map.put("GREEN", Color.GREEN);
        map.put("LIGHT_GRAY", Color.LIGHT_GRAY);
        map.put("lightGray", Color.lightGray);
        map.put("magenta", Color.magenta);
        map.put("MAGENTA", Color.MAGENTA);
        map.put("orange", Color.orange);
        map.put("ORANGE", Color.ORANGE);
        map.put("pink", Color.pink);
        map.put("PINK", Color.PINK);
        map.put("red", Color.red);
        map.put("RED", Color.RED);
        map.put("white", Color.white);
        map.put("WHITE", Color.WHITE);
        map.put("yellow", Color.yellow);
        map.put("YELLOW", Color.YELLOW);
        return Collections.unmodifiableMap(map);
    }
	
	public void updatePixel(int x, int y, Color color) {
		ARRAY[y][x] = color;
		this.showAgain();
		dPrint("update pixel ["+x+","+y+"] to "+color, _DEBUG);
	}
	public void updatePixelDontShow(int x, int y, Color color) {
		ARRAY[y][x] = color;
		dPrint("update pixel ["+x+","+y+"] to "+color+" but not showing it.", _DEBUG);
	}
	public void updateArray(Color[][] newArray) {
		dPrint("Update array ...", _DEBUG);
		for(int i=0; i<ARRAY_HEIGHT; i++) {
			for(int j=0; j<ARRAY_WIDTH; j++) {
				dPrint(newArray[i][j], _DEBUG);
				ARRAY[i][j] = newArray[i][j];
			}
			dPrint("", _DEBUG);
		}
		this.showAgain();
	}
	
	public void updateArrayDontShow(Color[][] newArray) {
		dPrint("Update array (not showing it)...", _DEBUG);
		for(int i=0; i<ARRAY_HEIGHT; i++) {
			for(int j=0; j<ARRAY_WIDTH; j++) {
				dPrint(newArray[i][j], _DEBUG);
				ARRAY[i][j] = newArray[i][j];
			}
			dPrint("", _DEBUG);
		}
	}
	
	public JFrame getFrame() {
		return _frame;
	}
	
	public void setPixelsBetweenColumns(int pixels) {
		this._nPixelsBetweenWidth = pixels;
	}
	public void setPixelsBetweenRows(int pixels) {
		this._nPixelsBetweenHeight = pixels;
	}
	public void setPixelsBetween(int pixels) {
		setPixelsBetweenRows(pixels);
		setPixelsBetweenColumns(pixels);
		dPrint("Number of pixels between each rows and columns : "+pixels, _DEBUG);
	}
	public void setDimension(int width, int height) {
		_frame.getContentPane().setPreferredSize(new Dimension(width,height));
		dPrint("Dimensions updated to "+width+"x"+height, _DEBUG);
	}
	
	public void autoUpdate(long ms) {
	    service.submit(new Runnable() {
	        public void run() {
	        	while(true) {
	        		showAgain();
	        		try {
						Thread.sleep(ms);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	        	}
	        }
	    });
	}
	
	/**
	 * Debug print, used to be activated or de-activated
	 * @param o what is to be print
	 * @param shouldIPrint true if debug mode is activated
	 */
	public void dPrint(Object o, boolean shouldIPrint) {
		if(shouldIPrint) {
			System.out.println(o.toString());
		}
	}
	
	public void debugMode(boolean bool) {
		this._DEBUG = bool;
	}
	
	
	
	

}
