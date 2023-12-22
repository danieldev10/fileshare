package ng.edu.aun.fileshare.service.implementation;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ng.edu.aun.fileshare.model.Attachment;
import ng.edu.aun.fileshare.model.Tag;
import ng.edu.aun.fileshare.repository.AttachmentRepository;
import ng.edu.aun.fileshare.repository.TagRepository;
import ng.edu.aun.fileshare.service.TagService;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public List<String> getAllTags() {
        // Retrieve all tags from the repository and map them to tag names
        return tagRepository.findAll().stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<Attachment> getAttachmentsByTag(String tagName) {
        return attachmentRepository.findByTagName(tagName);
    }
}
