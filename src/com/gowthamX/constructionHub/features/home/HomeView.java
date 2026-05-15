package com.gowthamX.constructionHub.features.home;

import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.features.attendance.AttendanceView;
import com.gowthamX.constructionHub.features.createsupervisor.CreateSupervisorView;
import com.gowthamX.constructionHub.features.salary.SalaryView;
import com.gowthamX.constructionHub.features.site.SiteView;
import com.gowthamX.constructionHub.features.task.TaskView;
import com.gowthamX.constructionHub.features.worker.WorkerView;
import com.gowthamX.constructionHub.util.ConsoleInput;

import java.util.Scanner;

public class HomeView {

    private final HomeModel homeModel;
    private final User user;
    private final Scanner scanner;

    public HomeView(User user) {
        this.homeModel = new HomeModel(this);
        this.user = user;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        homeModel.init(user);
    }

    void showUnauthorized() {
        System.out.println("Contact your administrator.");
    }

    void showOwnerMenu() {
        while (true) {
            System.out.println();
            System.out.println("Owner Dashboard, " + user.getName());
            System.out.println("1. Manage Sites");
            System.out.println("2. Manage Workers");
            System.out.println("3. Manage Tasks");
            System.out.println("4. Mark Attendance");
            System.out.println("5. Salary & Reports");
            System.out.println("6. Create Supervisor Account");
            System.out.println("7. Sign Out");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": new SiteView(user).init();break;
                case "2": new WorkerView(user).init();break;
                case "3": new TaskView(user).init();break;
                case "4": new AttendanceView(user).init();break;
                case "5": new SalaryView(user).init();break;
                case "6": new CreateSupervisorView().init();break;
                case "7":
                    System.out.println("Signed out.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    void showSupervisorMenu() {
        while (true) {
            System.out.println();
            System.out.println("Supervisor Dashboard, " + user.getName());
            System.out.println("1. View Sites");
            System.out.println("2. Manage Workers");
            System.out.println("3. Manage Tasks");
            System.out.println("4. Mark Attendance");
            System.out.println("5. Sign Out");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": new SiteView(user).init();break;
                case "2": new WorkerView(user).init();break;
                case "3": new TaskView(user).init();break;
                case "4": new AttendanceView(user).init();break;
                case "5":
                    System.out.println("signed out.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
