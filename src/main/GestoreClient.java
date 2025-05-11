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

    private Socket client;
    private Vector<GestoreClient> clients;
    private Scanner input;
    private PrintWriter output;
    private int mioIndice;

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

        mioIndice = clients.indexOf(this);
        output.println(ANSI_BLUE + "### Sei connesso come Client[" + mioIndice + "] - Scrivi 'STOP' per uscire ###\n" + ANSI_RESET);

        while (true) {
                if (Server.getTurnoCorrente() == clients.indexOf(this)) {
                    output.println(ANSI_YELLOW + "### E' il tuo turno - Scrivi un messaggio ###" + ANSI_RESET);
                    String messaggio = input.nextLine();

                    if (messaggio.equals("STOP")) {
                        break;
                    }

                    Server.broadcast("Client[" + mioIndice + "]: " + messaggio);
                    Server.passaTurno();
                } else {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }

        try {
            client.close();
        } catch (IOException e) {
            System.err.println("### Errore durante la disconnessione del client ###");
        }

        Server.rimuoviClient(this);
        Server.broadcast(ANSI_PURPLE + "### Client[" + mioIndice + "] si e' disconnesso ###" + ANSI_RESET);

    }

    public void inviaMessaggio(String messaggio) {
        output.println(messaggio);
    }

}
