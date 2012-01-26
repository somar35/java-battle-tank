
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;


public class BattleTank extends BasicGame{

	private Map m_map;
	private PlayerTank m_playerTank;
	
	private EffectManager m_effects;
	private EntityManager m_entities;
	
	AppGameContainer m_container;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(BattleTank.instance()); 
			BattleTank.instance().setContainer(app);
			app.start(); 
		} catch (SlickException e) { 
			e.printStackTrace(); 
		}
	}
	
	private BattleTank(){
		super("BattleTank - Devel");
	}
	
	@Override 
	public void init(GameContainer container) throws SlickException {
		m_playerTank = new PlayerTank();
		NPCTank npcTank = new NPCTank();
		npcTank.setCoord(new Vector2f(8.f,1.f));
		
		m_entities = new EntityManager();
		m_effects = new EffectManager();
		m_map = new Map();
		
		m_entities.add(m_playerTank);
		m_entities.add(npcTank);
	} 
	
	@Override 
	public void update(GameContainer container, int delta) throws SlickException {
		float elapsedTime = (float)delta/1000;
				
		m_entities.updateEntities(elapsedTime);
		m_effects.updateEffects(elapsedTime);
	} 
	
	@Override 
	public void render(GameContainer container, Graphics g) throws SlickException {
		m_map.render(g);
		
		g.drawString("X : " + m_playerTank.getCoord().getX(), 0, 50);
		g.drawString("Y : " + m_playerTank.getCoord().getY(), 0, 70);


		m_entities.renderEntities(g);
		m_effects.renderEffects(g);
	} 	
	
	public Map getMap(){
		return m_map;
	}
	
	public void setContainer(AppGameContainer container){
		m_container = container;
	}
	
	public Input getInput(){
		return m_container.getInput();
	}
	
	public void keyPressed(int key, char c){
		if(key == Input.KEY_SPACE){
			m_playerTank.shoot();
		}
	}
	
	public EffectManager getEffectManager(){
		return m_effects;
	}
	
	public EntityManager getEntityManager(){
		return m_entities;
	}
	
	///Singleton
	
	/**
	 * Singleton implementation
	 * @return Singleton instance
	 */
	public static BattleTank instance(){
		if(m_instance == null)
			m_instance = new BattleTank();
		return m_instance;		
	}
	
	/**
	 * Singleton static instance 
	 */
	private static BattleTank m_instance;

}
