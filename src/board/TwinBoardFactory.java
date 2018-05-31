package board;

public class TwinBoardFactory extends BoardFactory {
	private static TwinBoardFactory instance;
	
	public static BoardFactory getInstance(){
		if(instance==null)instance=new TwinBoardFactory();
		return instance;
	}
	
	public Board produce(){
		return new TwinBoard();
	}
}
