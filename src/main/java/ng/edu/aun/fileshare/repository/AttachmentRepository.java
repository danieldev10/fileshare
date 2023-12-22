package ng.edu.aun.fileshare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ng.edu.aun.fileshare.model.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, String> {
    List<Attachment> findByFileNameContainingIgnoreCase(String fileName);

    @Query("SELECT a FROM Attachment a JOIN a.tags t WHERE t.name = :tagName")
    List<Attachment> findByTagName(@Param("tagName") String tagName);

    List<Attachment> findByFileType(String fileType);

    List<Attachment> findByFileTypeStartingWith(String fileTypePrefix);

    int countByFileType(String fileType);

}
