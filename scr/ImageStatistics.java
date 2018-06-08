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
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

public class ImageStatistics {

	private BufferedImage image;
	public HashMap<String,Color> ExistingColor = new HashMap<>();
	private HashMap<String,Double> colorValue = new HashMap<>();
	private SimpleKMeans model;

	private static final int LowDefW=300;
	private static final int LowDefH=300;

	private static final int maxCluster = 10;
	private static final double desiredStdDev = 30.;

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
		BufferedImage image=lowDef(this.image,300,300);
		Attribute arr[] = new Attribute[3];
		arr[0] = new Attribute("Red");
		arr[1] = new Attribute("Green");
		arr[2] = new Attribute("Blue");
		FastVector attrs = new FastVector(4);
		for (Attribute x:arr) {attrs.addElement(x);}

		Instances dataSet = new Instances("xxx",attrs,1000000);
		for(int i=0;i<image.getWidth();i+=5){
			for(int j=0;j<image.getHeight();j+=5){
				double[] v = new double[3];
				v[0]=(new Color(image.getRGB(i,j)).getRed());
				v[1]=(new Color(image.getRGB(i,j)).getGreen());
				v[2]=(new Color(image.getRGB(i,j)).getBlue());
				dataSet.add(new Instance(1,v));
			}
		}

		SimpleKMeans model=null;
		for(int numCluster=1;numCluster<maxCluster;numCluster++){
			model = createModel(dataSet,numCluster);
			ArrayList<Instance> list =Collections.list(model.getClusterStandardDevs().enumerateInstances());
			double stdDev = list.stream().flatMap(x -> {
				ArrayList<Double> l = new ArrayList<>();
				l.add(x.value(0));
				l.add(x.value(1));
				l.add(x.value(2));
				return l.stream();
			}).mapToDouble(x->x).average().getAsDouble();
			System.out.println(list);
			System.out.println(stdDev);
			if(stdDev<=desiredStdDev)break;
		}
		this.model=model;

	}

	private SimpleKMeans createModel(Instances dataSet,int numCluster) {
		SimpleKMeans model = new SimpleKMeans();
		try {
			model.setNumClusters(numCluster);
			model.buildClusterer(dataSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.model=model;
		return model;
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

	private  static BufferedImage lowDef(BufferedImage image,int w,int h){
		BufferedImage imageLD = new BufferedImage(w,h,TYPE_3BYTE_BGR);
		int ratioW=image.getWidth()/w;
		int ratioH=image.getHeight()/h;
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				int startW = i*ratioW;
				int startH = j*ratioH;
				int r=0;
				int g=0;
				int b=0;
				int x,y=0;
				for(x=startW;x<startW+ratioW && x<image.getWidth();x++){
					for(y=startH;y<startH+ratioH && y<image.getHeight();y++){
						r+=new Color(image.getRGB(x,y)).getRed();
						g+=new Color(image.getRGB(x,y)).getGreen();
						b+=new Color(image.getRGB(x,y)).getBlue();
					}
				}
				x-=startW;
				y-=startH;
				imageLD.setRGB(i,j,new Color(r/(x*y),g/(x*y),b/(x*y)).getRGB());
			}
		}
		try {
			ImageIO.write(imageLD,"jpg",new File("outLD.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageLD;
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
