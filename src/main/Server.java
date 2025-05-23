package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/*

colorare gli output sul terminale
https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println/5762502#5762502

- rosso = errore (dato da System.err)
- viola = messaggi di info del server
- blu = messaggi di info dal gestore client
- ciano = nuovo client connesso o disconnesso
- giallo = messaggio di avviso del proprio turno
- bianco = messaggi inviati dai client

 */

public class Server {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private static ServerSocket server;
    private static final int PORTA = 5959;
    private static Vector<GestoreClient> clients = new Vector<>();
    private static int clientConnessi = 0;

    public static void main(String[] args) {

        System.out.println(ANSI_PURPLE + "### Apertura porta del server in corso... ###" + ANSI_RESET);
        try {
            server = new ServerSocket(PORTA);
        } catch (IOException e) {
            System.err.println("### Impossibile aprire la porta! ###");
            System.exit(1);
        }

        System.out.println(ANSI_PURPLE + "### Porta aperta con successo! ###\n" + ANSI_RESET);

        Socket connessione;
        boolean primaConnessione = true;

        while (true) {
            try {
                connessione = server.accept();
                GestoreClient nuovoClient = new GestoreClient(connessione, clients);
                clients.add(nuovoClient);
                clientConnessi++;
                System.out.println(ANSI_CYAN + "### Nuovo client connesso ###" + ANSI_RESET);
                System.out.println(ANSI_PURPLE + "### Client connessi --> " + clientConnessi + " ###\n" + ANSI_RESET);
                if (clientConnessi == 1 && primaConnessione) {
                    System.out.println(ANSI_PURPLE + "### Chatroom avviata! ###\n" + ANSI_RESET);
                    primaConnessione = false;
                }
            } catch (IOException e) {
                System.err.println("### Errore durante l'esecuzione del server ###");
            }
        }

    }

    public static synchronized void broadcast(String messaggio, GestoreClient trasmettitore) {
        for (GestoreClient client : clients) {
            if (client != trasmettitore) {
                client.inviaMessaggio(messaggio);
            }
        }
    }

    public static synchronized void rimuoviClient(GestoreClient client) {
        clients.remove(client);
        clientConnessi--;
        System.out.println(ANSI_CYAN + "### " + client.getUsername() + " si e' disconnesso ###\n" + ANSI_RESET);
        System.out.println(ANSI_PURPLE + "### Client connessi --> " + clientConnessi + " ###\n" + ANSI_RESET);
    }

}
