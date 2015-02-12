package soundplay;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.text.BadLocationException;
import soundplay.Message;
import soundplay.SoundWindow;


/**
 * DummySpeaker sends out a tonal frequency on the given stream. It is assumed
 * that the format will use 16-bit, 2-channel, 44.1kHz. The thread will probably
 * try sleeping for (freq/44100) seconds... we'll see if that works.
 * @author Pascal Lis
 */


public class DummySpeaker implements Runnable, AutoCloseable{
        volatile OutputStream out;
        String name;
        double frequency;
        volatile int pos = 0;
        volatile boolean stop;
        InputStream in;
        int size;
        DummyReceiver dummyReceiver;
        ObjectOutputStream oos;
        Thread drt; // dummyReceiverthread
        /**
         * 
         * @param inputStream - accepts dummy data sent to this speaker
         * @param outputStream - Stream to which messages will be sent
         * @param givenName - name to attach to messages created by this speaker
         * @param buffer_size - size of the byte[] buffer in Message
         * @param hertz - frequency of the tone
         * @throws LineUnavailableException 
         */
        public DummySpeaker(InputStream inputStream, OutputStream outputStream, String givenName, int buffer_size, double hertz) throws LineUnavailableException, IOException {
            stop = false;
            frequency = hertz;
            in = inputStream;
            out = outputStream;
            oos = new ObjectOutputStream(out);
            name = givenName;
            size = buffer_size;
        }
        
        void speak(){
            
            try {      
                Message msg = new Message(name, getSample(size));
                oos.writeObject(msg);
//                    System.out.println("dummy sent packet");
            } catch (IOException ex) {
                System.out.println("DummySpeaker Failed");
                Logger.getLogger(SoundWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @Override
        public void run(){
            dummyReceiver = new DummyReceiver();
            drt = new Thread(dummyReceiver);
            drt.start();
            
            while(!stop)
            {
                speak();
                try { // MESSAGE_SIZE / CHANNELS / SAMPLE_RATE * 1000 = time in s generated per getSample().
                    sleep((long) ( 1000. * 1000. / 88200. * .2)); // try to sleep 50% of that time
                } catch (InterruptedException ex) {
                    Logger.getLogger(DummySpeaker.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }

        
        /**
         * Repeated calls to getSample() should return a continuous sine 
         * signal of the given frequency.
         * 
         * @param frequency - frequency of the tone
         * @param size - size of the buffer to return
         * @return 
         */
        byte[] getSample(int size) {
            ByteBuffer res = ByteBuffer.allocate(size);
            res.order(ByteOrder.LITTLE_ENDIAN);
            double factor = frequency / 44100 * 2.0 * Math.PI;
            
            // every 2 bytes is one half sample (because stereo).
            for(int i = 0; i < size ; i+= 4)
            {
                short fac2 = (short) (Short.MAX_VALUE * (double) Math.sin((i/4 + pos) * factor));
                res.putShort(i, fac2);
                res.putShort(i + 2, fac2);
//                res.put(i+1, (byte)((fac2 >> 8 )& 0x0FF));
//                res.put(i, (byte)(fac2 & 0x0FF));
//                res.put(i+3, (byte)((fac2 >> 8 )& 0x0FF));
//                res.put(i+2, (byte)(fac2 & 0x0FF));
            }
            pos += size/2/2;
            return res.array();
            
        }

        /**
         * Will close both itself and the dummyReceiver by setting stop=true.
         */
    @Override
    public void close() {
        stop = true;
            try {
                //drt.join();
                in.close();
                out.close();
            } catch (IOException ex) {
                System.out.println("Failed to close DummySpeaker's streams");
                Logger.getLogger(DummySpeaker.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        
    private class DummyReceiver extends Thread{
        
        
        @Override
        public void run(){
            
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(in);
                while(!stop)
                {
                    Message m;
                    m = (Message) ois.readObject();
                    //m = ois.readObject();
    //                    System.out.println("message object received by dummy" + m.byteArray);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DummySpeaker.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
        
        
        
    }