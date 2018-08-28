package org.dpo.mnemo;

import java.net.URL;
import java.util.List;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class PlaySequenceTask extends TimerTask  implements LineListener  {
	
	public static final String SOUNDS_DIRECTORY_PATH = "/sounds";
	
	private WorkingMemory workingMemory;
	private int indiceSequence;
	private boolean playing;

	public static PlaySequenceTask buildPlaySequenceTask( WorkingMemory workingMemory){
		PlaySequenceTask playSequenceTask = new PlaySequenceTask();
		playSequenceTask.setWorkingMemory(workingMemory);
		playSequenceTask.setIndiceSequence(0);
		return playSequenceTask;
	}
	
	
	public synchronized boolean isPlaying() {
		return playing;
	}

	public synchronized void setPlaying(boolean playing) {
		this.playing = playing;
	}
	
	
	
	
	public synchronized WorkingMemory getWorkingMemory() {
		return workingMemory;
	}


	public synchronized void setWorkingMemory(WorkingMemory workingMemory) {
		this.workingMemory = workingMemory;
	}


	public synchronized List<Integer> getListSequence() {
		return getWorkingMemory().getListSequence();
	}




	public synchronized int getIndiceSequence() {
		return indiceSequence;
	}


	public synchronized void setIndiceSequence(int indiceSequence) {
		this.indiceSequence = indiceSequence;
	}


	public void playNumber(int n) {

		URL url = WorkingMemory.class.getResource(SOUNDS_DIRECTORY_PATH + "/" + n + ".wav");

		String urls = url.toString();
		urls = urls.replaceFirst("file:/", "file:///");

		try {
			Line.Info linfo = new Line.Info(Clip.class);
			Line line = AudioSystem.getLine(linfo);

			Clip clip = (Clip) line;
			clip.addLineListener(this);
			AudioInputStream ais = AudioSystem.getAudioInputStream(new URL(urls));
			clip.open(ais);
			clip.start();
			while (isPlaying()) {
				Thread.sleep(50);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public void update(LineEvent lineEvent) {
		LineEvent.Type type = lineEvent.getType();
		if (type == LineEvent.Type.OPEN) {
			setPlaying(true);
		} else if (type == LineEvent.Type.CLOSE) {
			setPlaying(false);
		} else if (type == LineEvent.Type.START) {
			setPlaying(true);

		} else if (type == LineEvent.Type.STOP) {
			setPlaying(false);
			if(getIndiceSequence()==getListSequence().size()-1){
				getWorkingMemory().sequenceEnded();
			}
		}
	}
	
	
	@Override
	public void run() {
		if(getIndiceSequence()<getListSequence().size()){
			playNumber(getListSequence().get(getIndiceSequence()));
			setIndiceSequence(getIndiceSequence()+1);
		} else {
			this.cancel();
		}
	}

	
	
}
