
public class CodeBook {
	
	private int size;
	private Vector[] codewords;
	private boolean flags;
	private double[] avgDistortion;
	
	public CodeBook(int n){
		this.size = n;
		this.codewords = initializeCodeBook(n);
		//flags = new boolean[n];
		flags = false;
		avgDistortion = new double[n];
	}

	public int getSize() {
		return size;
	}

	public Vector[] getCodewords() {
		return codewords;
	}
	
	public Vector getCodeword(int index){
		return this.codewords[index];
	}
	
	public double getAvgDistortion(int index){
		return this.avgDistortion[index];
	}
	
	public void setAvgDistortion(int index,double val){
		 this.avgDistortion[index] = val;
	}
	
	
	/**
	 * Divides the color spaces into n equally distributed vectors
	 * @param n: size of the codebook - number of codewords in codebook
	 * @return codewords: array of vectors
	 */
	public Vector[] initializeCodeBook(double n) {
		int numColorRange = 256;
		int numOfBlocksInRow;
		int numOfBlocksInCol;
		int pow = (int) (Math.log(n)/Math.log(2));
		
		if(pow % 2 != 0){//Odd power of 2
			int lastPerfectSquare = (int) Math.pow(2, pow-1);
			numOfBlocksInRow = (int)Math.sqrt(lastPerfectSquare);
			numOfBlocksInCol = numOfBlocksInRow * 2;
		}else{//even power of 2
			numOfBlocksInRow = (int) (Math.sqrt(n));
			numOfBlocksInCol = (int) (Math.sqrt(n));
		}
		int horStepSize = numColorRange/numOfBlocksInCol;
		int verStepSize = numColorRange/numOfBlocksInRow;
		int initialX = (horStepSize/2)-1;
		int initialY = (verStepSize/2)-1;
		
		Vector[] vector = new Vector[(int)n];
		int index = 0;
		
		for(int h = 0; h < numOfBlocksInRow; h++){			
			for(int w = 0; w < numOfBlocksInCol; w++){				
				vector[index] = new Vector(initialY,initialX);
				initialX += horStepSize;
				index++;
			}
			initialX = (horStepSize/2)-1;
			initialY += verStepSize;
		}
		
		return vector;
	}
	
	
	/** returns true if codebook is converged */
	public boolean isConverged(){
		
		return flags;
	}

	public void setCodeWord(int ind, Vector avgOfCluster) {
		this.codewords[ind] = avgOfCluster;
		
	}

	public void setCodewordFlag(boolean flag) {
		this.flags = flag;
		
	}
	
	
	public void printCodeword(){
		System.out.println("Codewords are below");
		for(int i =0; i < this.size; i++){
			System.out.println(i + " ->" + codewords[i].toString()   + " diff" + this.avgDistortion[i] );
		}
		
	}

}
