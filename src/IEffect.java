import org.newdawn.slick.Graphics;


public interface IEffect {
	public void update(float elapsedTime);
	public void render(Graphics g);
	
	public void destroy();
}
