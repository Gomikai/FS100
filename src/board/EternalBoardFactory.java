package board;

public class EternalBoardFactory extends BoardFactory {
	private static EternalBoardFactory instance;
	
	public static BoardFactory getInstance(){
		if(instance==null)instance=new EternalBoardFactory();
		return instance;
	}
	
	public Board produce(){
		return new EternalBoard();
	}
}
