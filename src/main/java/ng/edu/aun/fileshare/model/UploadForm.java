package ng.edu.aun.fileshare.model;

import java.util.*;

import org.springframework.web.multipart.MultipartFile;

public class UploadForm {
    private MultipartFile file;
    private String title;
    private String description;
    private Set<String> tags;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
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

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

}