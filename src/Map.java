import org.newdawn.slick.Graphics;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Map {
	public static final int width = 10;
	public static final int height = 10;  
	
	public static final int test_matrice[][] = 
		{	{1,1,1,1,1,1,1,1,1,1},
			{1,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,1},
			{1,0,0,1,0,1,1,0,0,1},
			{1,0,0,1,0,0,1,0,0,1},
			{1,0,0,1,0,0,1,0,0,1},
			{1,0,0,1,0,1,1,0,0,1},
			{1,0,0,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,1},
			{1,1,1,1,1,1,1,1,1,1}
		};
	
	public Map(){
		try{
			Image brickSprite = new Image("res/brick.png");
			Image nothingSprite = new Image(50,50);
			
			m_tiles = new Tile[width][height];
			for(int x = 0; x < Map.width; x++){
				for(int y = 0; y < Map.height; y++){
					switch(Map.test_matrice[x][y]){
					case 1:
						m_tiles[x][y] = new Tile(1,brickSprite);
						break;
					case 0:					
					default:
						
						m_tiles[x][y] = new Tile(0,nothingSprite);
					}
				}				
			}
		}catch(SlickException e){
			e.printStackTrace();
		}
	}
	
	Tile getTile(int x, int y){
		return m_tiles[x][y];
	}
	
	public void load(){
		
	}
	
	public void render(Graphics g){
		for(int x = 0; x < Map.width; x++){
			for(int y = 0; y < Map.height; y++){
				m_tiles[x][y].render(g, x, y);
			}
		}
	}
	

	private Tile[][] m_tiles;

	public boolean isMovableInTile(final int x, final int y, Rectangle box){
		return !m_tiles[x][y].intersect(x, y, box);
	}
	
	public boolean isMovable(final Rectangle box) {
		Rectangle gameBound = new Rectangle(0,0,10,10);
		if(!gameBound.contains(box)){
			return false;
		}
		
		return isMovableInTile((int)box.left(), (int)box.top(), box) &&
				isMovableInTile((int)box.left(), (int)box.bottom(), box) &&
				isMovableInTile((int)box.right(), (int)box.top(), box) &&
				isMovableInTile((int)box.right(), (int)box.bottom(), box);
	}

	public Vector2f isMovable(final Vector2f m_coord, final Vector2f move) {
		Vector2f testPoint;
		//Test chaque tranche de 0.25 du vecteur de déplacement
		testPoint = m_coord.add(move);
		Tile testTile = m_tiles[(int)testPoint.getX()][(int)testPoint.getY()];
		if(testTile.intersect(testPoint.getX(),testPoint.getY())){
			testPoint = new Vector2f(0.f,0.f);
		}
		
		return testPoint;
	}

	public boolean bulletHit(Vector2f m_coord, Vector2f move) {
		Vector2f testPoint = m_coord.add(move);
		boolean isHit = false;
		
		Rectangle gameBound = new Rectangle(0,0,10,10);
		if(!gameBound.intersect(testPoint)){
			return true;
		}
		
		if(move.getX()!=0){			
			int y1 = (int)(testPoint.getY()-0.1f);
			int y2 = (int)(testPoint.getY()+0.1f);
			int x = (int)testPoint.getX();
			
			isHit |= getTile(x,y1).intersect(Util.decimalPart(testPoint.getX()),Util.decimalPart(testPoint.getY()-0.1f));			
			isHit |= getTile(x,y2).intersect(Util.decimalPart(testPoint.getX()),Util.decimalPart(testPoint.getY()+0.1f));
			
			if(isHit){
				int yExp1 = (int)(testPoint.getY()-0.37f);
				int yExp2 = (int)(testPoint.getY()+0.37f);
				
				getTile(x,yExp1).verBulletHit(Util.decimalPart(testPoint.getX()), Util.decimalPart(testPoint.getY()-0.37f));
				if(yExp1 != yExp2){
					getTile(x,yExp2).verBulletHit(Util.decimalPart(testPoint.getX()), Util.decimalPart(testPoint.getY()-0.37f)-1);
				}
			}
		}
		else{
			int x1 = (int)(testPoint.getX()-0.1f);
			int x2 = (int)(testPoint.getX()+0.1f);
			int y = (int)testPoint.getY();
			
			isHit |= getTile(x1,y).intersect(Util.decimalPart(testPoint.getX()-0.1f),Util.decimalPart(testPoint.getY()));			
			isHit |= getTile(x2,y).intersect(Util.decimalPart(testPoint.getX()+0.1f),Util.decimalPart(testPoint.getY()));
			
			if(isHit){
				int xExp1 = (int)(testPoint.getX()-0.37f);
				int xExp2 = (int)(testPoint.getX()+0.37f);
				
				getTile(xExp1,y).horBulletHit(Util.decimalPart(testPoint.getX()-0.37f), Util.decimalPart(testPoint.getY()));
				if(xExp1 != xExp2){
					getTile(xExp2,y).horBulletHit(Util.decimalPart(testPoint.getX()-0.37f)-1, Util.decimalPart(testPoint.getY()));
				}
			}
		}
		return isHit;
	}
}
