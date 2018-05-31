package board;

public class BounceBoardFactory extends BoardFactory {
	private static BounceBoardFactory instance;
	
	public static BoardFactory getInstance(){
		if(instance==null)instance=new BounceBoardFactory();
		return instance;
	}
	
	public Board produce(){
		return new BounceBoard();
	}
}
