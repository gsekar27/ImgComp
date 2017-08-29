import java.util.HashMap;

public class Helper {
	private Cluster[] partition;
	private HashMap<Vector, Integer> inputVectorCodeWordMappings;
	public Helper(int codeBookSize){
		this.partition = new Cluster[codeBookSize];
		for(int i = 0; i < codeBookSize; i++){
			partition[i] = new Cluster();
		}
		this.inputVectorCodeWordMappings = new
				HashMap<Vector, Integer>();
	}
	
	public void addToHelper(Vector vector, int index){
		this.partition[index].addToCluster(vector);
		this.inputVectorCodeWordMappings.put(vector, index);
	}
	
	public Cluster getcluster(int index){
		return this.partition[index];
	}
	
	public HashMap<Vector, Integer> getInputVectorMappings() {		
		return inputVectorCodeWordMappings;
	}
	
	public void reset(){
		for(int i = 0; i < partition.length; i++){
			partition[i].clearCluster();
		}
		inputVectorCodeWordMappings.clear();
	}

	
	
	public CodeBook updateCodebook(CodeBook codebook){
		boolean flag = true;
		for(int i = 0; i < codebook.getSize(); i++){
			Vector codeword = codebook.getCodeword(i);			
			if(this.getcluster(i).getSize() != 0){							
				Vector avgOfCluster = this.getcluster(i).meanCluster();
				double avgDist = codeword.differnce(avgOfCluster) ;
				if(avgDist != codebook.getAvgDistortion(i)){
					codebook.setAvgDistortion(i,avgDist);
					codebook.setCodeWord(i, avgOfCluster);
						flag = false;
				}					
			} else{
				if((codeword.getX() + 4) < 255 && (codeword.getY()+4) < 255){
					codebook.setCodeWord(i, new Vector(codeword.getX() + 4,codeword.getY()+4));
				}				
			}
		}
		codebook.setCodewordFlag(flag);
		return codebook;
	}
	
	

	public int getCodewordIndex(Vector vector) {
		return inputVectorCodeWordMappings.get(vector);
	}
	
	public void printStats(CodeBook codebook){
		System.out.println("Codewords are below");
		for(int i =0; i < partition.length; i++){
			System.out.println("Codeword " + i +": "+ codebook.getCodeword(i).toString() +
					 ", clusterSize: " + partition[i].getSize()); 
		}
	}

}
