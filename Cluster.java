

public class Cluster {
	
	private int size;
	
	private Vector sumClusterVector ;
	
	public Cluster(){
		this.size = 0;
		this.sumClusterVector  = new Vector(0,0);
	}
	
	public void addToCluster(Vector vector){
		this.size++;
		this.sumClusterVector = sumClusterVector.add(vector);
		
	}
	
	public boolean isEmpty(){
		return this.size == 0;
	}
	
	public void clearCluster(){
		this.size = 0;
		this.sumClusterVector.reset();
				
	}
	public int getSize() {
		return size;
	}

	
	public void setSize(int size) {
		this.size = size;
	}

	public Vector getSumClusterVector() {
		return sumClusterVector;
	}

	
	public Vector meanCluster(){
		int cnt = this.size;
		
		return new Vector(Math.round(this.sumClusterVector.getX()/cnt), 
				Math.round(this.sumClusterVector.getY()/cnt));
		
	}
	
	

}
