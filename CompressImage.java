
import java.io.*;
import java.util.HashMap;

public class CompressImage {
	
	private static final int IMAGE_WIDTH = 352;
	private static final int IMAGE_HEIGHT = 288;
	
	private int sizeOfCodebook;
	private String fileName;
	private boolean isColor;
	
	
	public CompressImage(String[] args){
		this.fileName = args[0];
		this.sizeOfCodebook = Integer.parseInt(args[1]);	
		this.isColor = fileName.substring(fileName.length() - 3).equals("rgb");
		
	}
	
	public static void main(String[] args) {
		
		if(args.length != 2){
			System.out.println("Please provide the image file name and size of the codebook");
			System.exit(0);
		}
		
		
		//long t = System.currentTimeMillis();
		CompressImage compress = new CompressImage(args);
		
		if((compress.sizeOfCodebook & (compress.sizeOfCodebook  - 1)) != 0){
			System.out.println("size of the codebook is not a power of 2");
			System.exit(0);
		}
		//Get the input byte array for the final display
		byte[] inputByteArray = readFileIntoBytes(compress.fileName);
		System.out.println("Read File into bytes");
		
		///Make the source input vector
		Vector[] inputVectorArray = readFile(inputByteArray);				
		System.out.println("Created Input vector");
		
		//Create & Initialize the CodeBook
		System.out.println("Initializing Codewords");
		CodeBook codebook = new CodeBook(compress.sizeOfCodebook);
		//codebook.printCodeword();
		
		//Initialize the cluster
		Helper inputclusterMapping = new Helper(compress.sizeOfCodebook);
		inputclusterMapping = clusterize(inputclusterMapping,inputVectorArray,
				codebook);
		
		
		System.out.println("Initialized codeword and cluster");
		//inputclusterMapping.printStats(codebook);
		
		
		
		if(compress.sizeOfCodebook >= 64){
			System.out.println("PLEASE WAIT FOR MORE THAN 1 MINUTE FOR THE DISPLAY of the compressed image, "
					+ "as For larger size of codebook, the program takes time");
		}
		//Train cluster	
		int cnt = 0;
		System.out.println("Generating Final Codewords");
		while(!codebook.isConverged()){
			
			codebook = inputclusterMapping.updateCodebook(codebook);
			
			inputclusterMapping.reset();
			inputclusterMapping = clusterize(inputclusterMapping,inputVectorArray,
					codebook);			
			cnt++;			
		}
		
		//long et = System.currentTimeMillis();
		//System.out.println("Generated for: " + (et - t)/1000 + " seconds");	
		System.out.println("Generated Codewords");
		
		//inputclusterMapping.printStats(codebook);
		
		//Make the output byte array		
		Vector[] outputVectorArry = createOutputVector(inputVectorArray,
				inputclusterMapping,codebook);
		byte[] outputByteArray = createOutputByteArray(outputVectorArry);
		System.out.println("Created output ByteArray");
		
		
		//Display the image
		System.out.println("Displaying Compressed Image");
		DisplayImage display = new DisplayImage();
		display.showImage(inputByteArray, outputByteArray,IMAGE_WIDTH, 
				IMAGE_HEIGHT, compress.isColor);
		//et = System.currentTimeMillis();
		System.out.println("Image Compressed Successfull");	
		//System.out.println("Image Compressed Successfully");
	}

	/**
	 * Reads the input vector(X,Y) and its respective codeword cluster index 
	 * and creates the output vector
	 * returns vector of same length as input
	 */
	private static Vector[] createOutputVector(Vector[] input, Helper helper, 
			CodeBook codebook) {
		Vector[] output = new Vector[input.length];
		
		for(int i = 0; i < input.length; i++){
			int index = helper.getCodewordIndex(input[i]);
			output[i] = codebook.getCodeword(index);			
		}
		return output;
	}

	/**
	 * Create clusters of input vectors by finding the nearest codeword 
	 * to the input using eucledean distance
	 * @param inputclusterMapping
	 * @param inputVectorArray
	 * @param codebook
	 * @return Helper object which has the updated mapping of cluster
	 */
	private static Helper clusterize(Helper inputclusterMapping, 
			Vector[] inputVectorArray, CodeBook codebook) {
		int len = inputVectorArray.length;
		for(int i = 0; i < len; i++){
			//find the nearest codeword index
			int nearestCodewordIndex =  findNearestCodeword(codebook,inputVectorArray[i]);
			inputclusterMapping.addToHelper(inputVectorArray[i], nearestCodewordIndex);
			
		}
		return inputclusterMapping;
	}
	
	/**
	 * @param codebook
	 * @param vector
	 * @return index of the nearest codeword to the vector
	 */
	private static int findNearestCodeword(CodeBook codebook, Vector vector) {
		int min = Integer.MAX_VALUE;
		int index = 0;
		Vector[] codewords = codebook.getCodewords();
		for(int i = 0; i < codebook.getSize(); i++){
			int ed = euclideanDistance(codewords[i],vector);
			if(ed < min){
				min = ed;
				index = i;				
			}			
		}
		return index;
	}

	/**
	 * 
	 * @param vector1
	 * @param vector2
	 * @return euclidean distance between vector1 and vector2
	 */
	private static int euclideanDistance(Vector vector1, Vector vector2) {
		int deltaY= (int)Math.pow(Math.abs(vector1.getY() - vector2.getY()),2);
		int deltaX = (int)Math.pow(Math.abs (vector1.getX() - vector2.getX()),2); 
		int result  = deltaY+ deltaX;
		//int result  = (int)Math.sqrt(deltaY+ deltaX);
	    //System.out.println(result);
	    return result;
	}
	


	/**
	 * Vector is formed by reading pair of consecutive bytes into X-coord and Y-coord
	 * @param input
	 * @return Vector array
	 */
	private static Vector[] readFile(byte[] input) {
		
		int len = input.length;
		int index =0;
		Vector[] vector = new Vector[len/2];
		for(int i = 0;i < len;i= i+2){			
			vector[index] = new Vector(unsigned(input[i]), unsigned(input[i+1]));
			index++;
		}		
		return vector;		
	}
	
	/**
	 * Forms 2 bytes for each vector
	 * @param input vector array
	 * @return byte array
	 */
	private static byte[] createOutputByteArray(Vector[] input) {
		int len = input.length * 2;
		byte[] output = new byte[len];
		int index = 0;
		for(Vector vector: input){
			output[index] = (byte)vector.getX();
			output[index + 1] = (byte)vector.getY();
			index+=2;
		}
		return output;
	}
	
	/* Converts the byte to int */
	private static int unsigned(byte x) {
	    return (int) (x & 0xFF);
	}

	/**
	 * Reads the file into byte
	 * @param FileName name
	 * @return byte array
	 */
	private static byte[] readFileIntoBytes(String name) {
		try {
			File file = new File(name);
			InputStream is;
			is = new FileInputStream(file);
			long len = file.length();
			byte[] bytes = new byte[(int)len];
			
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}
			
			is.close();
			return bytes;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		
		
	}

	
}
