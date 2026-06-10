package com.gowthamX.constructionHub.features.signin;

import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.features.home.HomeView;
import com.gowthamX.constructionHub.util.ConsoleInput;

import java.util.Scanner;

public class SignInView {

    private final SignInModel signInModel;
    private final Scanner scanner;
    private boolean authenticated;

    public SignInView() {
        this.signInModel = new SignInModel(this);
        this.scanner = ConsoleInput.getScanner();
        this.authenticated = false;
    }

    public void     init() {
        System.out.println();
        System.out.println("Construction Site Management System");
        System.out.println("Sign in to ConstructHub");
        while (!authenticated) {
            inputAuthenticate();
            if (authenticated) return;
            if (!postFailureAction()) return;
        }
    }

    private void inputAuthenticate() {
        System.out.print("  Username : ");
        String username = scanner.nextLine();
        System.out.print("  Password : ");
        String password = scanner.nextLine();
        signInModel.authenticate(username==null?null:username.trim(),password);
    }

    private boolean postFailureAction() {
        while (true) {
            System.out.println();
            System.out.println("  1. Retry");
            System.out.println("  2. Exit");
            System.out.print("  Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    return true;
                case "2":
                    System.out.println("  Thank you for!");
                    System.exit(0);
                    return false;
                default:
                    System.out.println("  Invalid option. Please try again.");
            }
        }
    }

    void onSignInSuccessful(User user) {
        authenticated = true;
        System.out.println("  Welcome, " + user.getName() + " " + user.getRole() + "");
        new HomeView(user).init();
    }

    void onSignInFailed(String message) {
        System.out.println("  ERROR: " + message);
    }
}
