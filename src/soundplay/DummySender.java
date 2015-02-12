/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package soundplay;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;

/**
 *
 * @author Pascal Lis
 */
public class DummySender extends DummySpeaker {

    
    /**
     * 
     * @param inputStream - accepts input and does nothing with it.
     * @param outputStream - outputs messages to this stream.
     * @param givenName - name attached to output
     * @param buffer_size - size of Message
     * @param hertz - Frequency of messages
     * @throws LineUnavailableException 
     */
    public DummySender(InputStream inputStream, OutputStream outputStream, String givenName, int buffer_size, double hertz) throws LineUnavailableException, IOException {
        super(inputStream, outputStream, givenName, buffer_size, hertz);
        frequency = hertz * 1/(.9/44100);
    }
    int num;
    
    @Override
    void speak(){
        ObjectOutputStream oos;
        try {                
            oos = new ObjectOutputStream(out);
            Message msg = new Message(name, "hi #" + num++);
            oos.writeObject(msg);
//                    System.out.println("dummy sent packet");
        } catch (IOException ex) {
            Logger.getLogger(SoundWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
