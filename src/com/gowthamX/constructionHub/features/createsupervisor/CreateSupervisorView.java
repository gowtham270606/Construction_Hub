package com.gowthamX.constructionHub.features.createsupervisor;

import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.util.ConsoleInput;

import java.util.Scanner;

public class CreateSupervisorView {

    private final CreateSupervisorModel createSupervisorModel;
    private final Scanner scanner;

    public CreateSupervisorView() {
        this.createSupervisorModel = new CreateSupervisorModel(this);
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        System.out.println();
        System.out.println("Create Supervisor Account");
        System.out.print("Full Name : ");
        String name = scanner.nextLine();
        System.out.print("Username  : ");
        String username = scanner.nextLine();
        System.out.print("Password  : ");
        String password = scanner.nextLine();
        createSupervisorModel.createSupervisor(
                name == null ? null : name.trim(),
                username == null ? null : username.trim(),
                password);
    }

    void onCreateSuccessful(User user) {
        System.out.println("Supervisor account created!");
        System.out.println("Name    : " + user.getName());
        System.out.println("Username: " + user.getUsername());
    }

    void onCreateFailed(String message) {
        System.out.println("ERROR: " + message);
    }
}
