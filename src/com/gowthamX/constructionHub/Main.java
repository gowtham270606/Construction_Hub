package com.gowthamX.constructionHub;

import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.data.repository.ConstructionDB;
import com.gowthamX.constructionHub.features.signin.SignInView;
import com.gowthamX.constructionHub.util.ConsoleInput;

import java.util.Scanner;

public class Main {

    public static final int VERSION_NO = 1;
    public static final String VERSION_NAME = "1.0.0";
    private static final String DEFAULT_OWNER_USERNAME = "gk";
    private static final String DEFAULT_OWNER_PASSWORD = "270606";
    private static final String DEFAULT_OWNER_NAME = "Gowtham";

    public static void main(String[] args) {
        setDefaultOwner();
        System.out.println("Construction Site Management System      ");
        System.out.println("Version " + VERSION_NAME);
        showLandingMenu();
    }

    private static void setDefaultOwner() {
        if (!ConstructionDB.getInstance().hasUsers()) {
            User owner = new User();
            owner.setName(DEFAULT_OWNER_NAME);
            owner.setUsername(DEFAULT_OWNER_USERNAME);
            owner.setPassword(DEFAULT_OWNER_PASSWORD);
            owner.setRole(User.Role.OWNER);
            ConstructionDB.getInstance().addUser(owner);
        }
    }

    private static void showLandingMenu() {
        Scanner scanner = ConsoleInput.getScanner();
        while (true) {
            System.out.println();
            System.out.println("1. Sign In");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    new SignInView().init();
                    break;
                case "2":
                    System.out.println("Thank you .");
                    return;
                default:
                    System.out.println("Invalid option. ");
            }
        }
    }
}
