package ng.edu.aun.fileshare.model;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Attachment {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String attachment_id;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_details_id")
    private AttachmentDetails attachmentDetails;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "attachment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "attachment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "attachment_tags", joinColumns = @JoinColumn(name = "attachment_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    private int downloadCount;

    public Attachment() {
    }

    public Attachment(String fileName, String fileType, byte[] data, AttachmentDetails attachmentDetails,
            Set<Tag> tags) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.attachmentDetails = attachmentDetails;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Attachment [attachment_id=" + attachment_id + ", fileName=" + fileName + ", fileType=" + fileType + "]";
    }

    public String getTagNames() {
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.joining(", "));
    }

    public String getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(String attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public AttachmentDetails getAttachmentDetails() {
        return attachmentDetails;
    }

    public void setAttachmentDetails(AttachmentDetails attachmentDetails) {
        this.attachmentDetails = attachmentDetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }
}
