
public class Rectangle {
	Vector2f m_coord;
	Vector2f m_size;
	
	public Rectangle(){
		this(0.f,0.f,0.f,0.f);
	}
	
	public Rectangle(float x, float y, float width, float height){
		this(new Vector2f(x,y),new Vector2f(width,height));
	}
	
	public Rectangle(final Vector2f coord, final Vector2f size){		
		m_coord = new Vector2f(coord);
		m_size = new Vector2f(size);
		
		if(size.getX() < 0){
			m_coord.setX(m_coord.getX()+m_size.getX());
			m_size.setX(-m_size.getX());
		}
		
		if(size.getY() < 0){
			m_coord.setY(m_coord.getY()+m_size.getY());
			m_size.setY(-m_size.getY());
		}
	}
	
	public Vector2f getCoord(){
		return m_coord;
	}
	
	public Vector2f getSize(){
		return m_size;
	}
	
	public float left(){
		return m_coord.getX();
	}
	
	public float top(){
		return m_coord.getY();
	}
	
	public float right(){
		return m_coord.getX() + m_size.getX();
	}
	
	public float bottom(){
		return m_coord.getY() + m_size.getY();
	}
	
	public boolean intersect(Vector2f point){
		return 	point.getX() > left() && point.getX() < right() && 
				point.getY() > top() && point.getY() < bottom();
	}
	
	public boolean contains(Rectangle rec){
		if(left() <= rec.left() && right() >= rec.right() && top() <= rec.top() && bottom() >= rec.bottom())
			return true;
		return false;
	}

	public void setWidth(float width) {
		m_size.setX(width);
	}
	
	public void setHeight(float height) {
		m_size.setY(height);
	}

	public boolean intersect(Rectangle box) {
		return ! ( box.left() >= right()
		        || box.right() <= left()
		        || box.top() >= bottom()
		        || box.bottom() <= top()
		        );		
	}
}
