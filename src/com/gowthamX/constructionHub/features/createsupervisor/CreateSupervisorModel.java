package com.gowthamX.constructionHub.features.createsupervisor;

import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.data.repository.ConstructionDB;

class CreateSupervisorModel {

    private final CreateSupervisorView createSupervisorView;

    CreateSupervisorModel(CreateSupervisorView createSupervisorView) {
        this.createSupervisorView = createSupervisorView;
    }

    String validateName(String name) {
        if (name == null || name.trim().isEmpty()) return "Name cannot be empty";
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

    void createSupervisor(String name, String username, String password) {
        String nameError = validateName(name);
        if (nameError != null) { createSupervisorView.onCreateFailed(nameError); return; }

        String usernameError = validateUsername(username);
        if (usernameError != null) { createSupervisorView.onCreateFailed(usernameError); return; }

        String passwordError = validatePassword(password);
        if (passwordError != null) { createSupervisorView.onCreateFailed(passwordError); return; }

        User supervisor = new User();
        supervisor.setName(name.trim());
        supervisor.setUsername(username.trim());
        supervisor.setPassword(password);
        supervisor.setRole(User.Role.SUPERVISOR);

        User saved = ConstructionDB.getInstance().addUser(supervisor);
        if (saved == null) {
            createSupervisorView.onCreateFailed("Could not create account. Please try again.");
            return;
        }
        createSupervisorView.onCreateSuccessful(saved);
    }
}
