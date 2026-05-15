package com.gowthamX.constructionHub.features.task;

import com.gowthamX.constructionHub.data.dto.Site;
import com.gowthamX.constructionHub.data.dto.Task;
import com.gowthamX.constructionHub.data.dto.Worker;
import com.gowthamX.constructionHub.data.repository.ConstructionDB;
import com.gowthamX.constructionHub.util.ParseHelper;

import java.util.List;

class TaskModel {

    private final TaskView taskView;

    TaskModel(TaskView taskView) {
        this.taskView = taskView;
    }

    String validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) return "Description cannot be empty";
        if (description.trim().length() < 5) return "Description must be at least 5 characters";
        return null;
    }

    void loadSites() {
        List<Site> sites = ConstructionDB.getInstance().getSitesByStatus(Site.SiteStatus.ACTIVE);
        taskView.showSitePicker(sites);
    }

    void loadWorkersForSite(Long siteId) {
        List<Worker> workers = ConstructionDB.getInstance().getActiveWorkers();
        taskView.showWorkerPicker(workers);
    }

    void createTask(String siteIdInput, String workerIdInput,
                    String description, String deadlineInput) {
        Long siteId = parseLong(siteIdInput);
        if (siteId == null || ConstructionDB.getInstance().getSiteById(siteId) == null) {
            taskView.onTaskFailed("Invalid or unknown site ID");
            return;
        }

        Long workerId = null;
        if (workerIdInput != null && !workerIdInput.trim().isEmpty()) {
            workerId = parseLong(workerIdInput);
            if (workerId == null || ConstructionDB.getInstance().getWorkerById(workerId) == null) {
                taskView.onTaskFailed("Invalid or unknown worker ID");
                return;
            }
        }

        String descError = validateDescription(description);
        if (descError != null) { taskView.onTaskFailed(descError); return; }

        Long deadline = null;
        if (deadlineInput != null && !deadlineInput.trim().isEmpty()) {
            deadline = ParseHelper.parseDate(deadlineInput);
            if (deadline == null) { taskView.onTaskFailed("Invalid deadline. Use dd-MM-yyyy"); return; }
        }

        Task task = new Task();
        task.setSiteId(siteId);
        task.setWorkerId(workerId);
        task.setDescription(description.trim());
        task.setDeadline(deadline);
        task.setStatus(Task.TaskStatus.PENDING);

        Task saved = ConstructionDB.getInstance().addTask(task);
        if (saved == null) { taskView.onTaskFailed("Could not create task. Please try again."); return; }
        taskView.onTaskCreated(saved);
    }

    void updateTaskStatus(String taskIdInput, String statusChoice) {
        Long taskId = parseLong(taskIdInput);
        if (taskId == null) { taskView.onTaskFailed("Invalid task ID"); return; }

        Task task = ConstructionDB.getInstance().getTaskById(taskId);
        if (task == null) { taskView.onTaskFailed("Task not found with ID: " + taskId); return; }

        Task.TaskStatus newStatus = parseStatus(statusChoice);
        if (newStatus == null) { taskView.onTaskFailed("Invalid status choice"); return; }

        task.setStatus(newStatus);
        ConstructionDB.getInstance().updateTask(task);
        taskView.onTaskUpdated(task);
    }

    void loadTasksBySite(String siteIdInput) {
        Long siteId = parseLong(siteIdInput);
        if (siteId == null) { taskView.onTaskFailed("Invalid site ID"); return; }
        List<Task> tasks = ConstructionDB.getInstance().getTasksBySite(siteId);
        taskView.showTaskList(tasks);
    }

    Task.TaskStatus parseStatus(String choice) {
        if (choice == null) return null;
        switch (choice.trim()) {
            case "1": return Task.TaskStatus.PENDING;
            case "2": return Task.TaskStatus.IN_PROGRESS;
            case "3": return Task.TaskStatus.COMPLETED;
            default:  return null;
        }
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
