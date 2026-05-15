package com.gowthamX.constructionHub.features.worker;

import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.data.dto.Worker;
import com.gowthamX.constructionHub.util.ConsoleInput;

import java.util.List;
import java.util.Scanner;

public class WorkerView {

    private final WorkerModel workerModel;
    private final User user;
    private final Scanner scanner;

    public WorkerView(User user) {
        this.workerModel = new WorkerModel(this);
        this.user = user;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("Worker Management");
            System.out.println("1. View All Workers");
            System.out.println("2. Add New Worker");
            System.out.println("3. Deactivate Worker");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": workerModel.loadAllWorkers(); break;
                case "2": addWorker(); break;
                case "3": deactivateWorker(); break;
                case "0": return;
                default:  System.out.println("Invalid option. ");
            }
        }
    }

    private void addWorker() {
        System.out.println();
        System.out.print("Name           : ");
        String name = scanner.nextLine();
        System.out.print("Type (Mason, Electrician...): ");
        String type = scanner.nextLine();
        System.out.print("Daily Wage: ");
        String wage = scanner.nextLine();
        workerModel.addWorker(
                name == null ? null : name.trim(),
                type == null ? null : type.trim(),
                wage);
    }

    private void deactivateWorker() {
        System.out.println();
        System.out.print("Enter Worker ID to deactivate: ");
        String id = scanner.nextLine();
        workerModel.deactivateWorker(id == null ? null : id.trim());
    }

    void showWorkerList(List<Worker> workers) {
        System.out.println();
        if (workers.isEmpty()) {
            System.out.println("No workers found.");
            return;
        }
        System.out.println("ID   Worker ID   Name                 Type                 Daily Wage  Status");
        for (Worker w : workers) {
            System.out.printf("%-4d %-11s %-21s %-21s ₹%-9.2f %s%n",
                    w.getId(),
                    w.getWorkerId(),
                    w.getName(),
                    w.getType(),
                    w.getDailyWage(),
                    w.getStatus());
        }
    }

    void onWorkerAdded(Worker worker) {
        System.out.println("Worker '" + worker.getName() + "' added ID: " + worker.getWorkerId() + "");
    }

    void onWorkerUpdated(Worker worker) {
        System.out.println("  Worker '" + worker.getName() + "' is now " + worker.getStatus());
    }

    void onWorkerFailed(String message) {
        System.out.println("  ERROR: " + message);
    }
}










