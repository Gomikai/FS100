package musicPlayer;

import java.io.File;

import javax.sound.sampled.*;

public class MusicPlayer{
	Clip clip;

	public MusicPlayer(String str){
	  try{
		  File wavFile = new File(str);
		  AudioInputStream ais = AudioSystem.getAudioInputStream(wavFile);
		  clip = AudioSystem.getClip();
		  clip.open(ais);
		  clip.setFramePosition(0);
		  clip.setLoopPoints(0, -1);
	  }
	  catch(Exception e){
		  
	  }

	}
	
	public void play(){
		clip.setFramePosition(0);
		clip.start();
	}
}
