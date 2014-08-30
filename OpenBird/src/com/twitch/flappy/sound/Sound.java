package com.twitch.flappy.sound;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
	public static final Sound clink = new Sound("clink.wav");
	public static final Sound dead = new Sound("muerte.wav");
	private AudioClip clip;

	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
