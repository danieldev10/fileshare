package ng.edu.aun.fileshare.service;

import java.util.*;

import ng.edu.aun.fileshare.model.Attachment;

public interface TagService {
    List<String> getAllTags();

    List<Attachment> getAttachmentsByTag(String tagName);
}
