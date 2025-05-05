package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static InetAddress host;
    private static final int PORTA = 5959;
    private static String username;

    public static void main(String[] args) {

        //selezioneUsername();
        hostSetup();
        contattaServer();
    }

    private static void selezioneUsername() {

        Scanner usernameInput = new Scanner(System.in);

        System.out.print("Inserisci il tuo username (tutto maiuscolo): ");
        username = usernameInput.nextLine().toUpperCase();

        System.out.println("\n=======================\n\n");
    }

    private static void hostSetup() {

        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("### ID host non trovato... ###");
            System.exit(1);
        }
    }

    private static void contattaServer() {

        Socket connessione = null;

        try {

            connessione = new Socket(host, PORTA);

            Scanner input = new Scanner(connessione.getInputStream());
            PrintWriter output = new PrintWriter(connessione.getOutputStream(), true);

            Scanner tastiera = new Scanner(System.in);

            String messaggio;
            String risposta;
            do {

                //System.out.print("<< ");
                messaggio = tastiera.nextLine();
                output.println(messaggio);

                risposta = input.nextLine();
                //System.out.println(">> " + risposta);
                System.out.println(risposta);

            } while (!messaggio.equals("STOP"));

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {
                System.out.println("\n### Chiusura connessione in corso... ###");
                connessione.close();
                System.out.println("### Connessione chiusa con successo! ###\n");
            } catch (IOException e) {
                System.out.println("### Impossibile chiudere la connessione! ###");
                System.exit(1);
            }
        }
    }

}
