package com.gowthamX.constructionHub.features.home;

import com.gowthamX.constructionHub.data.dto.User;

class HomeModel {

    private final HomeView homeView;

    HomeModel(HomeView homeView) {
        this.homeView = homeView;
    }

    void init(User user) {
        if (user == null || user.getRole() == null) {
            homeView.showUnauthorized();
            return;
        }
        if (user.getRole() == User.Role.OWNER) {
            homeView.showOwnerMenu();
        } else {
            homeView.showSupervisorMenu();
        }
    }
}
