import java.util.ArrayList;
import java.util.HashMap;

public class Chord {
	static private HashMap<String,Double> noteToFrequencies;
	static {
		noteToFrequencies=new HashMap<>();
		noteToFrequencies.put("A",440.);
		noteToFrequencies.put("B",493.88);
		noteToFrequencies.put("C",523.25);
		noteToFrequencies.put("D",587.33);
		noteToFrequencies.put("E",659.25);
		noteToFrequencies.put("F",698.46);
		noteToFrequencies.put("G",783.99);
	}

	private ArrayList<String> notes = new ArrayList<>();
	private ArrayList<Double> frequencies = new ArrayList<>();

	public Chord(ArrayList<String> list){
		notes.addAll(list);
		for(String s:list){
			frequencies.add((noteToFrequencies.get(s)>=noteToFrequencies.get(list.get(0)))?noteToFrequencies.get(s):noteToFrequencies.get(s)*2);
		}
	}

	public ArrayList<Double> getFrequencies() {
		return frequencies;
	}

	@Override
	public String toString() {
		return "\n" + notes +
				"\n" +frequencies;
	}
}
