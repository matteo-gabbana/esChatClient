package main;

import java.util.Scanner;

public class Ricezione extends Thread {

    Scanner input;

    public Ricezione(Scanner input) {

        this.input = input;
        this.start();
    }

    public void run() {
        while (true) {
            if (input.hasNextLine()) {
                String messaggioRicevuto = input.nextLine();
                System.out.println(messaggioRicevuto);
            }
        }
    }

}
