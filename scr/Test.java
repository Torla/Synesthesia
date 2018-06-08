import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.clusterers.*;

import com.xuggle.ferry.*;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

public class Test {
	public static void main(String[] args) throws Exception{

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("nasi.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageStatistics stats = new ImageStatistics(image);
		System.out.println(stats.getDominant());


		Sound sound= new Sound(new Chord(ColorsToChord.convert(stats.getDominant())));
		sound.save();

		stats.printImage();






	}
}
