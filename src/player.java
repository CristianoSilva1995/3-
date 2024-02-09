
public class Player {
	private int life;
	private String shape;
	private String colour;
	
	public Player() {
		life = 2;
		shape = "E";
		colour = "E";
	}
	
	public void setShape(String shape) {
		this.shape = shape;
	}
	
	public void setColour(String colour) {
		this.colour = colour;
	}
	
	public String getShape() {
		return shape;
	}
	
	public String getColour() {
		return colour;
	}
	
	public void setLife(int life) {
		this.life = life;
	}
	
	public void setLife() {
		life--;
	}
	public int getLife() {
		return life;
	}
}
