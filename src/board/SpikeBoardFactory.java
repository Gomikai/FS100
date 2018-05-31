package board;

public class SpikeBoardFactory extends BoardFactory {
	private static SpikeBoardFactory instance;
	
	public static BoardFactory getInstance(){
		if(instance==null)instance=new SpikeBoardFactory();
		return instance;
	}
	
	public Board produce(){
		return new SpikeBoard();
	}
}
