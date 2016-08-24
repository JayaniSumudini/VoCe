import java.net.* ;
import java.io.*;
import javax.sound.sampled.*;

public class Server extends Thread{
	private final static int packetsize = 500 ;
	SourceDataLine sourceDataLine;
	AudioFormat audioFormat;
	int port = 2000;
	static byte tempBuffer[] = new byte[500];

	public AudioFormat getAudioFormat() {
    	float sampleRate = 16000.0F;
    	int sampleSizeInBits = 16;
    	int channels = 2;
    	boolean signed = true;
    	boolean bigEndian = true;
    	return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}


public void run(){
		try{
			
			DatagramSocket socket=new DatagramSocket(port);
			System.out.println("The server is ready...") ;

			DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize );
			//RecordPlayback rp = new RecordPlayback();
			//this.getServerAudio();
			try{
				this.audioFormat = this.getAudioFormat();
				DataLine.Info dataLineInfo1 = new DataLine.Info(SourceDataLine.class, this.audioFormat);
	        	this.sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
	        	this.sourceDataLine.open(this.audioFormat);
	        	this.sourceDataLine.start();

	        	//Setting the maximum volume
	        	FloatControl control = (FloatControl)this.sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
	        	control.setValue(control.getMaximum());
	        }	
        	catch(Exception e){
				e.printStackTrace();
			}	
			
			for(;;){
				socket.receive(packet);
				tempBuffer = packet.getData();
				this.sourceDataLine.write(tempBuffer, 0, 500);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
}
	public static void main(String args[]){
		try{
	       	address=InetAddress.getByName(args[0]);
			//App client = new App();
			Server app_server=new Server();
			//client.start();
			app_server.start();
			}
		catch(Exception e){
			e.printStackTrace();
		}


	}

}



//bytebuffer
//multicasting in udp , JAva2SE