


import org.jcodec.api.FrameGrab;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Test {
	public static void main(String[] args) throws Exception{

		//processImage("van.jpg");

		BufferedImage frame = FrameGrab.getFrame(new File("video.mp4"),10);


		File tempFile = new File("frame.jpg");
		ImageIO.write(frame, "jpg", tempFile);




	}

	private static void processImage(String path) throws Exception {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageStatistics stats = new ImageStatistics(image);
		System.out.println(stats.getDominant());
		System.out.println(ColorsToChord.convert(stats.getDominant()));


		Sound sound= new Sound(new Chord(ColorsToChord.convert(stats.getDominant()),0));
		sound.save();

		stats.printImage();
	}
}
