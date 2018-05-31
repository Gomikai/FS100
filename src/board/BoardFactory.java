package board;

public class BoardFactory {
	private static BoardFactory instance;
	
	protected BoardFactory(){
		
	}
	
	public static BoardFactory getInstance(){
		if(instance==null)instance=new BoardFactory();
		return instance;
	}
	
	public Board produce(){
		return new Board();
	}
}
