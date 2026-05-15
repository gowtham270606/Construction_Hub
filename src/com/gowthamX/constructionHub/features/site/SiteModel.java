package com.gowthamX.constructionHub.features.site;

import com.gowthamX.constructionHub.data.dto.Site;
import com.gowthamX.constructionHub.data.repository.ConstructionDB;
import com.gowthamX.constructionHub.util.ParseHelper;

import java.util.List;

class SiteModel {

    private final SiteView siteView;

    SiteModel(SiteView siteView) {
        this.siteView = siteView;
    }

    String validateName(String name) {
        if (name == null || name.trim().isEmpty()) return "Site name cannot be empty";
        if (name.trim().length() < 3) return "Site name must be at least 3 characters";
        return null;
    }

    String validateLocation(String location) {
        if (location == null || location.trim().isEmpty()) return "Location cannot be empty";
        return null;
    }


    void addSite(String name, String location, String startDateInput, String endDateInput) {
        String nameError = validateName(name);
        if (nameError != null) { siteView.onSiteFailed(nameError); return; }

        String locationError = validateLocation(location);
        if (locationError != null) { siteView.onSiteFailed(locationError); return; }

        Long startDate = ParseHelper.parseDate(startDateInput);
        if (startDate == null) { siteView.onSiteFailed("Invalid start date. Use dd-MM-yyyy"); return; }

        Long endDate = null;
        if (endDateInput != null && !endDateInput.trim().isEmpty()) {
            endDate = ParseHelper.parseDate(endDateInput);
            if (endDate == null) { siteView.onSiteFailed("Invalid end date. Use dd-MM-yyyy"); return; }
            if (endDate < startDate) { siteView.onSiteFailed("End date must be after start date"); return; }
        }

        Site site = new Site();
        site.setName(name.trim());
        site.setLocation(location.trim());
        site.setStartDate(startDate);
        site.setEndDate(endDate);
        site.setStatus(Site.SiteStatus.ACTIVE);

        Site saved = ConstructionDB.getInstance().addSite(site);
        if (saved == null) { siteView.onSiteFailed("Could not add site. Please try again."); return; }
        siteView.onSiteAdded(saved);
    }

    void loadAllSites() {
        List<Site> sites = ConstructionDB.getInstance().getAllSites();
        siteView.showSiteList(sites);
    }

    void markSiteCompleted(String idInput) {
        Long id = parseLong(idInput);
        if (id == null) { siteView.onSiteFailed("Invalid site ID"); return; }

        Site site = ConstructionDB.getInstance().getSiteById(id);
        if (site == null) { siteView.onSiteFailed("Site not found with ID: " + id); return; }
        if (site.getStatus() == Site.SiteStatus.COMPLETED) {
            siteView.onSiteFailed("Site is already marked as completed");
            return;
        }
        site.setStatus(Site.SiteStatus.COMPLETED);
        ConstructionDB.getInstance().updateSite(site);
        siteView.onSiteUpdated(site);
    }

    private Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try { return Long.parseLong(input.trim()); } catch (NumberFormatException e) { return null; }
    }
}
