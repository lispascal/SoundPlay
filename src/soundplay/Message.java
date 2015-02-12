/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package soundplay;

import java.io.Serializable;

/**
 *
 * @author Pascal Lis
 */
    public class Message implements Serializable{
        String name;
        String msg;
        byte[] byteArray;
        long time;
        public Message(String nameIn, String msgIn) {
            name = nameIn;
            msg = msgIn;
        }
        public Message(String nameIn, byte[] bytesIn) {
            name = nameIn;
            byteArray = bytesIn;
        }
        
        String toReadableString(){
            StringBuilder sb = new StringBuilder();
            sb.append("\nname: ").append(name);
            sb.append("\nmsg: ").append(msg);
            sb.append("\nbyteArray size: ").append(byteArray.length);
            sb.append("\ntime: ").append(time);
            return sb.toString();
            
        }
    }