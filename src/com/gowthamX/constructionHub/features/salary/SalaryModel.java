package com.gowthamX.constructionHub.features.salary;

import com.gowthamX.constructionHub.data.dto.Attendance;
import com.gowthamX.constructionHub.data.dto.Salary;
import com.gowthamX.constructionHub.data.dto.Worker;
import com.gowthamX.constructionHub.data.repository.ConstructionDB;
import com.gowthamX.constructionHub.util.ParseHelper;

import java.util.List;

class SalaryModel {

    private final SalaryView salaryView;

    SalaryModel(SalaryView salaryView) {
        this.salaryView = salaryView;
    }

    void calculateAndPaySalary(String workerIdInput, String siteIdInput,
                               String weekStartInput, String weekEndInput) {
        Long workerId = parseLong(workerIdInput);
        if (workerId == null || ConstructionDB.getInstance().getWorkerById(workerId) == null) {
            salaryView.onSalaryFailed("Invalid or unknown worker ID");
            return;
        }
        Long siteId = parseLong(siteIdInput);
        if (siteId == null || ConstructionDB.getInstance().getSiteById(siteId) == null) {
            salaryView.onSalaryFailed("Invalid or unknown site ID");
            return;
        }
        Long weekStart = ParseHelper.parseDate(weekStartInput);
        if (weekStart == null) { salaryView.onSalaryFailed("Invalid week start date. Use dd-MM-yyyy"); return; }
        Long weekEnd = ParseHelper.parseDate(weekEndInput);
        if (weekEnd == null) { salaryView.onSalaryFailed("Invalid week end date. Use dd-MM-yyyy"); return; }
        if (weekEnd < weekStart) { salaryView.onSalaryFailed("Week end must be after week start"); return; }

        Worker worker = ConstructionDB.getInstance().getWorkerById(workerId);
        List<Attendance> records = ConstructionDB.getInstance()
                .getAttendanceBySiteAndRange(siteId, weekStart, weekEnd);

        int presentDays = 0;
        for (Attendance a : records)
            if (workerId.equals(a.getWorkerId()) && a.isPresent()) presentDays++;

        double totalAmount = presentDays * worker.getDailyWage();

        Salary salary = new Salary();
        salary.setWorkerId(workerId);
        salary.setSiteId(siteId);
        salary.setTotalDays(presentDays);
        salary.setTotalAmount(totalAmount);
        salary.setWeekStart(weekStart);
        salary.setWeekEnd(weekEnd);

        Salary saved = ConstructionDB.getInstance().addSalary(salary);
        if (saved == null) { salaryView.onSalaryFailed("Could not record salary. Please try again."); return; }
        salaryView.onSalaryPaid(worker, saved);
    }

    void viewSalariesByWorker(String workerIdInput) {
        Long workerId = parseLong(workerIdInput);
        if (workerId == null) { salaryView.onSalaryFailed("Invalid worker ID"); return; }
        Worker worker = ConstructionDB.getInstance().getWorkerById(workerId);
        if (worker == null) { salaryView.onSalaryFailed("Worker not found"); return; }
        List<Salary> salaries = ConstructionDB.getInstance().getSalariesByWorker(workerId);
        salaryView.showSalaryList(worker, salaries);
    }

    void viewAllSalaries() {
        List<Salary> salaries = ConstructionDB.getInstance().getAllSalaries();
        salaryView.showAllSalaries(salaries);
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
