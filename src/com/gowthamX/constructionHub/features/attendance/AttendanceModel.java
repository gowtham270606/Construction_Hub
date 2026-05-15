package com.gowthamX.constructionHub.features.attendance;

import com.gowthamX.constructionHub.data.dto.Allocation;
import com.gowthamX.constructionHub.data.dto.Attendance;
import com.gowthamX.constructionHub.data.dto.Site;
import com.gowthamX.constructionHub.data.dto.Worker;
import com.gowthamX.constructionHub.data.repository.ConstructionDB;
import com.gowthamX.constructionHub.util.ParseHelper;

import java.util.List;

class AttendanceModel {

    private final AttendanceView attendanceView;

    AttendanceModel(AttendanceView attendanceView) {
        this.attendanceView = attendanceView;
    }

    void loadActiveSites() {
        List<Site> sites = ConstructionDB.getInstance().getSitesByStatus(Site.SiteStatus.ACTIVE);
        attendanceView.showSitePicker(sites);
    }

    void allocateWorker(String workerIdInput, String siteIdInput, String dateInput) {
        Long workerId = parseLong(workerIdInput);
        if (workerId == null || ConstructionDB.getInstance().getWorkerById(workerId) == null) {
            attendanceView.onAttendanceFailed("Invalid or unknown worker ID");
            return;
        }
        Long siteId = parseLong(siteIdInput);
        if (siteId == null || ConstructionDB.getInstance().getSiteById(siteId) == null) {
            attendanceView.onAttendanceFailed("Invalid or unknown site ID");
            return;
        }
        Long date = ParseHelper.parseDate(dateInput);
        if (date == null) { attendanceView.onAttendanceFailed("Invalid date. Use dd-MM-yyyy"); return; }

        if (ConstructionDB.getInstance().isWorkerAllocatedOnDate(workerId, date)) {
            attendanceView.onAttendanceFailed("Worker is already allocated on this date");
            return;
        }

        Allocation allocation = new Allocation();
        allocation.setWorkerId(workerId);
        allocation.setSiteId(siteId);
        allocation.setDate(date);

        ConstructionDB.getInstance().addAllocation(allocation);
        Worker worker = ConstructionDB.getInstance().getWorkerById(workerId);
        attendanceView.onWorkerAllocated(worker, ParseHelper.formatDate(date));
    }

    void markAttendance(String workerIdInput, String siteIdInput, String dateInput, boolean present) {
        Long workerId = parseLong(workerIdInput);
        if (workerId == null || ConstructionDB.getInstance().getWorkerById(workerId) == null) {
            attendanceView.onAttendanceFailed("Invalid or unknown worker ID");
            return;
        }
        Long siteId = parseLong(siteIdInput);
        if (siteId == null || ConstructionDB.getInstance().getSiteById(siteId) == null) {
            attendanceView.onAttendanceFailed("Invalid or unknown site ID");
            return;
        }
        Long date = ParseHelper.parseDate(dateInput);
        if (date == null) { attendanceView.onAttendanceFailed("Invalid date. Use dd-MM-yyyy"); return; }

        if (ConstructionDB.getInstance().hasAttendanceRecord(workerId, siteId, date)) {
            attendanceView.onAttendanceFailed("Attendance already marked for this worker on this date");
            return;
        }

        Attendance attendance = new Attendance();
        attendance.setWorkerId(workerId);
        attendance.setSiteId(siteId);
        attendance.setDate(date);
        attendance.setPresent(present);

        ConstructionDB.getInstance().addAttendance(attendance);
        Worker worker = ConstructionDB.getInstance().getWorkerById(workerId);
        attendanceView.onAttendanceMarked(worker, ParseHelper.formatDate(date), present);
    }

    void viewAttendanceForSiteAndDate(String siteIdInput, String dateInput) {
        Long siteId = parseLong(siteIdInput);
        if (siteId == null) { attendanceView.onAttendanceFailed("Invalid site ID"); return; }
        Long date = ParseHelper.parseDate(dateInput);
        if (date == null) { attendanceView.onAttendanceFailed("Invalid date. Use dd-MM-yyyy"); return; }

        List<Attendance> records = ConstructionDB.getInstance().getAttendanceBySiteAndDate(siteId, date);
        attendanceView.showAttendanceList(records, ParseHelper.formatDate(date));
    }

    void loadAvailableWorkers(String dateInput) {
        Long date = ParseHelper.parseDate(dateInput);
        if (date == null) { attendanceView.onAttendanceFailed("Invalid date. Use dd-MM-yyyy"); return; }
        List<Worker> workers = ConstructionDB.getInstance().getAvailableWorkersOnDate(date);
        attendanceView.showAvailableWorkers(workers, ParseHelper.formatDate(date));
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
