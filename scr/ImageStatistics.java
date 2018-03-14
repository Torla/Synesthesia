import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageStatistics {

	private BufferedImage image;
	public HashMap<String,Color> ExistingColor = new HashMap<>();
	private HashMap<String,Double> colorValue = new HashMap<>();

	ImageStatistics(BufferedImage image){
		this.image=image;
		ExistingColor.put("DeepRed",new Color(82,0,0));
		ExistingColor.put("Red",new Color(116,0,0));
		ExistingColor.put("BrightRed",new Color(179,0,0));
		ExistingColor.put("DarkOrange",new Color(238,127,0));
		ExistingColor.put("Orange",new Color(255,99,0));
		ExistingColor.put("Yellow",new Color(255,236,0));
		ExistingColor.put("LightGreen",new Color(153,255,0));
		ExistingColor.put("Green",new Color(40,255,0));
		ExistingColor.put("SkyBlue",new Color(0,255,232));
		ExistingColor.put("LightBlue",new Color(0,124,255));
		ExistingColor.put("Blue",new Color(5,0,255));
		ExistingColor.put("Indigo",new Color(69,0,234));
		ExistingColor.put("Violet",new Color(87,0,158));
		compute();
	}


	private double distance(Color c1, Color c2){
		double value=0;
		value += Math.pow(c1.getRed()-c2.getRed(),2);
		value += Math.pow(c1.getGreen()-c2.getGreen(),2);
		value += Math.pow(c1.getBlue()-c2.getBlue(),2);
		value = Math.sqrt(value);
		return value;
	}

	private void  compute(){
		for(String s:ExistingColor.keySet()) {colorValue.put(s,0.);}
		for(int i=0;i<image.getHeight();i++){
			for(int j=0;j<image.getWidth();j++){
				HashMap<String,Double> temp = new HashMap<>();
				for(String s:ExistingColor.keySet()){
					temp.put(s, distance(new Color(image.getRGB(j,i)),ExistingColor.get(s)));
				}
				String min = temp.entrySet().stream().min((x,y)->(Double.compare(x.getValue(),y.getValue()))).get().getKey();
				colorValue.put(min,colorValue.get(min)+1);
			}
		}
		final double max = colorValue.values().stream().mapToDouble(x -> (Double.valueOf(x))).max().getAsDouble();
		for(String s:colorValue.keySet()){
			colorValue.put(s,colorValue.get(s)/ max);
		}
	}

	ArrayList<String> getDominant(double Threshold){
		return colorValue.entrySet().stream().filter(x->(x.getValue()>Threshold))
				.sorted((y,x)->(Double.compare(x.getValue(),y.getValue())))
				.map(Map.Entry::getKey)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public String toString() {
		return "\n" + image.toString() +
				"\n" + colorValue.toString();
	}
}
