


import org.jcodec.api.FrameGrab;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.NIOUtils;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

public class Test {
	public static void main(String[] args) throws Exception{

		//processImage("van.jpg");
	FrameCatch frameCatch = new FrameCatch("video.mp4");
	Sound sound= new Sound((int)frameCatch.getNumFrame(),30);
	for(int i=0;i<frameCatch.getNumFrame();i++) {
		BufferedImage image = frameCatch.getNextFrame();
		System.out.println(i);
		ImageStatistics stats = new ImageStatistics(image);

		//File tempFile = new File("frame.jpg");
		//ImageIO.write(image, "jpg", tempFile);

		System.out.println(stats.getDominant());
		System.out.println(ColorsToChord.convert(stats.getDominant()));

		//stats.printImage();

		sound.add(new Chord(ColorsToChord.convert(stats.getDominant()),0),stats.getCount());


	}
		sound.save();


	}
/*
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
	}*/
}
