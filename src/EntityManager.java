import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.newdawn.slick.Graphics;


public class EntityManager {
	ArrayList<IEntity> m_entities;
	ArrayList<IEntity> m_entitiesToRemove;
	ArrayList<IEntity> m_entitiesToAdd;
	boolean m_isLock;
	
	public EntityManager(){
		m_entities = new ArrayList<IEntity>();
		m_entitiesToRemove = new ArrayList<IEntity>();
		m_entitiesToAdd = new ArrayList<IEntity>();
		m_isLock = false;
	}
	
	public void updateEntities(float elapsedTime){
		lock();
		Iterator<IEntity> iterator = m_entities.iterator();
		while ( iterator.hasNext() )
			iterator.next().think(elapsedTime);
		unlock();
		clean();
	}
	
	public void renderEntities(Graphics g){
		Iterator<IEntity> iterator = m_entities.iterator();
		while ( iterator.hasNext() )
			iterator.next().render(g);
	}
	
	public ArrayList<IEntity> getBulletList(){
		return m_entities;
	}
	
	public Stack<IEntity> intersectEntities(Rectangle box, IEntity excludeEntity){
		Stack<IEntity> oEntity = new Stack<IEntity>();
		
		Iterator<IEntity> iterator = m_entities.iterator();
		while ( iterator.hasNext() )
		{
			IEntity entity = iterator.next();
			if(entity != excludeEntity && entity.getBox().intersect(box))
				oEntity.add(entity);
		}
		
		return oEntity;
	}
	
	public void add(IEntity entity){
		if(m_isLock)
			m_entitiesToAdd.add(entity);
		else
			m_entities.add(entity);
	}
	
	public void remove(IEntity entity) {	
		if(m_isLock)
			m_entitiesToRemove.add(entity);
		else
			m_entities.remove(entity);
	}
	
	public void clean(){
		m_entities.removeAll(m_entitiesToRemove);
		m_entitiesToRemove.clear();
		
		m_entities.addAll(m_entitiesToAdd);
		m_entitiesToAdd.clear();
	}
	
	public void lock(){
		m_isLock = true;
	}
	
	public void unlock(){
		m_isLock = false;
	}
}
