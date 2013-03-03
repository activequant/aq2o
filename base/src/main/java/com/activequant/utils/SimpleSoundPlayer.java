package com.activequant.utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SimpleSoundPlayer {

	public static void playSound(String soundFile) {
		try {
			
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream;

			inputStream = AudioSystem
					.getAudioInputStream(SimpleSoundPlayer.class.getClassLoader()
							.getResourceAsStream(soundFile));
			clip.open(inputStream);
			clip.start();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) throws InterruptedException{
		// 
		SimpleSoundPlayer.playSound("sounds/negative.wav");
		Thread.sleep(100);
		
		SimpleSoundPlayer.playSound("sounds/negative.wav");
		Thread.sleep(100);
		SimpleSoundPlayer.playSound("sounds/negative.wav");
		Thread.sleep(100);
		SimpleSoundPlayer.playSound("sounds/negative.wav");
		Thread.sleep(100);
		SimpleSoundPlayer.playSound("sounds/negative.wav");
		Thread.sleep(100000);
		
	}

}
