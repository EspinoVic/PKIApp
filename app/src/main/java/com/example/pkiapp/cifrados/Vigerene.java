package com.example.pkiapp.cifrados;

import java.util.Calendar;
import java.util.Random;

public class Vigerene {


    public static String decifrar(String fullMsgEncrypted) throws Exception {
         String[] dataMsgEncrypted = separateMessageAndKey(fullMsgEncrypted);
         String decryptedMsg = applyVigerene(dataMsgEncrypted[0],dataMsgEncrypted[1],false);

         return decryptedMsg;
//        return Arrays.toString(dataMsgEncrypted);
    }

    public static String cifrar(String msg) throws Exception {

        if(msg==null){
            throw new Exception("Message mustn't be null.");
        }
        if(msg.isEmpty()){
            throw new Exception("Message mustn't be empty.");
        }

        String randomKey = createRandomKey(msg.length());
        String encryptedMsg = applyVigerene(msg,randomKey,true);
        char lengthKey = (char) randomKey.length();
        //Encrypted message + randomKey + lengthCastedToASCIICharValue
        return encryptedMsg+randomKey+lengthKey;
    }

    private static String applyVigerene(String msg,String key, boolean forEncrypting){

        int encryptActive = forEncrypting?1:-1;

        int A_VALUE_BITWISE_SHIFT = 1*encryptActive;
        int B_VALUE_BITWISE_SHIFT = 2*encryptActive;
        int C_VALUE_BITWISE_SHIFT = 5*encryptActive;

        int msgLength = msg.length();
        int keyLength = key.length();
        //char[][] encryptMatrix = new char[2][msgLength>keyLength?msgLength:keyLength];
//        int columnCount = msgLength;//Math.max(msgLength, keyLength);
        char[][] encryptMatrix = new char[2][msgLength];
        int indexKey = 0;

        for(int i = 0; i<msgLength;i++){
            encryptMatrix[0][i] = msg.charAt(i);
            encryptMatrix[1][i] = key.charAt(indexKey);
            indexKey++;
            if(indexKey>=keyLength)
                indexKey = 0;
        }

        StringBuilder encryptedMsg = new StringBuilder();
        char currentChar;
        for(int i = 0; i<msgLength;i++){
            currentChar = encryptMatrix[0][i];
            switch (encryptMatrix[1][i]){
                case'a':
                    encryptedMsg.append(Cesar.transform(currentChar+"",A_VALUE_BITWISE_SHIFT));
                    break;
                case'b':
                    encryptedMsg.append(Cesar.transform(currentChar+"",B_VALUE_BITWISE_SHIFT));
                    break;
                case'c':
                    encryptedMsg.append(Cesar.transform(currentChar+"",C_VALUE_BITWISE_SHIFT));
                    break;
            }
        }

        return  encryptedMsg.toString();
    }

    /**
     * Replaced by #applyVigerene function, that simplifies the operation in only
     * one function.
     * */
    @Deprecated
    private static String cifrar(String msg, String key){
        int msgLength = msg.length();
        int keyLength = key.length();
        //char[][] encryptMatrix = new char[2][msgLength>keyLength?msgLength:keyLength];
        int columnCount = msgLength;//Math.max(msgLength, keyLength);
        char[][] encryptMatrix = new char[2][columnCount];
        int indexKey = 0;

        for(int i = 0; i<columnCount;i++){
            encryptMatrix[0][i] = msg.charAt(i);
            encryptMatrix[1][i] = key.charAt(indexKey);
            indexKey++;
            if(indexKey>=keyLength)
                indexKey = 0;
        }

        StringBuilder encryptedMsg = new StringBuilder();
        char currentChar;
        for(int i = 0; i<columnCount;i++){
            currentChar = encryptMatrix[0][i];
            switch (encryptMatrix[1][i]){
                case'a':
                    encryptedMsg.append(Cesar.transform(currentChar+"",1));
                    break;
                case'b':
                    encryptedMsg.append(Cesar.transform(currentChar+"",3));
                    break;
                case'c':
                    encryptedMsg.append(Cesar.transform(currentChar+"",5));
                    break;
            }
        }

        return  encryptedMsg.toString();
    }

    /**
     * Replaced by #applyVigerene function, that simplifies the operation in only
     * one function.
     * */
    @Deprecated
    private static String decifrar(String msg,String key) throws Exception {
        if(msg==null){
            throw new Exception("Message mustn't be null.");
        }
        if(msg.isEmpty()){
            throw new Exception("Message mustn't be empty.");
        }

        int msgLength = msg.length();
        int keyLength = key.length();
        //char[][] encryptMatrix = new char[2][msgLength>keyLength?msgLength:keyLength];
        int columnCount = msgLength;//Math.max(msgLength, keyLength);
        char[][] encryptMatrix = new char[2][columnCount];
        int indexKey = 0;

        for(int i = 0; i<columnCount;i++){
            encryptMatrix[0][i] = msg.charAt(i);
            encryptMatrix[1][i] = key.charAt(indexKey);
            indexKey++;
            if(indexKey>=keyLength)
                indexKey = 0;
        }

        StringBuilder encryptedMsg = new StringBuilder();
        char currentChar;
        for(int i = 0; i<columnCount;i++){
            currentChar = encryptMatrix[0][i];
            switch (encryptMatrix[1][i]){
                case'a':
                    encryptedMsg.append(Cesar.transform(currentChar+"",-1));
                    break;
                case'b':
                    encryptedMsg.append(Cesar.transform(currentChar+"",-3));
                    break;
                case'c':
                    encryptedMsg.append(Cesar.transform(currentChar+"",-5));
                    break;
            }
        }

        return  encryptedMsg.toString();


    }

    /**
     *
     * @param msgEncrypted
     * @return
     * Array with two elements in it:
     * [0] The message encrypted.
     * [1] The key for descrypting.
     *
     * null if anything else.
     */
    private static String[] separateMessageAndKey(String msgEncrypted){
        int encryptedLength = msgEncrypted.length();
        int keyStringLength = msgEncrypted.charAt(encryptedLength-1);//extract the last byte

        msgEncrypted = msgEncrypted.substring(0,encryptedLength-1);
        encryptedLength = msgEncrypted.length();

        int startPositionKey = encryptedLength-keyStringLength;
        String key = msgEncrypted.substring(startPositionKey);
        String msg = msgEncrypted.substring(0,startPositionKey);//inclusivo/exclusivo

        return new String[]{msg,key};
    }

    private static String createRandomKey(int lengtMsg){

        Random random = new Random(Calendar.getInstance().getTimeInMillis());
        int keyLength = random.nextInt(lengtMsg)+1;//número entre 0 y length
        keyLength = keyLength==0?1:keyLength;
        keyLength = keyLength>254?random.nextInt(254)+1:keyLength;

        StringBuilder key = new StringBuilder();

        for(int i = 0;i<keyLength;i++){
            key.append(
                    (char)('a' + random.nextInt(3) )
            );//97 98 99
        }

        return key.toString();
    }



}
