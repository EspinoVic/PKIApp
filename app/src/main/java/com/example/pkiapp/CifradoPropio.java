package com.example.pkiapp;

import java.util.Random;

public class CifradoPropio  {

    public static String cifrar(String msg){
        //Paso 1
        StringBuilder cadenaCifrada = new StringBuilder();
        cadenaCifrada.append(Transposicion.cifrar(msg,false));
        //Paso 2
        int random = getRandomPrimo(10,255);
        cadenaCifrada= new StringBuilder( Cesar.encriptar(cadenaCifrada.toString(),random) );
        cadenaCifrada.append ((char)random);

        //Paso 3, Resta 0 y par, Suma impares
        cadenaCifrada = new StringBuilder(changeValuesPerPositionEncrypt(cadenaCifrada.toString()));
        return  cadenaCifrada.toString();
    }

    public static String decifrar(String msg){

        //Paso 1,
        String cadenaDecifrada = changeValuesPerPositionDecrypt(msg);
        //Paso 2
        int length = cadenaDecifrada.length();
        int randomPrimo = cadenaDecifrada.charAt(length-1);
        cadenaDecifrada = cadenaDecifrada.substring(0,length-1);
        cadenaDecifrada = Cesar.desencriptar(cadenaDecifrada,randomPrimo);
        //paso 3
        try {
            cadenaDecifrada = Transposicion.decifrar(cadenaDecifrada);
        } catch (Exception e) {
            e.printStackTrace();
            cadenaDecifrada = "error";
        }


        return cadenaDecifrada;
    }

    private static String changeValuesPerPositionEncrypt(String msg){
        char charActual ;
        StringBuilder newMsg = new StringBuilder();
        for (int indexString = 0; indexString<msg.length();indexString++){
            charActual = msg.charAt(indexString);
            if(indexString%2 == 0){
                newMsg.append(Cesar.transform(charActual+"",indexString));
            }else{
                newMsg.append(Cesar.transform(charActual+"",indexString*-1));

            }
        }
        return newMsg.toString();
    }

    private static String changeValuesPerPositionDecrypt(String msg) {
        char charActual ;
        StringBuilder newMsg = new StringBuilder();
        for (int indexString = 0; indexString<msg.length();indexString++){
            charActual = msg.charAt(indexString);
            if(indexString%2 == 0){
                newMsg.append(Cesar.transform(charActual+"",indexString*-1));
            }else{
                newMsg.append(Cesar.transform(charActual+"",indexString));
            }
        }
        return newMsg.toString();
    }

    private static int getRandomPrimo(int n1, int n2){
        Random random = new Random();
        int randomGot = random.nextInt(n2) + n1;
        while(!esPrimo(randomGot)&&randomGot<n2-1){
            randomGot = random.nextInt(n2) + n1;
        }
        return randomGot;
    }

    private static boolean esPrimo(int numero){
        int contador = 2;
        boolean primo=true;
        while ((primo) && (contador!=numero)){
            if (numero % contador == 0)
                primo = false;
            contador++;
        }
        return primo;
    }

}
