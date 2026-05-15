package com.gowthamX.constructionHub.features.signin;

import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.data.repository.ConstructionDB;

class SignInModel {

    private final SignInView signInView;

    SignInModel(SignInView signInView) {
        this.signInView = signInView;
    }

    String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) return "Username cannot be empty";
        return null;
    }

    String validatePassword(String password) {
        if (password == null || password.isEmpty()) return "Password cannot be empty";
        return null;
    }

    void authenticate(String username, String password) {
        String usernameError = validateUsername(username);
        if (usernameError != null) {
            signInView.onSignInFailed(usernameError);
            return;
        }




        String passwordError = validatePassword(password);
        if (passwordError != null) {
            signInView.onSignInFailed(passwordError);
            return;
        }

        User user = ConstructionDB.getInstance().authenticate(username, password);
        if (user == null) {
            signInView.onSignInFailed("Invalid username or password");
            return;
        }
        signInView.onSignInSuccessful(user);
    }
}
