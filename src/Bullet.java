import java.util.Stack;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Implémentation des obus
 * @author isra17
 *
 */
public class Bullet implements IEntity {
	//Coordonnée de l'obus
	private Vector2f m_coord;
	
	//Vitesse de l'obus
	private Vector2f m_velocity;
	
	//Sprite de l'obus
	private Image m_sprite;
	
	private Tank m_shooter;

	public Bullet(Tank shooter){
		try{
		m_sprite = new Image("res/bullet.png");
		}catch(SlickException e){
			e.printStackTrace();
		}
		
		m_shooter = shooter;
		m_coord = new Vector2f(0.f,0.f);
		m_velocity = new Vector2f(0.f,0.f);
	}
	
	/**
	 * Met à jour l'obus
	 * @param elapsedTime Temps écoulé depuis la derniere mise-à-jour
	 */
	public void think(float elapsedTime){		
		boolean isHit = false;
		do{
			float minElapsedTime = elapsedTime < 0.25f?elapsedTime : 0.25f;
			if(BattleTank.instance().getMap().bulletHit(m_coord,m_velocity.scale(minElapsedTime))){
				isHit = true;
				destroy();
			}
			
			m_coord = m_coord.add(m_velocity.scale(elapsedTime));
			
			//Test la collision avec les autres entity
			Stack<IEntity> oEntity = BattleTank.instance().getEntityManager().intersectEntities(getBox(), this);
			while(!oEntity.empty()){
				IEntity entity = oEntity.pop();
				if(entity != m_shooter){
					entity.onBulletHit();
					isHit = true;
					destroy();
				}
			}
			
			elapsedTime -= minElapsedTime;
		}while(elapsedTime > 0 && !isHit);
	}
	
	@Override
	/**
	 * Rend l'obus sur un objet graphics
	 * @param g Object graphique sur lequel l'obus est rendu
	 */
	public void render(Graphics g) {
		g.drawImage(m_sprite, m_coord.getX()*50-4, m_coord.getY()*50-4);
	}
	
	public void onBulletHit(){
		destroy();
	}
	
	/**
	 * Détruit l'obus
	 */
	public void destroy(){
		try{
			Image explosionSprite = new Image("res/explosion.png");
			TempSpriteEffect explosionEffect = new TempSpriteEffect(explosionSprite, m_coord.add(new Vector2f(-0.08f,-0.08f)),0.1f);
			//TempSpriteEffect explosionEffect = new TempSpriteEffect(explosionSprite, m_coord.add(new Vector2f(-4f,-8f)),1.f);
			BattleTank.instance().getEffectManager().add(explosionEffect);
		}catch(SlickException e){
			e.printStackTrace();
		}finally{
			BattleTank.instance().getEntityManager().remove(this);
		}
	}
	
	//getter/setter
	@Override
	public Vector2f getCoord() {		
		return m_coord;
	}

	@Override
	public void setCoord(Vector2f coord) {
		m_coord = coord;
	}
	
	@Override
	public Rectangle getBox(){
		return new Rectangle(m_coord.add(new Vector2f(-0.1f,-0.1f)),new Vector2f(0.2f,0.2f));
	}

	public void setVelocity(Vector2f velocity) {
		m_velocity = velocity;		
	}
	
}
