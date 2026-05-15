package com.gowthamX.constructionHub.features.signup;

import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.util.ConsoleInput;

import java.util.Scanner;

public class SignUpView {

    private final SignUpModel signUpModel;
    private final Scanner scanner;

    public SignUpView() {
        this.signUpModel = new SignUpModel(this);
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        System.out.println();
        System.out.println("Create New Account");
        Register();
    }

    private void Register() {
        System.out.print("Full Name              : ");
        String name = scanner.nextLine();
        System.out.print("Username               : ");
        String username = scanner.nextLine();
        System.out.print("Password               : ");
        String password = scanner.nextLine();
        System.out.print("Confirm Password       : ");
        String confirmPassword = scanner.nextLine();
        System.out.print("Role (OWNER/SUPERVISOR): ");
        String roleInput = scanner.nextLine();
        User.Role role = signUpModel.parseRole(roleInput);
        signUpModel.register(
                name == null ? null : name.trim(),
                username == null ? null : username.trim(),
                password,
                confirmPassword,
                role);
    }

    void onSignUpSuccessful(User user) {
        System.out.println("  Account created successfully!");
        System.out.println("  Welcome, " + user.getName() + "sign in.");
    }

    void onSignUpFailed(String message) {
        System.out.println("  ERROR: " + message);
    }
}
