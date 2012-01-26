import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Graphics;


public class EffectManager {
	ArrayList<IEffect> m_effects;
	ArrayList<IEffect> m_effectsToRemove;
	boolean m_isLock;
	
	public EffectManager(){
		m_effects = new ArrayList<IEffect>();
		m_effectsToRemove = new ArrayList<IEffect>();
		m_isLock = false;
	}
	
	public void updateEffects(float elapsedTime){
		lock();
		Iterator<IEffect> iterator = m_effects.iterator();
		while ( iterator.hasNext() )
			iterator.next().update(elapsedTime);
		unlock();
		clean();
	}
	
	public void renderEffects(Graphics g){
		Iterator<IEffect> iterator = m_effects.iterator();
		while ( iterator.hasNext() )
			iterator.next().render(g);
	}
	
	public void add(IEffect effect){
		m_effects.add(effect);
	}
	
	public void remove(IEffect effect) {	
		if(m_isLock)
			m_effectsToRemove.add(effect);
		else
			m_effects.remove(effect);
	}
	
	public void clean(){
		m_effects.removeAll(m_effectsToRemove);
		m_effectsToRemove.clear();
	}
	
	public void lock(){
		m_isLock = true;
	}
	
	public void unlock(){
		m_isLock = false;
	}
}
