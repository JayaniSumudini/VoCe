import java.io.*;
import javax.sound.sampled.*;
import java.net.* ;

public class App extends Thread{
	private final static int packetsize = 100 ;
	static byte buf[] = new byte[500];
	static byte buf1[] = new byte[500];
	static int re_count;
	int port = 2000;
    static InetAddress address;

	AudioFormat audioFormat;
	TargetDataLine targetDataLine;	

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
			DatagramSocket socket = new DatagramSocket();
			System.out.println( "The App is ready..." ) ;
			//this.getClientAudio();
					try{
			Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();    //get available mixers
	        	System.out.println("Available mixers:");
	        	Mixer mixer = null;
	        	for (int cnt = 0; cnt < mixerInfo.length; cnt++) {
	            	System.out.println(cnt + " " + mixerInfo[cnt].getName());
	            	mixer = AudioSystem.getMixer(mixerInfo[cnt]);

	            	Line.Info[] lineInfos = mixer.getTargetLineInfo();
	            	if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
	                	System.out.println(cnt + " Mic is supported!");
	                	break;
	            	}
	        	}

	        	this.audioFormat = this.getAudioFormat();     //get the audio format
	        	DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, this.audioFormat);

	        	this.targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
	        	this.targetDataLine.open(this.audioFormat);
	        	this.targetDataLine.start();
	        }
	        catch(Exception e){
				e.printStackTrace();

	        }
	

			while (true) {
                re_count = this.targetDataLine.read(buf, 0, buf.length);

				DatagramPacket packet_send = new DatagramPacket(buf, buf.length, address, port);
			
				socket.send(packet_send);
				
				System.out.println("Sent...");
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}


	public static void main(String args[]){
		try{
	       	address=InetAddress.getByName(args[0]);
			App client = new App();
			//Server app_server=new Server();
			client.start();
			//app_server.start();
			}
		catch(Exception e){
			e.printStackTrace();
		}


	}

}