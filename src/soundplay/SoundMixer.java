package soundplay;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Pascal Lis
 */
/**
     * A class that schedules sounds
     */
    public class SoundMixer {
        private final PriorityBlockingQueue<Message> queue;
        private volatile long pos = 0;
        private final ConcurrentHashMap<String, Long> timemap;
        
        public SoundMixer(){
            queue = new PriorityBlockingQueue(5, new Comparator<Message>(){
                @Override
                public int compare(Message o1, Message o2) {
                    return (int) (o1.time - o2.time);
                }
            });
            timemap = new ConcurrentHashMap<>();
        }
        
        /**
         * 
         * @return true if the internal queue is not empty.
         */
        synchronized public boolean available(){
            return !(queue.isEmpty());
        }
        
        
        
        /**
         * Add a sound to the PriorityBlockingQueue which is sorted by time,
         * and set the time accordingly for that sound and the global time.
         * @param sound 
         */
        synchronized public void addSound(Message sound){
            timemap.putIfAbsent(sound.name, new Long(pos));

//            if(timemap.get(sound.name) > pos + 50)
//            {
//                ask dummy to stop transmitting
//                return;
//            }
            sound.time = timemap.get(sound.name);
            if(sound.time < pos)
                sound.time = pos;
            timemap.put(sound.name, sound.time+1); // 
            queue.put(sound);
            
        }
        
        /**
         * Update time position, then grab all the sounds that weren't processed
         * and have the old time position, and mix them.
         * Relies on microphone being 16-bit per channel, little-endian, and 
         * on the input buffers to Messages being 1000 long
         * @return The mixed sounds
         */
        synchronized public byte [] getMixedSounds(){
//            ArrayList<Message> sounds = new ArrayList<>();

//            test for sound quality below
////            if(!queue.isEmpty())
////                return queue.poll().byteArray;
            ArrayList<ByteBuffer> soundsbb = new ArrayList<>();
            final int MESSAGE_SIZE = 1000;
//            byte[] result = new byte[MESSAGE_SIZE];
            ByteBuffer res = ByteBuffer.allocate(MESSAGE_SIZE).order(ByteOrder.LITTLE_ENDIAN);
            pos++;
            
            while(!queue.isEmpty() && queue.peek().time + 1 <= pos)
            {
                if(queue.peek().time + 1 == pos) // all sounds of previous time pos
//                    sounds.add(queue.poll());
                    soundsbb.add(ByteBuffer.wrap(queue.poll().byteArray).order(ByteOrder.LITTLE_ENDIAN));
            }
            
            int num = soundsbb.size();
//              int num = sounds.size();
//            
            for(ByteBuffer bb : soundsbb)
            {
                for(int i = 0; i < bb.capacity(); i+= 2)
//                    result[i] += bb.get(i) / num;
                    res.putShort(i, (short) (res.getShort(i) + bb.getShort(i) / num));
            }
                
//            // combine sample-by-sample. Assumes 16-bit 2-channel pwm.
//            for(int i = 0; i < MESSAGE_SIZE; i += 2 )
//            {
//                short b = 0;
//                for(Message sound : sounds)
//                {
//                    b += ByteBuffer.wrap(sound.byteArray).order(ByteOrder.LITTLE_ENDIAN).getShort(i) / num;
////                    b += (sound.byteArray[i] << 8) / num;
////                    b += sound.byteArray[i+1] / num;
//                }
//                
//                
////                b = b/num;
////                
////                if (b > 0x0FFFF)
////                    b = 0x0FFFF;
//                ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN).putShort(i, b);
////                result[i] = (byte) ((b >> 8) & 0x00FF); // left half of remaining short
////                result[i+1] = (byte) (b & 0x00FF); // right half 
//            }
//            return result;
            return res.array();
        }
        
        
    }