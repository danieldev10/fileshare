package ng.edu.aun.fileshare.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ng.edu.aun.fileshare.model.Report;
import ng.edu.aun.fileshare.repository.ReportRepository;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Long id) {
        return reportRepository.findById(id).orElse(null);
    }

    @Transactional
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    public Report updateReport(Long id, Report updateReport) {
        if (reportRepository.existsById(id)) {
            updateReport.setReport_id(id);
            return reportRepository.save(updateReport);
        }
        return null;
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

}
