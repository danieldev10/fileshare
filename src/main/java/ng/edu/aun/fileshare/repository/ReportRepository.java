package ng.edu.aun.fileshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ng.edu.aun.fileshare.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
