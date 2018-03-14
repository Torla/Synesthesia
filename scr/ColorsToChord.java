import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ColorsToChord {
	static private HashMap<String,String> colorToNote;
	static {
		colorToNote =new HashMap<>();
		colorToNote.put("DeepRed","F");
		colorToNote.put("Red","F#");
		colorToNote.put("BrightRed","G");
		colorToNote.put("DarkOrange","G#");
		colorToNote.put("Orange","A");
		colorToNote.put("Yellow","A#");
		colorToNote.put("LightGreen","B");
		colorToNote.put("Green","C");
		colorToNote.put("SkyBlue","C#");
		colorToNote.put("LightBlue","D");
		colorToNote.put("Blue","D#");
		colorToNote.put("Indigo","E");
		colorToNote.put("Violet","F");
	}
	static ArrayList<String> convert(ArrayList<String> colors){
		return colors.stream().map(x->(colorToNote.get(x))).collect(Collectors.toCollection(ArrayList::new));
	}
}
