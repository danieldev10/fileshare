package ng.edu.aun.fileshare.service.implementation;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import ng.edu.aun.fileshare.model.Attachment;
import ng.edu.aun.fileshare.model.AttachmentDetails;
import ng.edu.aun.fileshare.model.Tag;
import ng.edu.aun.fileshare.model.User;
import ng.edu.aun.fileshare.repository.AttachmentDetailsRepository;
import ng.edu.aun.fileshare.repository.AttachmentRepository;
import ng.edu.aun.fileshare.service.AttachmentService;
import ng.edu.aun.fileshare.service.UserService;

@Service
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    private UserService userService;

    private AttachmentRepository attachmentRepository;
    private AttachmentDetailsRepository attachmentDetailsRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository,
            AttachmentDetailsRepository attachmentDetailsRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentDetailsRepository = attachmentDetailsRepository;
    }

    @Override
    public Attachment saveAttachment(MultipartFile file, String title, String description, Set<Tag> tags)
            throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence " + fileName);
            }

            User currentUser = userService.getCurrentUser();

            AttachmentDetails attachmentDetails = new AttachmentDetails(title, description);
            Attachment savedAttachment = attachmentRepository
                    .save(new Attachment(fileName, file.getContentType(), file.getBytes(), attachmentDetails, tags));

            attachmentDetails.setAttachment(savedAttachment);
            attachmentDetailsRepository.save(attachmentDetails);

            savedAttachment.setUser(currentUser);
            attachmentRepository.save(savedAttachment);

            return savedAttachment;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not save File: " + fileName, e);
        }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository.findById(fileId)
                .orElseThrow(() -> new Exception("File not found with Id: " + fileId));
    }

    @Override
    public void editAttachment(String fileId, MultipartFile file, String title, String description, Set<Tag> tags)
            throws Exception {
        Attachment existingAttachment = attachmentRepository.findById(fileId)
                .orElseThrow(() -> new Exception("File not found with Id: " + fileId));

        // Check if a new file is being uploaded
        if (file != null && !file.isEmpty()) {
            existingAttachment.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
            existingAttachment.setFileType(file.getContentType());
            existingAttachment.setData(file.getBytes());
        }

        AttachmentDetails existingDetails = existingAttachment.getAttachmentDetails();
        if (existingDetails != null) {
            existingDetails.setTitle(title);
            existingDetails.setDescription(description);
            attachmentDetailsRepository.save(existingDetails);
        }

        // Update tags
        existingAttachment.setTags(tags);

        attachmentRepository.save(existingAttachment);
    }

    @Override
    public void deleteAttachment(String fileId) throws Exception {
        Attachment existingAttachment = attachmentRepository.findById(fileId)
                .orElseThrow(() -> new Exception("File not found with Id: " + fileId));
        attachmentRepository.delete(existingAttachment);
    }

    @Override
    public List<Attachment> searchByFileName(String fileName) {
        return attachmentRepository.findByFileNameContainingIgnoreCase(fileName);
    }

    @Override
    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAll();
    }

    @Override
    public long countAttachments() {
        return attachmentRepository.count();
    }

    @Override
    public void updateDownloadCount(String fileId) throws Exception {
        Attachment attachment = getAttachment(fileId);
        if (attachment != null) {
            attachment.setDownloadCount(attachment.getDownloadCount() + 1);
            attachmentRepository.save(attachment);
        } else {
            throw new Exception("Attachment not found with ID: " + fileId);
        }
    }

    @Override
    public int getTotalDownloadCount() {
        List<Attachment> attachments = attachmentRepository.findAll();
        return attachments.stream().mapToInt(Attachment::getDownloadCount).sum();
    }

    @Override
    public List<Attachment> findByFileType(String fileType) {
        return attachmentRepository.findByFileType(fileType);
    }

    @Override
    public List<Attachment> findByFileTypeStartingWith(String fileTypePrefix) {
        return attachmentRepository.findByFileTypeStartingWith(fileTypePrefix);
    }

    @Override
    public int countByFileType(String fileType) {
        return attachmentRepository.countByFileType(fileType);
    }
}
