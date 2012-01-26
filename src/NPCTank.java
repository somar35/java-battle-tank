import java.util.Random;


public class NPCTank extends Tank {

	float m_nextMove;
	float m_nextShoot;
	float m_elapsedTime;
	Dir m_curDir;
	Random m_randomizer;
	
	
	public NPCTank() {
		super();
		m_nextMove = 0;
		m_nextShoot = 0;
		m_curDir = Dir.Left;
		m_randomizer = new Random();
	}

	@Override
	public Dir pickDirection() {
		m_nextMove -= m_elapsedTime;
		m_elapsedTime = 0.f;
		if(m_nextMove < 0)
			m_curDir = getRandomDir();
		return m_curDir;
	}

	@Override
	public void think(float elapsedTime){
		m_elapsedTime += elapsedTime;
		m_nextShoot -= elapsedTime;
		if(m_nextShoot < 0)
		{
			shoot();
			m_nextShoot = 0.2f;
		}
		
		super.think(elapsedTime);
	}
	
	private Dir getRandomDir(){
		int numDir = m_randomizer.nextInt(5);
		m_nextMove = m_randomizer.nextFloat()*5.f+1.f;
		Dir dir = Dir.None;
		switch(numDir){
		case 0:
			dir = Dir.Left;
			break;
		case 1:
			dir = Dir.Up;
			break;
		case 2: 
			dir = Dir.Right;
			break;
		case 3: 
			dir = Dir.Down;
			break;
		case 4:
			dir = Dir.None;
		}
			
		return dir;
	}
}
