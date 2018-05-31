package board;

public class SparkBoardFactory extends BoardFactory {
	private static SparkBoardFactory instance;
	
	public static BoardFactory getInstance(){
		if(instance==null)instance=new SparkBoardFactory();
		return instance;
	}
	
	public Board produce(){
		return new SparkBoard();
	}
}
