import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.NIOUtils;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FrameCatch {
	private FileChannelWrapper ch ;
	private FrameGrab grab;
	private long numFrame=0;
	private int current=0;

	public FrameCatch(String fileName){
		try {
			ch = NIOUtils.readableFileChannel(new File("video.mp4"));
			grab=new FrameGrab(ch);
			MP4Demuxer dm = new MP4Demuxer(ch);
			DemuxerTrack vt = dm.getVideoTrack();
			while(vt.nextFrame()!=null) numFrame++;
		} catch (JCodecException | IOException e) {
			e.printStackTrace();
		}
	}
	public BufferedImage getNextFrame(){
		try {
			return grab.seekToFramePrecise(current).getFrame();
		} catch (IOException | JCodecException e) {
			e.printStackTrace();
		}
		return null;
	}
}
