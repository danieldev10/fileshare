package ng.edu.aun.fileshare.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

@Entity
public class AttachmentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachment_detail_id;

    private String title;

    @Lob
    private String description;

    @OneToOne(mappedBy = "attachmentDetails", cascade = CascadeType.ALL)
    private Attachment attachment;

    public AttachmentDetails() {
    }

    public AttachmentDetails(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getAttachment_detail_id() {
        return attachment_detail_id;
    }

    public void setAttachment_detail_id(Long attachment_detail_id) {
        this.attachment_detail_id = attachment_detail_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "AttachmentDetails [attachment_detail_id=" + attachment_detail_id + ", title=" + title + ", description="
                + description + "]";
    }

}
