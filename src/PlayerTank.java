import org.newdawn.slick.Input;

/**
 * Implémentation d'un tank jouable
 * @author isra17
 *
 */
public class PlayerTank extends Tank {

	@Override
	public Dir pickDirection() {
		Input input = BattleTank.instance().getInput();
		Dir dir;
		if(input.isKeyDown(Input.KEY_A))
			dir = Dir.Left;
		else if(input.isKeyDown(Input.KEY_W))
			dir = Dir.Up;
		else if(input.isKeyDown(Input.KEY_D))
			dir = Dir.Right;
		else if(input.isKeyDown(Input.KEY_S))
			dir = Dir.Down;
		else
			dir = Dir.None;
		return dir;
	}
	
}
