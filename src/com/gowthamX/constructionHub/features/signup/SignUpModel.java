package com.gowthamX.constructionHub.features.signup;

import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.data.repository.ConstructionDB;

class SignUpModel {

    private final SignUpView signUpView;

    SignUpModel(SignUpView signUpView) {
        this.signUpView = signUpView;
    }

    String validateName(String name) {
        if (name == null || name.trim().isEmpty()) return "Full name cannot be empty";
        return null;
    }

    String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) return "Username cannot be empty";
        if (username.trim().length() < 3) return "Username must be at least 3 characters";
        if (ConstructionDB.getInstance().getUserByUsername(username) != null)
            return "Username '" + username.trim() + "' is already taken";
        return null;
    }

    String validatePassword(String password) {
        if (password == null || password.isEmpty()) return "Password cannot be empty";
        if (password.length() < 6) return "Password must be at least 6 characters";
        return null;
    }

    String validateConfirmPassword(String password, String confirm) {
        if (confirm == null || confirm.isEmpty()) return "Please confirm your password";
        if (!confirm.equals(password)) return "Passwords do not match";
        return null;
    }

    User.Role parseRole(String input) {
        if (input == null) return null;
        String t = input.trim().toUpperCase();
        if (t.equals("OWNER"))      return User.Role.OWNER;
        if (t.equals("SUPERVISOR")) return User.Role.SUPERVISOR;
        return null;
    }

    void register(String name, String username, String password,
                  String confirmPassword, User.Role role) {
        String nameError = validateName(name);
        if (nameError != null) { signUpView.onSignUpFailed(nameError); return; }

        String usernameError = validateUsername(username);
        if (usernameError != null) { signUpView.onSignUpFailed(usernameError); return; }

        String passwordError = validatePassword(password);
        if (passwordError != null) { signUpView.onSignUpFailed(passwordError); return; }

        String confirmError = validateConfirmPassword(password, confirmPassword);
        if (confirmError != null) { signUpView.onSignUpFailed(confirmError); return; }

        if (role == null) { signUpView.onSignUpFailed("Invalid role. Enter OWNER or SUPERVISOR"); return; }

        User user = new User();
        user.setName(name.trim());
        user.setUsername(username.trim());
        user.setPassword(password);
        user.setRole(role);

        User saved = ConstructionDB.getInstance().addUser(user);
        if (saved == null) {
            signUpView.onSignUpFailed("Could not create account. Please try again.");
            return;
        }
        signUpView.onSignUpSuccessful(saved);
    }
}
