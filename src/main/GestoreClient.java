package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class GestoreClient extends Thread {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private String username;
    private Socket client;
    private Vector<GestoreClient> clients;
    private Scanner input;
    private PrintWriter output;

    public GestoreClient(Socket connessione, Vector<GestoreClient> clients) {

        this.client = connessione;
        this.clients = clients;
        try {
            input = new Scanner(client.getInputStream());
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }

    public void run() {

        output.println(ANSI_BLUE + "Inserisci il tuo username (tutto maiuscolo):" + ANSI_RESET);
        username = input.nextLine().trim().toUpperCase();

        Server.broadcast(ANSI_PURPLE + "### " + username + " si è unito alla chat ###\n" + ANSI_RESET);

        output.println(ANSI_BLUE + "### Benvenuto nella chatroom, " + username + " - Scrivi 'STOP' per uscire ###" + ANSI_RESET);

        while (true) {
            try {
                if (input.hasNextLine()) {
                    String messaggio = input.nextLine();
                    if (messaggio.equals("STOP")) {
                        break;
                    }
                    Server.broadcast(username + ": " + messaggio);
                }
            } catch (Exception e) {
                break;
            }
        }

        try {
            client.close();
        } catch (IOException e) {
            System.err.println("### Errore durante la disconnessione del client ###");
        }

        Server.rimuoviClient(this);
        Server.broadcast(ANSI_PURPLE + "### " + username + " si e' disconnesso ###" + ANSI_RESET);

    }

    public void inviaMessaggio(String messaggio) {
        output.println(messaggio);
    }

    public String getUsername() {
        return username;
    }

}
