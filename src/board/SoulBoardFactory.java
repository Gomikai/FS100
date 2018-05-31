package board;

public class SoulBoardFactory extends BoardFactory {
	private static SoulBoardFactory instance;
	
	public static BoardFactory getInstance(){
		if(instance==null)instance=new SoulBoardFactory();
		return instance;
	}
	
	public Board produce(){
		return new SoulBoard();
	}
}
