package ng.edu.aun.fileshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ng.edu.aun.fileshare.model.AttachmentDetails;

@Repository
public interface AttachmentDetailsRepository extends JpaRepository<AttachmentDetails, Long> {
}
