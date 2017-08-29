

import java.awt.*;
import java.awt.image.*;


import javax.swing.*;
import javax.swing.border.TitledBorder;


public class DisplayImage {
	
	JFrame frame;
	JLabel lbIm1;
	JLabel lbIm2;
	BufferedImage imgOriginal;
	BufferedImage imgCompressed;
	
	/**
	 * Display the original image and compressed image side by side
	 * @param input byte array of image
	 * @param output byte array of image
	 * @param width
	 * @param height
	 * @param isColor boolean
	 */
	public void showImage(byte[] input, byte[] output, int width, int height, boolean isColor){
		imgOriginal = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		imgCompressed = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		int[][] inputPixArray= new int[height][width];
		int[][] outputPixArray= new int[height][width];
							
		inputPixArray = createPixelsFromByteArray(input,height,width, isColor);
		outputPixArray = createPixelsFromByteArray(output,height,width, isColor);	
		
	
		//Render Image
		frame = new JFrame();			
		GridBagLayout gLayout = new GridBagLayout();			
		frame.getContentPane().setLayout(gLayout);			
		
		imgOriginal = setImagePixels(width, height,imgOriginal,inputPixArray);
		imgCompressed = setImagePixels(width, height,imgCompressed,outputPixArray);	
		
		//Add original Image	
		lbIm1 = new JLabel(new ImageIcon(imgOriginal));
		TitledBorder Oborder = new TitledBorder("Original Image");		
		Oborder.setTitleJustification(TitledBorder.CENTER);
		Oborder.setTitlePosition(TitledBorder.TOP);
		JPanel panel1 = new JPanel();
		panel1.setBorder(Oborder);
		panel1.add(lbIm1);
		
		//Add Compressed Image
		lbIm2 = new JLabel(new ImageIcon(imgCompressed));
		TitledBorder Cborder = new TitledBorder("Compressed Image");	
		Cborder.setTitleJustification(TitledBorder.CENTER);
		Cborder.setTitlePosition(TitledBorder.TOP);
		JPanel panel2 = new JPanel();		
		panel2.setBorder(Cborder);	
		panel2.add(lbIm2);
		
		frame.add(panel1);
		frame.add(panel2);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);	
				
	
	}
	
	/**
	 * Sets the pixels of the image
	 * @param width
	 * @param height
	 * @param img
	 * @param pixArray
	 * @return
	 */
	private BufferedImage setImagePixels(int width, int height, 
			BufferedImage img, int[][] pixArray) {
		for(int y = 0; y < height; y++){					
			for(int x = 0; x < width; x++){												
				img.setRGB(x,y,pixArray[y][x]);						
			}					
		}
		return img;
	}
	
	
	/**
	 * Create pixel array from the bytes
	 * @param inputeByteArray
	 * @param height
	 * @param width
	 * @param isColor
	 * @return pixel array
	 */
	private static int[][] createPixelsFromByteArray(byte[] inputeByteArray, int height, int width, boolean isColor){
		int[][] pixArray = new int[height][width];			
		
		int ind = 0;
		
			for(int y = 0; y < height; y++){					
				for(int x = 0; x < width; x++){						
					byte a = 0;						
					byte r = inputeByteArray[ind];
					byte g;
					byte b;
					if(isColor){
						g = inputeByteArray[ind+height*width];
						b = inputeByteArray[ind+height*width*2]; 
					}
					else{
						g = inputeByteArray[ind];
						b = inputeByteArray[ind]; 
					}
											

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					pixArray[y][x] = pix;						
					ind++;
				}	
			}
		
			
			
		return pixArray;
	}

}
