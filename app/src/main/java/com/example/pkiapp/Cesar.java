package com.example.pkiapp;

public class Cesar {

    /**
     * This function will call to "transform function" with the positive,
     * in order to encrypt the @param msg.
     * */
    public static String encriptar(String msg,int key){ return transform(msg,key); }//no change

    /**
     * This function will call to "transform function" but will convert the @param key to its oposite number,
     * so cesar algorithm just will count back, in order to desencrypt the msg.
     * */
    public static String desencriptar(String msg,int key){ return transform(msg,key*-1); }

    public static String transform(String msg,int key){
        String newMsg = "";
        if(key == 0)
            return msg;//no changes

        for(int indexMsg = 0 ; indexMsg < msg.length() ; indexMsg++){

            int charMsg = msg.charAt(indexMsg);

            int charMsgAdded = charMsg + key;
            //newMsg+=(char)charMsgAdded;

            if(charMsgAdded>=256){
                while(charMsgAdded>256){
                    charMsgAdded = charMsgAdded-256;
                }
            }else if(charMsgAdded<0){//it will add 255 until it be positive
                while(charMsgAdded<0){
                    charMsgAdded = charMsgAdded+256;
                }
            }
            newMsg += (char)charMsgAdded;
        }
        return newMsg;
    }

}
