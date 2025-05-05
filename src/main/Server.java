package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket server;
    private static final int PORTA = 5959;

    public static void main(String[] args) {

        System.out.println("\n### Apertura porta del server in corso... ###");
        try {
            server = new ServerSocket(PORTA);
        } catch (IOException e) {
            System.out.println("### Impossibile aprire la porta! ###");
            System.exit(1);
        }

//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println("### Porta aperta con successo! ###\n");

        // NEL CICLO INFINITO VENIVA CREATO OGNI VOLTA UN ARRAY DI CLIENT NUOVO VUOTO
//        do {
//            gestioneClient();
//        } while (true);

        Socket connessione = null;
        int clientConnessi = 0;
        GestoreClient[] clients = new GestoreClient[2];

        System.out.println("### In attesa di 2 client per avviare la chat... ###");
        while (clientConnessi != 2) {
            try {
                connessione = server.accept();
                clients[clientConnessi] = new GestoreClient(connessione, clients);
                clientConnessi++;
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private static void gestioneClient() {

        Socket connessione = null;
        int clientConnessi = 0;
        GestoreClient[] clients = new GestoreClient[2];

        System.out.println("### In attesa di 2 client per avviare la chat... ###");
        while (clientConnessi != 2) {
            try {
                connessione = server.accept();
                clients[clientConnessi] = new GestoreClient(connessione, clients);
                clientConnessi++;
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
