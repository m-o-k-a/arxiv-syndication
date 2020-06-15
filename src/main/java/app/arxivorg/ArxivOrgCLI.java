package app.arxivorg;

import app.arxivorg.controller.ArxivOrgCLIController;

import java.util.Scanner;

public class ArxivOrgCLI {
    public static void main(String[] args) {
        ArxivOrgCLIController controller = new ArxivOrgCLIController();
        System.out.println("Welcome to the arXiv organizer!");
        Scanner scanner;

        for(;;) {
            System.out.println("Input your command: ");
            scanner = new Scanner(System.in);
            if(controller.readCommand(scanner.nextLine()) == -1) break;
        }
        scanner.close();
    }
}
