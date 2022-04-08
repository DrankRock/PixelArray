package gameOfLife;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * Simple way to draw a 2D Array as a screen of colored pixels in Java using default libraries
 * Heavily inspired from "PixelScreen" that I could find nowhere
 * Made to respond to my specific needs, but I can update if needed
 * @author Matvei Pavlov
 * @version 0.3 (16-Mar-22)
 * @since 02 Feb 2022
 *
 */

public class PixelArray extends JPanel{
	//necessary for execution
	private static final long serialVersionUID = -6342058807695314872L;
	private double ARRAY_HEIGHT;
	private double ARRAY_WIDTH;
	private double SCREEN_WIDTH;
	private double SCREEN_HEIGHT;
	private int _nPixelsBetweenCol;
	private int _nPixelsBetweenRow;
	private JFrame _frame;
	private JPanel _latest;
	private static final int inFocused = JComponent.WHEN_IN_FOCUSED_WINDOW;
	private Color[][] ARRAY;
	private boolean _DEBUG = false;
	private boolean isVisible = true;
	private boolean IS_RUNNING = true;
	private boolean useAsJPanel = false;
	ExecutorService service = Executors.newFixedThreadPool(10);
	int[][] sizeColumns;
	int[][] sizeRows;
	
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
		dPrint("Creating array of height : "+array.length+" and width "+array[0].length);
	}
	
	public PixelArray(int HEIGHT, int WIDTH, String title, Color color) {
		this(HEIGHT, WIDTH, title, color, 500, 500, false);
	}
	
	public PixelArray(int HEIGHT, int WIDTH, String title, Color color, int frame_width, int frame_height, boolean useAsJPanel) {
		if(HEIGHT < 0 || WIDTH < 0 || HEIGHT == 0 && WIDTH == 0) {
			throw new IllegalArgumentException("Incorrect array size");
			
		}
		this.ARRAY_HEIGHT = HEIGHT;
		this.ARRAY_WIDTH = WIDTH;
		this.setPixelsBetween(0);
		this.setPreferredSize(new Dimension(frame_width,frame_height));
		this.useAsJPanel = useAsJPanel;
		this.ARRAY = new Color[HEIGHT][WIDTH];
		for(int i=0; i<HEIGHT;i++) {
			for(int j=0;j<WIDTH;j++) {
				ARRAY[i][j] = color;
			}
		}
		sizeColumns = new int[(int) ARRAY_WIDTH][2];
    	sizeRows = new int[(int) ARRAY_HEIGHT][2];
		this.addComponentListener(new ResizeListener());
		
		if(!useAsJPanel)
			this.frameStart(title, frame_width, frame_height);
		SCREEN_WIDTH = getSize().width;
		SCREEN_HEIGHT = getSize().height;
		updateRowsAndColumnsSize();
		addMouseListener(new mouseEventListener());
    	
	}
	
	@Override
    public void paint(Graphics g) {
		SCREEN_WIDTH = getSize().width;
		SCREEN_HEIGHT = getSize().height;
        //System.out.println("w : "+SCREEN_WIDTH+"; h : "+SCREEN_HEIGHT);
        paintArray(g);
    }
	
	public void frameStart(String title) {
		frameStart(title, 1000, 1000);
	}
	
	public void frameStart(String title, int width, int height) {
		_latest = this;
		_frame = new JFrame();
		_frame.setTitle(title);
		_frame.getContentPane().setPreferredSize(new Dimension(width,height));
		_frame.setResizable(true);
		_frame.setLocationRelativeTo(null);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setBackground(Color.lightGray);
		_frame.add(_latest);
		_frame.pack();
		_frame.setVisible(true);
	}
	
	public void frameSetVisible(boolean bool) {
		if(this.useAsJPanel == true) {
			throw new IllegalStateException("Can't set frame visibility in JPanel mode");
		}
		_frame.setVisible(bool);
		this.isVisible = bool;
	}
	
	private void paintArray(Graphics g) {
		dPrint("private call to paintArray()");
		Graphics2D g2d = (Graphics2D) g;
		/* TODO: make this multithreaded, with one thread doing even and one thread doing odds*/
		for(int i=0; i<ARRAY_HEIGHT; i++) {
    		int varY = sizeRows[i][1];
			for(int j=0; j<ARRAY_WIDTH; j++) {
		    	g.setColor(ARRAY[i][j]);
		    	int varX = sizeColumns[j][1];
		    	g2d.fillRect(varX,varY,sizeColumns[j][0]-this._nPixelsBetweenCol,sizeRows[i][0]-this._nPixelsBetweenRow);
		    	dPrint("fillrect starting at "+varX+","+varY+" to "+ARRAY[i][j]);
		    	}
			dPrint("");
		}
	}
	
	/**
	 * When the user resizes the screen, recalculates the size of each rows and columns to have no lost pixels.
	 */
	private void updateRowsAndColumnsSize() {
		this.dPrint("Update Rows and Columns Sizes : ");
		// Rounded to the smallest to be int, which puts some unused pixels at the end
		int sizeCol = (int) ((SCREEN_WIDTH/ARRAY_WIDTH)- _nPixelsBetweenCol);
    	int sizeRow = (int) ((SCREEN_HEIGHT/ARRAY_HEIGHT)- _nPixelsBetweenRow);
    	// Calculate the number of unused pixels
    	int lostColumnPixels = (int) (SCREEN_WIDTH-(sizeCol+_nPixelsBetweenRow)*ARRAY_WIDTH);
    	int lostRowPixels = (int) (SCREEN_HEIGHT-(sizeRow+_nPixelsBetweenRow)*ARRAY_HEIGHT);
    	dPrint("SizeCol : "+sizeCol+"; sizeRow : "+sizeRow+"; lostColumnPixels = "+lostColumnPixels+"; lostRowPixels = "+lostRowPixels+"\nscreen width = "+SCREEN_WIDTH+"; screen height ="+SCREEN_HEIGHT);
    	// if we have n unused pixels, the first n squares will get a 1px bonus, on row or/and height
    	for(int i=0; i<ARRAY_WIDTH;i++) {
    		sizeColumns[i][0] = sizeCol+_nPixelsBetweenCol;
    		if(lostColumnPixels > 0)
    			sizeColumns[i][0]++;
    		if(i>0)
    			sizeColumns[i][1] = sizeColumns[i-1][1]+sizeColumns[i-1][0];
    		else
    			sizeColumns[i][1] = 0;
    		lostColumnPixels--;
    	}
    	for(int i=0; i<ARRAY_HEIGHT;i++) {
    		sizeRows[i][0] = sizeRow+_nPixelsBetweenRow;
    		if(lostRowPixels > 0)
    			sizeRows[i][0]++;
    		if(i>0)
    			sizeRows[i][1] = sizeRows[i-1][1]+sizeRows[i-1][0];
    		else
    			sizeRows[i][1] = 0;
    		lostRowPixels--;
    	}
	}
	
	public void showAgain() {
		if(!this.useAsJPanel) {
			dPrint("private call to showAgain()");
			_frame.remove(_latest);
			_latest = this;
			_frame.add(_latest);
			_frame.revalidate();
			_frame.repaint();
			//_frame.pack(); //I commented that because it auto-resizes on every showAgain to previous size. 
		}
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
	
	/**
	 * Update a single pixel to a given color
	 * @param x the x coordinate of the pixel (starts at 0)
	 * @param y the y coordinate of the pixel (starts at 0)
	 * @param color the color (ex : Color.blue)
	 */
	public void updatePixel(int x, int y, Color color) {
		ARRAY[y][x] = color;
		this.showAgain();
		dPrint("update pixel ["+x+","+y+"] to "+color);
	}
	
	public void updatePixel(Point point, Color color) {
		this.updatePixel(point.x, point.y, color);
	}
	
	/**
	 * Update a single pixel to a given color but dont update the screen
	 * @param x the x coordinate of the pixel (starts at 0)
	 * @param y the y coordinate of the pixel (starts at 0)
	 * @param color the color (ex : Color.blue)
	 */
	public void updatePixelDontShow(int x, int y, Color color) {
		ARRAY[y][x] = color;
		dPrint("update pixel ["+x+","+y+"] to "+color+" but not showing it.");
	}
	
	/**
	 * Update the full screen with a Color[][] Array
	 * @param newArray the new Array.
	 */
	public void updateArray(Color[][] newArray) {
		dPrint("Update array ...");
		for(int i=0; i<ARRAY_HEIGHT; i++) {
			for(int j=0; j<ARRAY_WIDTH; j++) {
				dPrint(newArray[i][j]);
				ARRAY[i][j] = newArray[i][j];
			}
			dPrint("");
		}
		this.showAgain();
	}
	
	/**
	 * Update the full screen with a Color[][] Array but don't update the screen
	 * @param newArray the new Array.
	 */
	public void updateArrayDontShow(Color[][] newArray) {
		dPrint("Update array (not showing it)...");
		for(int i=0; i<ARRAY_HEIGHT; i++) {
			for(int j=0; j<ARRAY_WIDTH; j++) {
				dPrint(newArray[i][j]);
				ARRAY[i][j] = newArray[i][j];
			}
			dPrint("");
		}
	}
	
	public JFrame getFrame() {
		if(this.useAsJPanel == true) {
			throw new IllegalStateException("Can't get frame in JPanel mode");
		}
		return _frame;
	}
	
	public void setPixelsBetweenColumns(int pixels) {
		this._nPixelsBetweenRow = pixels;
	}
	public void setPixelsBetweenRows(int pixels) {
		this._nPixelsBetweenCol = pixels;
	}
	
	/**
	 * Set the number of pixels (literally pixels) between each rectangles of the array
	 * @param pixels 
	 */
	public void setPixelsBetween(int pixels) {
		setPixelsBetweenRows(pixels);
		setPixelsBetweenColumns(pixels);
		dPrint("Number of pixels between each rows and columns : "+pixels);
	}
	/**
	 * Update the dimensions of the frame
	 * @param width
	 * @param height
	 * @hidden (Currently not working !)
	 */
	public void setDimension(int width, int height) {
		_frame.getContentPane().setPreferredSize(new Dimension(width,height));
		SCREEN_WIDTH = getSize().width;
		SCREEN_HEIGHT = getSize().height;
		_frame.validate();
        _frame.repaint();
        _frame.pack();
		dPrint("Dimensions updated to "+width+"x"+height);
	}
	
	/**
	 * Automatically update the frame every ms milliseconds
	 * @param ms long defining the number of milliseconds between each updates
	 */
	public void autoUpdate(long ms) {
	    service.submit(new Runnable() {
	        public void run() {
	        	while(IS_RUNNING) {
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
	 * @return the value of isVisible
	 */
	public boolean getVisible() {
		return this.isVisible;
	}
	public Point pixelCoordsToArrayCoords(Point point) {
		return this.pixelCoordsToArrayCoords(point.x, point.y);
	}
	public Point pixelCoordsToArrayCoords(int x, int y) {
		int lastX = 0;
		int lastY = 0;
		int currentX;
		int currentY;
		int finalX = -1;
		int finalY = -1;
		for(int i=1; i<ARRAY_WIDTH; i++) {
			currentX = this.sizeColumns[i][1];
			if(x>=lastX && x<=currentX) {
				finalX = i-1;
				break;
			}
			lastX = currentX;
		}
		if(finalX == -1) {
			finalX = (int) (ARRAY_WIDTH-1);
		}
		for(int i=1; i<ARRAY_HEIGHT; i++) {
			currentY = this.sizeRows[i][1];
			if(y>=lastY && y<=currentY) {
				finalY = i-1;
				break;
			}
			lastY = currentY;
		}
		if(finalY == -1) {
			finalY = (int) (ARRAY_HEIGHT-1);
		}
		return new Point(finalY, finalX); //tbh I don't understand why it's inverted, but meh whatever
	}
	
	/**
	 * Close the frame, dispose of it, stops any autoUpdate.
	 */
	public void exit() {
		dPrint("Exiting ...");
		_frame.setVisible(false);
		_frame.dispose();
		IS_RUNNING = false;
	}
	
	/**
	 * Debug print, used to be activated or de-activated
	 * @param o what is to be print
	 */
	public void dPrint(Object o) {
		if(_DEBUG) {
			System.out.println(o.toString());
		}
	}
	
	/**
	 * Set the debug mode
	 * @param bool true to print everything, false to print nothing
	 */
	public void debugMode(boolean bool) {
		this._DEBUG = bool;
	}
	
	class ResizeListener extends ComponentAdapter{
		public void componentResized(ComponentEvent e) {
			updateRowsAndColumnsSize();
		}
	}
	
	/**
	 * Example MouseEventListener class to execute action on mouse click.
	 * Feel free to copy this, and do some actions. Use mouseClickToCoords(int x, int y) to get the
	 * coordinates in the array of this point. 
	 * do myPixelArray.add(myMouseEventListener) to link pixelArray and mouse clicks
	 *
	 */
	public class mouseEventListener extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
		}
		@Override
		public void mouseExited(MouseEvent arg0) {
		}
		@Override
		public void mousePressed(MouseEvent e) {
		}
		@Override
		public void mouseReleased(MouseEvent arg0) {
			//System.out.println();
			//updatePixel(pixelCoordsToArrayCoords(arg0.getX(), arg0.getY()), Color.blue);
		}
	}

}
