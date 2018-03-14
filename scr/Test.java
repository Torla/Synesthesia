import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Test {
	public static void main(String[] args){

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("od.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageStatistics stats = new ImageStatistics(image);

		Double ThreshHold = 0.1;

		System.out.println(stats);
		System.out.println(stats.getDominant(ThreshHold, 2));
		System.out.println(ColorsToChord.convert(stats.getDominant(ThreshHold, 2)));

		Sound sound= new Sound(new Chord(ColorsToChord.convert(stats.getDominant(ThreshHold, 2))));
		sound.save();


	}
}
