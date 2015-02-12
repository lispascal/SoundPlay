package soundplay;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;

/**
 *
 * @author Pascal Lis
 */
public class SoundPlay {
    
    
    static TargetDataLine getDefaultMic_tdl() throws LineUnavailableException{
        Mixer.Info[] s = AudioSystem.getMixerInfo();
        
        
        for(Mixer.Info i : s)
        {
            Mixer ip = AudioSystem.getMixer(i);
            for(Line.Info ipt : ip.getTargetLineInfo())
            {
                System.out.println("Got " + i.getName());
                return (TargetDataLine) AudioSystem.getLine(ipt);
            }
            
        }
        return null;
    }
    
    static Port getMic() throws LineUnavailableException {
        Port lineIn;
        Mixer mixer = AudioSystem.getMixer(null);
        lineIn = (Port)mixer.getLine(Port.Info.LINE_IN);
        return lineIn;
    }
    static Port getSpeaker() throws LineUnavailableException {
        Port lineOut;
        Mixer mixer = AudioSystem.getMixer(null);
        lineOut = (Port)mixer.getLine(Port.Info.LINE_OUT);
        return lineOut;
    }
    
    static SourceDataLine getDefaultSpeakers_sdl() throws LineUnavailableException{
        Mixer.Info[] s = AudioSystem.getMixerInfo();
        
        
        for(Mixer.Info i : s)
        {
            Mixer ip = AudioSystem.getMixer(i);
            for(Line.Info ips : ip.getSourceLineInfo())
            {
                System.out.println("Got " + i.getName());
                return (SourceDataLine) AudioSystem.getLine(ips);
            }
            
        } 
        return null;
    }
    
    static void getAllMixers() throws LineUnavailableException{
        //Mixer.Info[] s = AudioSystem.getMixerInfo();
        for(Mixer.Info i : AudioSystem.getMixerInfo())
        {
            Mixer ip = AudioSystem.getMixer(i);
            System.out.println(i.getName());
            for(Line.Info ips : ip.getSourceLineInfo())
                System.out.println("s\t" + ips.getLineClass());
            for(Line.Info ipt : ip.getTargetLineInfo())
                System.out.println("t\t" + ipt.getLineClass());
            System.out.println();
        }
    }
    
    static void test1() throws LineUnavailableException
    {
        SourceDataLine speaker = getDefaultSpeakers_sdl();
        TargetDataLine mic = getDefaultMic_tdl();
        
        mic.open();
        speaker.open();
        
        System.out.println(mic.getFormat().getSampleRate());
        System.out.println(mic.getFormat().getSampleSizeInBits());
        System.out.println(mic.getFormat().getChannels());
        System.out.println(mic.getFormat().getFrameRate());
        System.out.println(mic.getFormat().getFrameSize());
        System.out.println(mic.getFormat().isBigEndian());
        
        System.out.println(speaker.getFormat().getEncoding());
        System.out.println(speaker.available());
        System.out.println(speaker.getFormat().isBigEndian());
        System.out.println(mic.available());


        mic.start();
        speaker.start();
            
        byte[] b = new byte[44104]; // 1/2 of a second at a time
        for(int i = 0; i < 20000; i++)
        {
//            System.out.println(speaker.available());
            if(mic.available() != 0)
                System.out.println(mic.available());
            mic.read(b, 0, 44104);
            speaker.write(b, 0, 44104);
        }
    }
    
    static void test2() throws LineUnavailableException
    {
        Port out = getSpeaker();
        Port in = getMic();
        
        out.open();
        in.open();
    }
    
//    static ClientWindow makeClientWindow(SoundWindow soundWindow, int port, int serverPort) throws UnknownHostException
//    {
//        ClientWindow cli = new ClientWindow(soundWindow, port, InetAddress.getLocalHost(), serverPort);
//        cli.pack();
//        cli.setVisible(true);
//        return cli;
//    }
    
    
    /**
     * @param args the command line arguments
     * @throws javax.sound.sampled.LineUnavailableException
     */
    public static void main(String[] args) throws LineUnavailableException, InterruptedException {
        SoundWindow sw = new SoundWindow();
        sw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sw.pack();
        sw.setVisible(true);
        sw.requestFocus();
        getAllMixers();
//        test1();
//        ServerWindow server = new ServerWindow();
//        server.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        server.pack();
//        server.setVisible(true);
        
//        sleep(1000);
//        try {
//            ClientWindow cli1 = makeClientWindow(sw, 3001, 3000);
//            //ClientWindow cli2 = makeClientWindow(sw, 3002, 3000);
//            //ClientWindow cli3 = makeClientWindow(sw, 3003, 3000);
//        } catch (UnknownHostException ex) {
//            Logger.getLogger(SoundPlay.class.getName()).log(Level.SEVERE, null, ex);
//        }
        

        
        
    }

    
}
