import java.util.Stack;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Classe représentant un char d'assaut et ses fonctionnalité
 * @author isra17
 */
public abstract class Tank implements IEntity {
	/**
	 * Enumération des direction
	 * @author isra17
	 */
	enum Dir{
		Left,Up,Down,Right,None
	}
	
	//Vitesse du Tank (tile/sec)
	private float m_speed;

	//Coordonnée du tank
	private Vector2f m_coord;
	
	//Orientation du tank
	private Dir m_dir;
	
	//Direction du mouvement du tank
	private Dir m_curState;
	
	//Si le tank est en mouvement
	private boolean m_isBusy;
	
	//Sprite du Tank
	private Image m_sprite;
	
	/**
	 * Méthode abstraite permettant à la surclasse de définir le comportement du tank
	 * @return La direction du Tank.
	 */
	public abstract Dir pickDirection();
		
	public Tank(){
		try{
			m_sprite = new Image("res/tank.png");
			m_sprite.setCenterOfRotation((float)m_sprite.getWidth()/2, (float)m_sprite.getHeight()/2);
		}catch(SlickException e){
			e.printStackTrace();
		}
		
		m_speed = 1;
		m_coord = new Vector2f(1.f,1.f);
		m_curState = Dir.None;
		m_isBusy = false;
		setDir(Dir.Up);
	}
	
	/**
	 * Trouve la nouvelle direction du tank avec pickDirection() et valide le mouvement.
	 * @return La direction valide.
	 */
	private Dir pickValideDirection(){
		Dir dir = pickDirection();
		if(dir!=Dir.None)
		{
			//Met à jour l'orientation du tank même si le mouvement n'est pas possible.
			setDir(dir);
			
			//Initialise la 'boite de détection'
			Vector2f boxCoord = m_coord;
			if(dir == Dir.Down)
				boxCoord = new Vector2f(m_coord.add(new Vector2f(0.f,1.f)));
			else if(dir == Dir.Right)
				boxCoord = new Vector2f(m_coord.add(new Vector2f(1.f,0.f)));			
			Rectangle testBox = new Rectangle(boxCoord,new Vector2f(dir).scale(0.20f));			
			if(testBox.getSize().getX() == 0)testBox.setWidth(0.9f);
			else if(testBox.getSize().getY() == 0)testBox.setHeight(0.9f);
			
			//Test la collision avec la map
			if(!BattleTank.instance().getMap().isMovable(testBox)){
				//si une collision à lieu on ne bouge pas
				dir = Dir.None;
			}
			
			//Test la collision avec les autres entity
			Stack<IEntity> oEntity = BattleTank.instance().getEntityManager().intersectEntities(testBox, this);
			if(!oEntity.empty()){
				dir = Dir.None;
			}
		}
		return dir;
	}
	
	/**
	 * Met à jour le Tank
	 * @param elapsedTime Temps écoulé depuis la dernière mise-à-jour.
	 */
	public void think(float elapsedTime){
		//Si le tank n'est pas en mouvement on prend le prochain mouvement.
		if(!m_isBusy){
			m_curState = pickValideDirection();
			if(m_curState!=Dir.None)
				setDir(m_curState);
		}
		
		if(m_curState != Dir.None){		
			//process movement
			Vector2f moveVec = new Vector2f(m_curState);
			
			//move by 0.25 gap
			Vector2f vecMoved = new Vector2f(moveVec);
			vecMoved.setX(vecMoved.getX()*m_coord.getX());
			vecMoved.setY(vecMoved.getY()*m_coord.getY());
			float distanceTraveled = vecMoved.getNorm();

			//calcule la distance à parcourcir afin de se rendre au prochain 'waypoint'
			float distanceToTravel;			
			float moveAngle = dir2angle(m_curState);
			
			if(moveAngle > 0 && moveAngle <= 180){
				distanceToTravel = 0.25f - (float)(distanceTraveled%0.25);
			}else{
				distanceToTravel = (float)(distanceTraveled%0.25);
				if(distanceToTravel == 0)
					distanceToTravel = 0.25f;
			}
			
			//calcule la distance à parcourcir maximal afin de se rendre au prochain 'waypoint'
			float maxMoveLen = elapsedTime*m_speed;
			float moveLen = maxMoveLen > distanceToTravel ? distanceToTravel : maxMoveLen;
			
			//Créer le vecteur de mouvement
			moveVec = moveVec.scale(moveLen);
			
			if(distanceToTravel > 0.25f)
				System.out.println("OH SHIT");
			
			m_coord = m_coord.add(moveVec);
			
			if(moveLen == distanceToTravel)
				m_isBusy = false;
			else
				m_isBusy = true;
						
			//Si on a atteint un waypoint et qu'il reste encore du mouvement
			if(moveLen < maxMoveLen){
				think((maxMoveLen - moveLen)/m_speed);
			}
		}
	}
	
	/**
	 * Tir un obus
	 */
	public void shoot(){
		Bullet newBullet = new Bullet(this); 
		BattleTank.instance().getEntityManager().add(newBullet);
		Vector2f bulletCoord = new Vector2f(0.f,0.f);
		bulletCoord = m_coord.add(new Vector2f(0.5f,0.5f)).add(new Vector2f(m_dir).scale(0.5f));
		newBullet.setVelocity(new Vector2f(m_dir).scale(5.f));
		newBullet.setCoord(bulletCoord);
		newBullet.think(0.0001f);
	}
	
	/**
	 * Rend le tank sur un objet graphics
	 * @param g Objet graphique sur lequel le tank est rendu.
	 */
	@Override
	public void render(Graphics g){
		g.drawImage(m_sprite, m_coord.getX()*50, m_coord.getY()*50);
	}
	
	public void onBulletHit(){
		destroy();
	}
	
	public void destroy(){
		BattleTank.instance().getEntityManager().remove(this);
	}
	
	/**
	 * Transforme un direction en angle (degree)
	 */
	public static float dir2angle(Dir dir){
		float angle = 0.f;
		switch(dir){
		case Up:
			angle = 0.f;
			break;
		case Right:
			angle = 90;
			break;
		case Down:
			angle = 180;
			break;
		case Left:
			angle = 270;
			break;
		}
		return angle;
	}
	
	//setter/getter
	
	public void setDir(Dir dir){
		m_dir = dir;
		m_sprite.setRotation(dir2angle(dir));
	}
	
	public Dir getDir(){
		return m_dir;
	}

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
		return new Rectangle(m_coord,new Vector2f(1.f,1.f));
	}
	
	public float getSpeed() {
		return m_speed;
	}

	public void setSpeed(float speed) {
		this.m_speed = speed;
	}
}
