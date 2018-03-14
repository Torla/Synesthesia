import com.sun.media.sound.WaveFileWriter;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
	public static void main(String[] args){

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("nasi.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageStatistics stats = new ImageStatistics(image);

		System.out.println(stats);
		System.out.println(stats.getDominant(0.2));
		System.out.println(ColorsToChord.convert(stats.getDominant(0.2)));

		Sound sound= new Sound(new Chord(ColorsToChord.convert(stats.getDominant(0.2))));
		sound.save();


	}
}
