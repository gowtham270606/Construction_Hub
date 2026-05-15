package com.gowthamX.constructionHub.features.worker;

import com.gowthamX.constructionHub.data.dto.Worker;
import com.gowthamX.constructionHub.data.repository.ConstructionDB;

import java.util.List;

class WorkerModel {

    private final WorkerView workerView;

    WorkerModel(WorkerView workerView) {
        this.workerView = workerView;
    }

    String validateName(String name) {
        if (name == null || name.trim().isEmpty()) return "Worker name cannot be empty";
        return null;
    }
    String validateType(String type) {
        if (type == null || type.trim().isEmpty()) return "Worker type cannot be empty";
        return null;
    }
    Double parseDailyWage(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try {
            double v = Double.parseDouble(input.trim());
            if (v <= 0) return null;
            return v;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    void addWorker(String name, String type, String dailyWageInput) {
        String nameError = validateName(name);
        if (nameError != null) { workerView.onWorkerFailed(nameError); return; }
        String typeError = validateType(type);
        if (typeError != null) { workerView.onWorkerFailed(typeError); return; }
        Double dailyWage = parseDailyWage(dailyWageInput);
        if (dailyWage == null) { workerView.onWorkerFailed("Daily wage  number"); return; }
        Worker worker = new Worker();
        worker.setName(name.trim());
        worker.setType(type.trim());
        worker.setDailyWage(dailyWage);
        Worker saved = ConstructionDB.getInstance().addWorker(worker);
        if (saved == null) { workerView.onWorkerFailed("Not add worker."); return; }
        workerView.onWorkerAdded(saved);
    }

    void loadAllWorkers() {
        List<Worker> workers = ConstructionDB.getInstance().getAllWorkers();
        workerView.showWorkerList(workers);
    }

    void deactivateWorker(String idInput) {
        Long id = parseLong(idInput);
        if (id == null) { workerView.onWorkerFailed("Invalid worker ID"); return; }

        Worker worker = ConstructionDB.getInstance().getWorkerById(id);
        if (worker == null) { workerView.onWorkerFailed("Worker not found: " + id); return; }
        if (worker.getStatus() == Worker.WorkerStatus.INACTIVE) {
            workerView.onWorkerFailed("Worker is already inactive");
            return;
        }
        worker.setStatus(Worker.WorkerStatus.INACTIVE);
        workerView.onWorkerUpdated(worker);
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
