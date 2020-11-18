package com.example.pkiapp;

import java.time.LocalDateTime;
import java.util.Random;

public class Transposicion {


    public static String cifrar(String msj,boolean simetrico){

        int lengthMessage = msj.length();
        int matrixRows = 0;
        int matrixColumns = 0;
        if(simetrico){
            matrixRows = getRootSquareNumber(lengthMessage);
            matrixColumns = matrixRows;
        }else{
            int[] randomNumbers = getRandomNumbersMultipliedLargerThanMsg(lengthMessage);
            matrixRows = randomNumbers[0];
            matrixColumns = randomNumbers[1];
        }

        return  matrixRows +"|" +matrixColumns +"°" + msj.length() +"&" + cifrar(msj,matrixRows,matrixColumns);

    }

    private static String cifrar(String mensaje,int matrixRows, int matrixColumns){
        char [][] matrixCalculate;
        matrixCalculate = new char[matrixRows][matrixColumns];
        int stringIndex = 0;
        int lengthMessage = mensaje.length();

        for(int rowIndex = 0;rowIndex<matrixRows; rowIndex++){
            for(int columnIndex = 0;columnIndex<matrixColumns; columnIndex++){

                stringIndex = (rowIndex*matrixColumns) + columnIndex;
                char charN =
                        stringIndex>lengthMessage-1?getRandomChar():mensaje.charAt(stringIndex);

                matrixCalculate[rowIndex][columnIndex] = charN;

            }
        }

        StringBuilder mensajeCifrado = new StringBuilder();
        for(int columnIndex = 0;columnIndex<matrixColumns; columnIndex++) {
            for(int rowIndex = 0;rowIndex<matrixRows; rowIndex++){
                mensajeCifrado.append(matrixCalculate[rowIndex][columnIndex]);
            }
        }

        return mensajeCifrado.toString();

    }

    public static String decifrar(String msg) throws Exception {
        //from 0 to ->
        int matrixRowIndexEnd = msg.indexOf("|");
        //from matrixRowIndexEnd +1 to ->
        int matrixColumnIndexEnd = msg.indexOf("°");
        //from matrixColumnIndexEnd +1 to ->
        int lengthMessageIndex =  msg.indexOf("&");
        //encrypted message starts at lengthMessageIndex +1

        if(matrixRowIndexEnd == -1 ||matrixColumnIndexEnd == -1||lengthMessageIndex == -1){
            throw new Exception(
                    "Corrupted Message, can't be decrypted .",
                    new Throwable("Insufficient data for decrypting the message.")
            );
        }
        int matrixRowsCount = Integer.parseInt(msg.substring(0,matrixRowIndexEnd));
        int matrixColumnsCount = Integer.parseInt(msg.substring(matrixRowIndexEnd+1,matrixColumnIndexEnd));
        int lengthMessage = Integer.parseInt(msg.substring(matrixColumnIndexEnd+1,lengthMessageIndex));
        String messageEncrypted = msg.substring(lengthMessageIndex+1);



        return decifrar(messageEncrypted,matrixRowsCount,matrixColumnsCount,lengthMessage);

    }

    private static String decifrar(String messageEncrypted, int matrixRowsCount, int matrixColumnsCount, int lengthMessage) {
        char [][] matrixCalculate;
        matrixCalculate = new char[matrixRowsCount][matrixColumnsCount];
        int stringIndex = messageEncrypted.length()-1;

        for(int columnIndex = matrixColumnsCount-1;columnIndex > -1; columnIndex--) {
            for(int rowIndex = matrixRowsCount-1;rowIndex > -1; rowIndex--){


                matrixCalculate[rowIndex][columnIndex] = messageEncrypted.charAt(stringIndex);
//                mensajeCifrado.append(matrixCalculate[rowIndex][columnIndex]);
                stringIndex--;/*(rowIndex*matrixColumns) + columnIndex;*/

            }
        }
        //read the original lengh from rows left to right , from up to down
        stringIndex = 0;
        StringBuilder mensajeCifrado = new StringBuilder();
        boolean breakLoop = false;
        for(int rowIndex = 0;rowIndex< matrixRowsCount&& !breakLoop; rowIndex++) {
            for(int columnIndex = 0; columnIndex < matrixColumnsCount &&!breakLoop; columnIndex++) {

                mensajeCifrado.append(matrixCalculate[rowIndex][columnIndex]);
                if(stringIndex==lengthMessage-1){
                    breakLoop = true;
                }
                stringIndex++;

            }
        }


        return mensajeCifrado.toString();

    }

    private static int[] getRandomNumbersMultipliedLargerThanMsg(int lengthMessage) {

        Random random = new Random(System.currentTimeMillis());
        /*
        Numbers smaller than lengthMessage
        but multiplied larger than lenght
        */
        int random1 = random.nextInt(lengthMessage)+1;
        int random2 = random.nextInt(lengthMessage)+1;

        while(random1*random2<lengthMessage){
            random1 = random.nextInt(lengthMessage)+1;
            random2 = random.nextInt(lengthMessage)+1;
        }


        return new int[]{random1,random2};
    }
    private static int getRootSquareNumber(int number) {

        int result = -1;
        for(int i = 0; i<number; i++){

            result = i*i;

            if(result>number){
                return i;
            }
        }

        return result;
    }
    private static char getRandomChar(){
        Random random = new Random();
//        return (char)(random.nextInt('z' - 'a') + 'a');
        //from ASCII 20 to ASCII 254
        char resultChar =(char) (random.nextInt(254-20)+20);

        while(resultChar=='|' || resultChar=='°' || resultChar =='&' ){
            resultChar =(char) (random.nextInt(254-20)+20);
        }
        return resultChar;

    }



}
