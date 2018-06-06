import sun.java2d.pipe.SpanShapeRenderer;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

public class ImageStatistics {

	private BufferedImage image;
	public HashMap<String,Color> ExistingColor = new HashMap<>();
	private HashMap<String,Double> colorValue = new HashMap<>();
	private SimpleKMeans model;
	private static final int numCluster = 3;

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
		Attribute arr[] = new Attribute[5];
		arr[0] = new Attribute("Red");
		arr[1] = new Attribute("Green");
		arr[2] = new Attribute("Blue");
		arr[3] = new Attribute("x");
		arr[4] = new Attribute("y");
		FastVector attrs = new FastVector(4);
		for (Attribute x:arr) {attrs.addElement(x);}

		Instances dataSet = new Instances("xxx",attrs,1000000);
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				double[] v = new double[5];
				v[0]=(new Color(image.getRGB(i,j)).getRed());
				v[1]=(new Color(image.getRGB(i,j)).getGreen());
				v[2]=(new Color(image.getRGB(i,j)).getBlue());
				v[3]=(i);
				v[4]=(j);
				dataSet.add(new Instance(1,v));
			}
		}

		SimpleKMeans model = new SimpleKMeans();
		try {
			model.setNumClusters(numCluster);
			model.buildClusterer(dataSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.model=model;
		//System.out.println(model.getClusterCentroids().toString());
	}

	ArrayList<String> getDominant(){
		ArrayList<String> ret = new ArrayList<>();
		for(Instance instance: (List<Instance>)Collections.list(model.getClusterCentroids().enumerateInstances())){
			Color color = new Color((int)instance.value(0),(int)instance.value(1),(int)instance.value(2));
			ret.add(ExistingColor.entrySet().stream().min((x,y)->Double.compare(distance(x.getValue(),color),distance(y.getValue(),color))).get().getKey());
		}
		return ret;
	}

	@Override
	public String toString() {
		return "\n" + image.toString() +
				"\n" + colorValue.toString();
	}

	public void printImage() throws Exception{
		BufferedImage imageOut = new BufferedImage(image.getWidth(),image.getHeight(),TYPE_3BYTE_BGR);
		for(int i=0;i<image.getWidth();i++){
			for(int j=0;j<image.getHeight();j++){
				ArrayList<Instance> list =Collections.list(model.getClusterCentroids().enumerateInstances());
				List<Color> c = list.stream().map(x -> new Color((int)x.value(0),(int)x.value(1),(int)x.value(2))).collect(Collectors.toList());
				double[] v = new double[3];
				v[0]=(new Color(image.getRGB(i,j)).getRed());
				v[1]=(new Color(image.getRGB(i,j)).getGreen());
				v[2]=(new Color(image.getRGB(i,j)).getBlue());
				imageOut.setRGB(i,j,c.get(model.clusterInstance(new Instance(1,v))).getRGB());

			}
		}
		ImageIO.write(imageOut, "jpg",new File("out.jpg"));
	}
}
