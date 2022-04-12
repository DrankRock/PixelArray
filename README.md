# PixelArray
 A java 2D pixel like frame for easy pixelated visuals

## Objective
The main objective is to make 2D games very easily with pixelated visuals. It can be used for virtually anything if it can be represented using a pixelated view.

## Usage 
This project isn't currently available as a library. You can use it by adding the PixelArray.java to your project and directly creating and using a PixelArray object. 

### Setup
```java
// Create a new PixelArray object
int width = 100;
int height = 200;
String title = "PixelArray Demo";
Color backgroundColor = Color.white;
PixelArray myPixelArray = new PixelArray(width, height, title, backgroundColor);
new PixelArray(size,size,"Game Of Life",Color.white);
```

Immediately after this, a new window containing the 100 by 200 pixelArray will open. To update it you need a simple game loop such as :
### Looping
```java

```

## Example
A complete Conway's Game of Life using PixelArray for the visual can be found in gameOfLife.java. It is fully working and commented.  
Here is a Demo of it working. The first part is sped because I really wanted to show off making a launcher. 

### Demo  


https://user-images.githubusercontent.com/32172257/162944629-cdc8d1b9-f02a-4f31-ace0-899b164eafe1.mp4


## What next ?
Next steps will be to make it possible to use PixelArray as a JPanel instead of it having its own window.  
I will also make it run faster with multi-threaded array filling, and make it more secure to multithread a pixelarray.
