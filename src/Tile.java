import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;


public class Tile {
	private int m_type;
	private Image m_sprite;
	private int[][] m_matrice;
	
	public Tile(int type, Image sprite){
		m_type = type;
		m_sprite = sprite;
		m_matrice = new int[4][4];
		for(int x = 0; x < 4; x++){
			for(int y = 0; y < 4; y++){
				m_matrice[x][y] = m_type;
			}				
		}
	}
	
	public int getIDType(){
		return m_type;
	}
	
	public Image getSprite(){
		return m_sprite;
	}
	
	public boolean intersect(int x, int y, Rectangle box){
		boolean isIntersect = false;
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				if(m_matrice[i][j] == 1){
					isIntersect |= box.intersect(new Vector2f((float)(x)+i*0.25f + 0.125f,(float)(y)+j*.25f + 0.125f));
				}
			}				
		}
		
		return isIntersect;		
	}
	
	public boolean intersect(float x, float y){
		if(m_matrice[(int)(Util.decimalPart(x)/0.25f)][(int)(Util.decimalPart(y)/0.25f)] == 1)
			return true;
		return false;				
	}
	
	public void render(Graphics g, int x, int y){
		for(int i=0;i<4;i++)
			for(int j=0;j<4;j++){
				if(m_matrice[i][j] == 1){
					Vector2f destPos = new Vector2f(x*50+(i*12.5f), y*50+(j*12.5f)); 
					Vector2f destPos2 = destPos.add(new Vector2f(12.5f,12.5f));
					Vector2f srcPos = new Vector2f((i*12.5f),(j*12.5f));
					Vector2f srcPos2 = srcPos.add(new Vector2f(12.5f,12.5f));
					g.drawImage(m_sprite, destPos.getX(),destPos.getY(),destPos2.getX(),destPos2.getY(),
											srcPos.getX(),srcPos.getY(),srcPos2.getX(),srcPos2.getY());
				}
			}
	}

	public void verBulletHit(float x, float y) {
		int iX = (int)(x/0.25f);
		int minY = y > 0?(int)(y/0.25f): 0;
		int maxY = y+1.f > 1.f?4: (int)((y+1)/0.25f);
		
		for(int i=minY; i<maxY; i++){
				m_matrice[iX][i]=0;
		}
	}
	
	public void horBulletHit(float x, float y) {
		int iY = (int)(y/0.25f);
		int minX = x > 0?(int)(x/0.25f): 0;
		int maxX = x+1.f > 1.f?4: (int)((x+1)/0.25f);
		
		for(int i=minX; i<maxX; i++){
				m_matrice[i][iY]=0;
		}
	}
}
