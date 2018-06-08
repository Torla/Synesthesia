import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Sound {

	static private double sampleRate=44100.0;
	static private double seconds = 1/25;
	static private double amplitude = 0.8;
	private float[] buffer = new float[(int) (seconds * sampleRate)];
	private final byte[] byteBuffer = new byte[buffer.length * 2];

	public Sound(ArrayList<Double> frequencys){
		ArrayList<Double> twoPiFs = new ArrayList<>();
		for(double d:frequencys){twoPiFs.add(d*Math.PI*2);}
		for (int sample = 0; sample < buffer.length; sample++)
		{
			double time = sample / sampleRate;
			for(Double d:twoPiFs) {
				buffer[sample] += (float) (amplitude / twoPiFs.size() * Math.sin(d * time));
			}

		}
		int bufferIndex = 0;
		for (int i = 0; i < byteBuffer.length; i++) {
			final int x = (int) (buffer[bufferIndex++] * 32767.0);
			byteBuffer[i] = (byte) x;
			i++;
			byteBuffer[i] = (byte) (x >>> 8);
		}

	}

	public Sound(Chord chord){
		this(chord.getFrequencies());
	}

	public void save(){
		File out = new File("out.wav");
		boolean bigEndian = false;
		boolean signed = true;
		int bits = 16;
		int channels = 1;
		AudioFormat format;
		format = new AudioFormat((float)sampleRate, bits, channels, signed, bigEndian);
		ByteArrayInputStream bais = new ByteArrayInputStream(byteBuffer);
		AudioInputStream audioInputStream;
		audioInputStream = new AudioInputStream(bais, format,buffer.length);
		try {
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, out);
			audioInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
