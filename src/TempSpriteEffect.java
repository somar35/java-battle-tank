import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;


public class TempSpriteEffect implements IEffect {
	private Image m_sprite;
	private Vector2f m_coord;
	private float m_timeToLive;
	
	public TempSpriteEffect(Image sprite, Vector2f coord, float timeToLive){
		m_sprite = sprite;
		m_coord = new Vector2f(coord);
		m_timeToLive = timeToLive;
	}
	
	@Override
	public void update(float elapsedTime) {
		m_timeToLive -= elapsedTime;
		if(m_timeToLive <= 0){
			destroy();
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(m_sprite, m_coord.getX()*50, m_coord.getY()*50);
	}

	@Override
	public void destroy() {
		BattleTank.instance().getEffectManager().remove(this);
	}

}
