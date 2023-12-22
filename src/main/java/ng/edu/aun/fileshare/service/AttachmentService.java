package ng.edu.aun.fileshare.service;

import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import ng.edu.aun.fileshare.model.Attachment;

import ng.edu.aun.fileshare.model.Tag;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file, String title, String description, Set<Tag> tags) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;

    void editAttachment(String fileId, MultipartFile file, String title, String description, Set<Tag> tags)
            throws Exception;

    void deleteAttachment(String fileId) throws Exception;

    List<Attachment> searchByFileName(String fileName);

    List<Attachment> getAllAttachments();

    public long countAttachments();

    void updateDownloadCount(String fileId) throws Exception;

    int getTotalDownloadCount();

    List<Attachment> findByFileType(String fileType);

    List<Attachment> findByFileTypeStartingWith(String fileTypePrefix);

    int countByFileType(String fileType);

}
