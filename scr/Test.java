import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Test {
	public static void main(String[] args) {

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("od.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageStatistics stats = new ImageStatistics(image);

		System.out.println(stats);
		System.out.println(stats.getDominant(0.8));



	}
}
