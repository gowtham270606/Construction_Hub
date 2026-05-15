package com.gowthamX.constructionHub.features.task;

import com.gowthamX.constructionHub.data.dto.Site;
import com.gowthamX.constructionHub.data.dto.Task;
import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.data.dto.Worker;
import com.gowthamX.constructionHub.util.ConsoleInput;
import com.gowthamX.constructionHub.util.ParseHelper;

import java.util.List;
import java.util.Scanner;

public class TaskView {

    private final TaskModel taskModel;
    private final User user;
    private final Scanner scanner;

    public TaskView(User user) {
        this.taskModel = new TaskModel(this);
        this.user = user;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("Task Management");
            System.out.println("1.View Tasks by Site");
            System.out.println("2.Create New Task");
            System.out.println("3.Update Task Status");
            System.out.println("0.Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": viewTasksBySite(); break;
                case "2": createTask();      break;
                case "3": updateStatus();    break;
                case "0": return;
                default:  System.out.println("  Invalid option. ");
            }
        }
    }

    private void viewTasksBySite() {
        System.out.print("  Enter Site ID: ");
        String siteId = scanner.nextLine();
        taskModel.loadTasksBySite(siteId == null ? null : siteId.trim());
    }

    private void createTask() {
        System.out.println();
        System.out.println("Create New Task ");
        taskModel.loadSites();
        System.out.print("Site ID       : ");
        String siteId = scanner.nextLine();
        taskModel.loadWorkersForSite(null);
        System.out.print("Worker ID (optional, press Enter to skip): ");
        String workerId = scanner.nextLine();
        System.out.print("Description   : ");
        String description = scanner.nextLine();
        System.out.print("Deadline (dd-MM-yyyy, optional): ");
        String deadline = scanner.nextLine();
        taskModel.createTask(
                siteId == null ? null : siteId.trim(),
                workerId == null ? null : workerId.trim(),
                description == null ? null : description.trim(),
                deadline);
    }

    private void updateStatus() {
        System.out.println();
        System.out.print("Enter Task ID: ");
        String taskId = scanner.nextLine();
        System.out.println("New Status:");
        System.out.println("1. PENDING");
        System.out.println("2. IN_PROGRESS");
        System.out.println("3. COMPLETED");
        System.out.print("Choose: ");
        String statusChoice = scanner.nextLine();
        taskModel.updateTaskStatus(
                taskId == null ? null : taskId.trim(),
                statusChoice == null ? null : statusChoice.trim());
    }

    void showSitePicker(List<Site> sites) {
        if (sites.isEmpty()) { System.out.println("  No active sites found."); return; }
        System.out.println("  Active Sites:");
        for (Site s : sites)
            System.out.printf("    ID: %-4d  %s  (%s)%n", s.getId(), s.getName(), s.getLocation());
    }

    void showWorkerPicker(List<Worker> workers) {
        if (workers.isEmpty()) { System.out.println("  No active workers found."); return; }
        System.out.println("  Active Workers:");
        for (Worker w : workers)
            System.out.printf("    ID: %-4d  %-20s  [%s]  ₹%.2f/day%n",
                    w.getId(), w.getName(), w.getType(), w.getDailyWage());
    }

    void showTaskList(List<Task> tasks) {
        System.out.println();
        if (tasks.isEmpty()) { System.out.println("  No tasks found for this site."); return; }
        System.out.println("  ID   Description                    Status       Deadline");
        for (Task t : tasks) {
            System.out.printf("  %-4d %-31s %-12s %s%n",
                    t.getId(),
                    truncate(t.getDescription(), 31),
                    t.getStatus(),
                    ParseHelper.formatDate(t.getDeadline()));
        }
    }

    void onTaskCreated(Task task) {
        System.out.println("  Task created successfully (ID: " + task.getId() + ")");
    }

    void onTaskUpdated(Task task) {
        System.out.println("  Task #" + task.getId() + " status updated to " + task.getStatus());
    }

    void onTaskFailed(String message) {
        System.out.println("  ERROR: " + message);
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
}
