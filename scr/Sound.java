import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Sound {

	static private double sampleRate=44100.0;

	private double amplitude = 0.8;
	private double framePerSecond;
	private float[] buffer;
	private byte[] byteBuffer;
	private LinkedList<ArrayList<Double>> freq= new LinkedList<>();
	private LinkedList<ArrayList<Double>> count= new LinkedList<>();


	public Sound(int totFrame,double framaRate){
		framePerSecond=framaRate;
		buffer = new float[(int) (totFrame/framaRate * sampleRate)];
		byteBuffer = new byte[buffer.length * 2];
	}

	private void add(ArrayList<Double> frequency){
		freq.addLast(frequency);
	}
	public void add(Chord chord,ArrayList<Double> count){
		add(chord.getFrequencies());
		this.count.addLast(count);
	}

	private void produceTrack() {
		int frame=0;
		for(ArrayList<Double> frequencys:freq) {
			ArrayList<Double> twoPiFs = new ArrayList<>();
			int f=0;
			for (double d : frequencys) {
				twoPiFs.add(d * Math.PI * 2 * count.get(frame).get(f));
			}
			for (int sample = (int) (frame*sampleRate/framePerSecond); sample <(int) ((frame+1)*sampleRate/framePerSecond) ; sample++) {
				double time = sample / sampleRate;
				for (Double d : twoPiFs) {
					buffer[sample] += (float) (amplitude/ twoPiFs.size() * Math.sin(d * time));
				}

			}
			frame++;
		}
		int bufferIndex = 0;
		for (int i = 0; i < byteBuffer.length; i++) {
			final int x = (int) (buffer[bufferIndex++] * 32767.0);
			byteBuffer[i] = (byte) x;
			i++;
			byteBuffer[i] = (byte) (x >>> 8);
		}
	}



	public void save(){
		produceTrack();
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
