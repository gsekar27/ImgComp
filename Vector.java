
public class Vector {	
	private int x;	
	private int y;	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public Vector add(Vector v){
		this.x = this.x + v.x;
		this.y = this.y + v.y;
		return this;		
	}
	
	public void reset(){
		this.x = 0;
		this.y = 0;
	}
	
	public Vector(int pixel1, int pixel2){
		this.x = pixel1;
		this.y = pixel2;
	}

	public int differnce(Vector v) {
		int delx = Math.abs(this.x - v.x); 
		int dely = Math.abs(this.y - v.y);
		return delx + dely;		
	}	
	public String toString(){
		return "Vector: x =" + this.x + ", y=" + this.y;
	}

}
