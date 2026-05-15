package com.gowthamX.constructionHub.data.repository;

import com.gowthamX.constructionHub.data.dto.Allocation;
import com.gowthamX.constructionHub.data.dto.Attendance;
import com.gowthamX.constructionHub.data.dto.Salary;
import com.gowthamX.constructionHub.data.dto.Site;
import com.gowthamX.constructionHub.data.dto.Task;
import com.gowthamX.constructionHub.data.dto.User;
import com.gowthamX.constructionHub.data.dto.Worker;
import com.gowthamX.constructionHub.util.ParseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConstructionDB {
    private static ConstructionDB instance = null; //only one instance

    private ConstructionDB() {
    }

    public static ConstructionDB getInstance() {
        if (instance == null) instance = new ConstructionDB();
        return instance;
    }

    // primary key
    private long userPk       = 0;
    private long sitePk       = 0;
    private long workerPk     = 0;
    private long attendancePk = 0;
    private long taskPk       = 0;
    private long allocationPk = 0;
    private long salaryPk     = 0;

    // tables
    private final List<User>       users       = new ArrayList<>();
    private final List<Site>       sites       = new ArrayList<>();
    private final List<Worker>     workers     = new ArrayList<>();
    private final List<Attendance> attendances = new ArrayList<>();
    private final List<Task>       tasks       = new ArrayList<>();
    private final List<Allocation> allocations = new ArrayList<>();
    private final List<Salary>     salaries    = new ArrayList<>();

    // User
    public User addUser(User u) {
        if (u == null || u.getUsername() == null) return null;
        userPk++;
        u.setId(userPk);
        if (u.getCreatedAt() == null) u.setCreatedAt(System.currentTimeMillis());
        users.add(u);
        return u;
    }

    public User getUserByUsername(String username) {
        if (username == null) return null;
        String key = username.trim().toLowerCase(Locale.ROOT);
        for (User u : users)
            if (u.getUsername() != null && u.getUsername().toLowerCase(Locale.ROOT).equals(key))
                return u;
        return null;
    }

    public User authenticate(String username, String password) {
        User u = getUserByUsername(username);
        if (u == null || password == null || !password.equals(u.getPassword())) return null;
        return u;
    }

    public boolean hasUsers() {
        return !users.isEmpty();
    }

    // Site
    public Site addSite(Site s) {
        if (s == null) return null;
        sitePk++;
        s.setId(sitePk);
        if (s.getCreatedAt() == null) s.setCreatedAt(System.currentTimeMillis());
        if (s.getStatus() == null)    s.setStatus(Site.SiteStatus.ACTIVE);
        sites.add(s);
        return s;
    }

    public Site updateSite(Site s) {
        if (s == null || s.getId() == null) return null;
        for (int i = 0; i < sites.size(); i++)
            if (s.getId().equals(sites.get(i).getId())) { sites.set(i, s); return s; }
        return null;
    }

    public List<Site> getAllSites() { return new ArrayList<>(sites); }

    public Site getSiteById(Long id) {
        if (id == null) return null;
        for (Site s : sites) if (id.equals(s.getId())) return s;
        return null;
    }

    public List<Site> getSitesByStatus(Site.SiteStatus status) {
        List<Site> result = new ArrayList<>();
        for (Site s : sites) if (status == null || status == s.getStatus()) result.add(s);
        return result;
    }

    // Worker
    public Worker addWorker(Worker w) {
        if (w == null) return null;
        workerPk++;
        w.setId(workerPk);
        w.setWorkerId(String.format(Locale.ROOT, "WRK%04d", workerPk));
        if (w.getCreatedAt() == null) w.setCreatedAt(System.currentTimeMillis());
        if (w.getStatus() == null)    w.setStatus(Worker.WorkerStatus.ACTIVE);
        workers.add(w);
        return w;
    }

    public List<Worker> getAllWorkers() { return new ArrayList<>(workers); }

    public List<Worker> getActiveWorkers() {
        List<Worker> result = new ArrayList<>();
        for (Worker w : workers)
            if (w.getStatus() == Worker.WorkerStatus.ACTIVE) result.add(w);
        return result;
    }

    public Worker getWorkerById(Long id) {
        if (id == null) return null;
        for (Worker w : workers) if (id.equals(w.getId())) return w;
        return null;
    }

    // Attendance
    public Attendance addAttendance(Attendance a) {
        if (a == null) return null;
        attendancePk++;
        a.setId(attendancePk);
        if (a.getMarkedAt() == null) a.setMarkedAt(System.currentTimeMillis());
        attendances.add(a);
        return a;
    }

//    public List<Attendance> getAttendanceByWorker(Long workerId) {
//        List<Attendance> result = new ArrayList<>();
//        if (workerId == null) return result;
//        for (Attendance a : attendances)
//            if (workerId.equals(a.getWorkerId())) result.add(a);
//        return result;
//    }

    public List<Attendance> getAttendanceBySiteAndDate(Long siteId, Long date) {
        List<Attendance> result = new ArrayList<>();
        if (siteId == null || date == null) return result;
        for (Attendance a : attendances)
            if (siteId.equals(a.getSiteId()) && isSameDay(a.getDate(), date))
                result.add(a);
        return result;
    }

    public boolean hasAttendanceRecord(Long workerId, Long siteId, Long date) {
        if (workerId == null || siteId == null || date == null) return false;
        for (Attendance a : attendances)
            if (workerId.equals(a.getWorkerId()) && siteId.equals(a.getSiteId())
                    && isSameDay(a.getDate(), date))
                return true;
        return false;
    }

    public List<Attendance> getAttendanceBySiteAndRange(Long siteId, Long from, Long to) {
        List<Attendance> result = new ArrayList<>();
        if (siteId == null) return result;
        for (Attendance a : attendances)
            if (siteId.equals(a.getSiteId())
                    && (from == null || a.getDate() >= from)
                    && (to   == null || a.getDate() <= to))
                result.add(a);
        return result;
    }

    // Task
    public Task addTask(Task t) {
        if (t == null) return null;
        taskPk++;
        t.setId(taskPk);
        long now = System.currentTimeMillis();
        if (t.getCreatedAt() == null) t.setCreatedAt(now);
        t.setUpdatedAt(now);
        if (t.getStatus() == null) t.setStatus(Task.TaskStatus.PENDING);
        tasks.add(t);
        return t;
    }

    public Task updateTask(Task t) {
        if (t == null || t.getId() == null) return null;
        for (int i = 0; i < tasks.size(); i++)
            if (t.getId().equals(tasks.get(i).getId())) {
                t.setUpdatedAt(System.currentTimeMillis());
                tasks.set(i, t);
                return t;
            }
        return null;
    }

    public List<Task> getTasksBySite(Long siteId) {
        List<Task> result = new ArrayList<>();
        if (siteId == null) return result;
        for (Task t : tasks) if (siteId.equals(t.getSiteId())) result.add(t);
        return result;
    }

    public List<Task> getAllTasks() { return new ArrayList<>(tasks); }

    public Task getTaskById(Long id) {
        if (id == null) return null;
        for (Task t : tasks) if (id.equals(t.getId())) return t;
        return null;
    }

    // Allocation
    public Allocation addAllocation(Allocation a) {
        if (a == null) return null;
        allocationPk++;
        a.setId(allocationPk);
        if (a.getCreatedAt() == null) a.setCreatedAt(System.currentTimeMillis());
        allocations.add(a);
        return a;
    }

    public boolean isWorkerAllocatedOnDate(Long workerId, Long date) {
        if (workerId == null || date == null) return false;
        for (Allocation a : allocations)
            if (workerId.equals(a.getWorkerId()) && isSameDay(a.getDate(), date))
                return true;
        return false;
    }

//    public List<Allocation> getAllocationsBySiteAndDate(Long siteId, Long date) {
//        List<Allocation> result = new ArrayList<>();
//        if (siteId == null || date == null) return result;
//        for (Allocation a : allocations)
//            if (siteId.equals(a.getSiteId()) && isSameDay(a.getDate(), date))
//                result.add(a);
//        return result;
//    }

    public List<Worker> getAvailableWorkersOnDate(Long date) {
        List<Worker> result = new ArrayList<>();
        if (date == null) return result;
        for (Worker w : workers) {
            if (w.getStatus() != Worker.WorkerStatus.ACTIVE) continue;
            if (!isWorkerAllocatedOnDate(w.getId(), date)) result.add(w);
        }
        return result;
    }

    // Salary
    public Salary addSalary(Salary s) {
        if (s == null) return null;
        salaryPk++;
        s.setId(salaryPk);
        if (s.getPaidAt() == null) s.setPaidAt(System.currentTimeMillis());
        salaries.add(s);
        return s;
    }

    public List<Salary> getSalariesByWorker(Long workerId) {
        List<Salary> result = new ArrayList<>();
        if (workerId == null) return result;
        for (Salary s : salaries) if (workerId.equals(s.getWorkerId())) result.add(s);
        return result;
    }

//    public List<Salary> getSalariesBySite(Long siteId) {
//        List<Salary> result = new ArrayList<>();
//        if (siteId == null) return result;
//        for (Salary s : salaries) if (siteId.equals(s.getSiteId())) result.add(s);
//        return result;
//    }

    public List<Salary> getAllSalaries() { return new ArrayList<>(salaries); }

    // helper
    private boolean isSameDay(Long a, Long b) {
        if (a == null || b == null) return false;
        return ParseHelper.isSameDay(a, b);
    }
}
