import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageStatistics {

	private BufferedImage image;
	public HashMap<String,Color> ExistingColor = new HashMap<>();
	private HashMap<String,Double> colorValue = new HashMap<>();

	ImageStatistics(BufferedImage image){
		this.image=image;
		ExistingColor.put("Red",new Color(255,0,0));
		ExistingColor.put("Orange",new Color(255,127,0));
		ExistingColor.put("Yellow",new Color(255,255,0));
		ExistingColor.put("Green",new Color(0,255,0));
		ExistingColor.put("Blue",new Color(0,0,255));
		ExistingColor.put("Indigo",new Color(75,0,130));
		ExistingColor.put("Violet",new Color(139,0,255));
		compute();
	}


	private double scalarProduct(Color c1, Color c2){
		int value=0;
		int norm=0;
		value += c1.getRed()*c2.getRed();
		value += c1.getGreen()*c2.getGreen();
		value += c1.getBlue()*c2.getBlue();
		norm=(c1.getGreen()+c1.getBlue()+c1.getRed())*(c2.getGreen()+c2.getBlue()+c2.getRed());
		double ret = (double)value/(double)norm;
		ret*=ret;
		return (norm==0)?0:ret;
	}

	private void  compute(){
		for(String s:ExistingColor.keySet()) {colorValue.put(s,0.);}
		for(int i=0;i<image.getHeight();i++){
			for(int j=0;j<image.getWidth();j++){
				for(String s:ExistingColor.keySet()){
					colorValue.put(s,colorValue.get(s)+scalarProduct(new Color(image.getRGB(j,i)),ExistingColor.get(s)));
				}
			}
		}
		final double max = colorValue.values().stream().mapToDouble(x -> (Double.valueOf(x))).max().getAsDouble();
		for(String s:colorValue.keySet()){
			colorValue.put(s,colorValue.get(s)/ max);
		}
	}

	ArrayList<String> getDominant(double Threshold){
		return colorValue.entrySet().stream().filter(x->(x.getValue()>Threshold)).map(Map.Entry::getKey).collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public String toString() {
		return "\n" + image.toString() +
				"\n" + colorValue.toString();
	}
}
