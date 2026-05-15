package com.gowthamX.constructionHub.features.attendance;

import com.gowthamX.constructionHub.data.dto.Attendance;
import com.gowthamX.constructionHub.data.dto.Site;
import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.data.dto.Worker;
import com.gowthamX.constructionHub.util.ConsoleInput;

import java.util.List;
import java.util.Scanner;

public class AttendanceView {

    private final AttendanceModel attendanceModel;
    private final User user;
    private final Scanner scanner;

    public AttendanceView(User user) {
        this.attendanceModel = new AttendanceModel(this);
        this.user = user;
        this.scanner = ConsoleInput.getScanner();
    }

    public void init() {
        while (true) {
            System.out.println();
            System.out.println("  [ Attendance ]");
            System.out.println("  1. Mark Attendance");
            System.out.println("  2. View Attendance for a Site & Date");
            System.out.println("  3. Allocate Worker to Site");
            System.out.println("  4. View Available Workers on Date");
            System.out.println("  0. Back");
            System.out.print("  Choose an option: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": promptMarkAttendance();         break;
                case "2": promptViewAttendance();         break;
                case "3": promptAllocateWorker();         break;
                case "4": promptAvailableWorkers();       break;
                case "0": return;
                default:  System.out.println("  Invalid option. Please try again.");
            }
        }
    }

    private void promptMarkAttendance() {
        System.out.println();
        System.out.println("  -- Mark Attendance --");
        System.out.print("  Worker ID         : ");
        String workerId = scanner.nextLine();
        attendanceModel.loadActiveSites();
        System.out.print("  Site ID           : ");
        String siteId = scanner.nextLine();
        System.out.print("  Date (dd-MM-yyyy) : ");
        String date = scanner.nextLine();
        System.out.print("  Present? (Y/N)    : ");
        String presentInput = scanner.nextLine();
        boolean present = presentInput != null && (presentInput.trim().equalsIgnoreCase("Y"));
        attendanceModel.markAttendance(
                workerId == null ? null : workerId.trim(),
                siteId == null ? null : siteId.trim(),
                date == null ? null : date.trim(),
                present);
    }

    private void promptViewAttendance() {
        System.out.println();
        System.out.print("  Site ID           : ");
        String siteId = scanner.nextLine();
        System.out.print("  Date (dd-MM-yyyy) : ");
        String date = scanner.nextLine();
        attendanceModel.viewAttendanceForSiteAndDate(
                siteId == null ? null : siteId.trim(),
                date == null ? null : date.trim());
    }

    private void promptAllocateWorker() {
        System.out.println();
        System.out.println("  -- Allocate Worker to Site --");
        System.out.print("  Worker ID         : ");
        String workerId = scanner.nextLine();
        attendanceModel.loadActiveSites();
        System.out.print("  Site ID           : ");
        String siteId = scanner.nextLine();
        System.out.print("  Date (dd-MM-yyyy) : ");
        String date = scanner.nextLine();
        attendanceModel.allocateWorker(
                workerId == null ? null : workerId.trim(),
                siteId == null ? null : siteId.trim(),
                date == null ? null : date.trim());
    }

    private void promptAvailableWorkers() {
        System.out.println();
        System.out.print("  Date (dd-MM-yyyy) : ");
        String date = scanner.nextLine();
        attendanceModel.loadAvailableWorkers(date == null ? null : date.trim());
    }

    void showSitePicker(List<Site> sites) {
        if (sites.isEmpty()) { System.out.println("  No active sites found."); return; }
        System.out.println("  Active Sites:");
        for (Site s : sites)
            System.out.printf("    ID: %-4d  %s%n", s.getId(), s.getName());
    }

    void showAttendanceList(List<Attendance> records, String dateLabel) {
        System.out.println();
        System.out.println("  Attendance on " + dateLabel + ":");
        if (records.isEmpty()) { System.out.println("  No records found."); return; }
        System.out.println("  Worker ID   Present");
        for (Attendance a : records)
            System.out.printf("  %-11d %s%n", a.getWorkerId(), a.isPresent() ? "YES" : "NO");
    }

    void showAvailableWorkers(List<Worker> workers, String dateLabel) {
        System.out.println();
        System.out.println("  Available Workers on " + dateLabel + ":");
        if (workers.isEmpty()) { System.out.println("  All workers are already allocated."); return; }
        for (Worker w : workers)
            System.out.printf("    ID: %-4d  %-20s  [%s]%n", w.getId(), w.getName(), w.getType());
    }

    void onWorkerAllocated(Worker worker, String dateLabel) {
        System.out.println("  " + worker.getName() + " allocated successfully on " + dateLabel);
    }

    void onAttendanceMarked(Worker worker, String dateLabel, boolean present) {
        System.out.println("  Attendance marked: " + worker.getName()
                + " — " + (present ? "PRESENT" : "ABSENT") + " on " + dateLabel);
    }

    void onAttendanceFailed(String message) {
        System.out.println("  ERROR: " + message);
    }
}

