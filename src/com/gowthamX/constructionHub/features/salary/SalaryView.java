package com.gowthamX.constructionHub.features.salary;

import com.gowthamX.constructionHub.data.dto.Salary;
import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.data.dto.Worker;
import com.gowthamX.constructionHub.util.ConsoleInput;
import com.gowthamX.constructionHub.util.ParseHelper;

import java.util.List;
import java.util.Scanner;

public class SalaryView {

    private final SalaryModel salaryModel;
    private final User user;
    private final Scanner scanner;

    public SalaryView(User user) {
        this.salaryModel = new SalaryModel(this);
        this.user = user;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("Salary & Reports");
            System.out.println("1. Calculate & Pay Weekly Salary");
            System.out.println("2. View Salary History by Worker");
//            System.out.println("3. View All Salary Records");
            System.out.println("0. Back");
            System.out.print("  Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": paySalary();           break;
                case "2": salaryByWorker();      break;
                case "3": salaryModel.viewAllSalaries(); break;
                case "0": return;
                default:  System.out.println("  Invalid option. Please try again.");
            }
        }
    }

    private void paySalary() {
        System.out.println();
        System.out.println("Calculate Weekly Salary");
        System.out.print("Worker ID              : ");
        String workerId = scanner.nextLine();
        System.out.print("Site ID                : ");
        String siteId = scanner.nextLine();
        System.out.print("Week Start (dd-MM-yyyy): ");
        String weekStart = scanner.nextLine();
        System.out.print("Week End   (dd-MM-yyyy): ");
        String weekEnd = scanner.nextLine();
        salaryModel.calculateAndPaySalary(
                workerId == null ? null : workerId.trim(),
                siteId == null ? null : siteId.trim(),
                weekStart == null ? null : weekStart.trim(),
                weekEnd == null ? null : weekEnd.trim());
    }

    private void salaryByWorker() {
        System.out.print("Worker ID: ");
        String workerId = scanner.nextLine();
        salaryModel.viewSalariesByWorker(workerId == null ? null : workerId.trim());
    }

    void showSalaryList(Worker worker, List<Salary> salaries) {
        System.out.println();
        System.out.println("Salary history for: " + worker.getName()
                + "" + worker.getWorkerId() + " Daily: " + worker.getDailyWage());
        if (salaries.isEmpty()) { System.out.println("  No salary records found."); return; }
        System.out.println("ID   Week Start   Week End     Days  Total Amount");
        for (Salary s : salaries)
            System.out.printf("  %-4d %-12s %-12s %-5d ₹%.2f%n",
                    s.getId(),
                    ParseHelper.formatDate(s.getWeekStart()),
                    ParseHelper.formatDate(s.getWeekEnd()),
                    s.getTotalDays(),
                    s.getTotalAmount());
    }

    void showAllSalaries(List<Salary> salaries) {
        System.out.println();
        if (salaries.isEmpty()) { System.out.println("No salary records found."); return; }
        System.out.println("ID   Worker ID  Site ID  Week Start   Week End     Days  Total Amount");
        for (Salary s : salaries)
            System.out.printf(""+""+"" +"" +"",
                    s.getId(),
                    s.getWorkerId(),
                    s.getSiteId(),
                    ParseHelper.formatDate(s.getWeekStart()),
                    ParseHelper.formatDate(s.getWeekEnd()),
                    s.getTotalDays(),
                    s.getTotalAmount());
    }

    void onSalaryPaid(Worker worker, Salary salary) {
        System.out.println("Salary Paid!");
        System.out.println("Worker      : " + worker.getName() + " [" + worker.getWorkerId() + "]");
        System.out.println("Period      : " + ParseHelper.formatDate(salary.getWeekStart())
                + " to " + ParseHelper.formatDate(salary.getWeekEnd()));
        System.out.println("Days Present: " + salary.getTotalDays());
        System.out.printf("Total Amount: ", salary.getTotalAmount());
    }

    void onSalaryFailed(String message) {
        System.out.println("  ERROR: " + message);
    }
}
