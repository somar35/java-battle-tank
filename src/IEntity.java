import org.newdawn.slick.Graphics;

public interface IEntity {
	public Vector2f getCoord();
	public void setCoord(Vector2f coord);
	public Rectangle getBox();
	
	void think(float elapsedTime);
	public void render(Graphics g);
	
	public void onBulletHit();
}
