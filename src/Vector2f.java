import java.lang.Math;

public class Vector2f {
	private float x;
	private float y;
		
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(final Tank.Dir dir){
		x = 0.f;
		y = 0.f;
		
		switch(dir){
		case Up:
			y = -1.f;
			break;
		case Right:
			x = 1.f;
			break;
		case Down:
			y = 1.f;
			break;
		case Left:
			x = -1.f;
			break;				
		}
	}
	
	public Vector2f(final Vector2f copy){
		x = copy.x;
		y = copy.y;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public float getNorm(){
		return (float)Math.sqrt((double)Math.abs(x*x)+Math.abs(y*y));
	}
	
	public Vector2f scale(float fScale){
		Vector2f vec = new Vector2f(x*fScale,y*fScale);
		return vec;
	}
	
	public Vector2f add(Vector2f vec){
		return new Vector2f(x+vec.x,y+vec.y);
	}
}
