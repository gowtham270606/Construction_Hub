package com.gowthamX.constructionHub.features.site;

import com.gowthamX.constructionHub.data.dto.Site;
import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.util.ConsoleInput;
import com.gowthamX.constructionHub.util.ParseHelper;

import java.util.List;
import java.util.Scanner;

public class SiteView {

    private final SiteModel siteModel;
    private final User user;
    private final Scanner scanner;

    public SiteView(User user) {
        this.siteModel = new SiteModel(this);
        this.user = user;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("Site Management");
            System.out.println("1.View All Sites");
            System.out.println("2.Add New Site");
            if (user.getRole() == User.Role.OWNER) {
                System.out.println("3.Mark Site as Completed");
            }
            System.out.println("0.Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": siteModel.loadAllSites(); break;
                case "2":
                    if (user.getRole() == User.Role.OWNER) addSite();
                    else System.out.println("Permission denied.");
                    break;
                case "3":
                    if (user.getRole() == User.Role.OWNER) markCompleted();
                    else System.out.println("Invalid option.");
                    break;
                case "0": return;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    private void addSite() {
        System.out.println();
        System.out.println("Add New Site");
        System.out.print("Site Name     : ");
        String name = scanner.nextLine();
        System.out.print("Location      : ");
        String location = scanner.nextLine();
        System.out.print("Start Date (dd-MM-yyyy): ");
        String startDate = scanner.nextLine();
        System.out.print("End Date   (optional): ");
        String endDate = scanner.nextLine();
        siteModel.addSite(
                name == null ? null : name.trim(),
                location == null ? null : location.trim(),
                startDate,
                endDate);
    }

    private void markCompleted() {
        System.out.println();
        System.out.print(" Enter Site ID to mark as completed: ");
        String id = scanner.nextLine();
        siteModel.markSiteCompleted(id == null ? null : id.trim());
    }

    void showSiteList(List<Site> sites) {
        System.out.println();
        if (sites.isEmpty()) {
            System.out.println("No sites found.");
            return;
        }
        System.out.println("  ID   Name                 Location             Status      Start Date   End Date");
        for (Site s : sites) {
            System.out.printf("  %-4d %-21s %-21s %-10s  %-11s  %s%n",
                    s.getId(),
                    truncate(s.getName(), 21),
                    truncate(s.getLocation(), 21),
                    s.getStatus(),
                    ParseHelper.formatDate(s.getStartDate()),
                    ParseHelper.formatDate(s.getEndDate()));
        }
    }

    void onSiteAdded(Site site) {
        System.out.println("Site '" + site.getName() + "' added successfully ID: " + site.getId() + ")");
    }

    void onSiteUpdated(Site site) {
        System.out.println("Site '" + site.getName() + "' marked as " + site.getStatus());
    }

    void onSiteFailed(String message) {
        System.out.println("  ERROR: " + message);
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
