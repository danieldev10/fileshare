package ng.edu.aun.fileshare;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import ng.edu.aun.fileshare.model.Attachment;
import ng.edu.aun.fileshare.model.Comment;
import ng.edu.aun.fileshare.model.Report;
import ng.edu.aun.fileshare.model.SearchForm;
import ng.edu.aun.fileshare.model.Tag;
import ng.edu.aun.fileshare.model.UploadForm;
import ng.edu.aun.fileshare.model.User;
import ng.edu.aun.fileshare.service.AttachmentService;
import ng.edu.aun.fileshare.service.CommentService;
import ng.edu.aun.fileshare.service.ReportService;
import ng.edu.aun.fileshare.service.TagService;
import ng.edu.aun.fileshare.service.UserService;

@Controller
public class ApplicationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/landing")
    public String landingPage() {
        return "landing";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping(value = "register")
    public RedirectView addNew(User user, RedirectAttributes redir) {
        userService.save(user);
        RedirectView redirectView = new RedirectView("/login", true);
        redir.addFlashAttribute("message",
                "You successfully registered! You can now login");
        return redirectView;
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }

    @GetMapping("/student/index")
    public String indexPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> currentUserAttachments = new ArrayList<>();
        List<Attachment> otherUserAttachments = new ArrayList<>();
        List<String> allTags = tagService.getAllTags();

        for (Attachment attachment : attachmentService.getAllAttachments()) {
            if (attachment.getUser() != null && attachment.getUser().equals(currentUser)) {
                currentUserAttachments.add(attachment);
            } else {
                otherUserAttachments.add(attachment);
            }
        }

        model.addAttribute("otherUserAttachments", otherUserAttachments);
        model.addAttribute("allTags", allTags);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "student-index";
    }

    @GetMapping("/otherfiles")
    public String otherFiles(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> currentUserAttachments = new ArrayList<>();
        List<Attachment> otherUserAttachments = new ArrayList<>();
        List<String> allTags = tagService.getAllTags();

        for (Attachment attachment : attachmentService.getAllAttachments()) {
            if (attachment.getUser() != null && attachment.getUser().equals(currentUser)) {
                currentUserAttachments.add(attachment);
            } else {
                otherUserAttachments.add(attachment);
            }
        }

        model.addAttribute("otherUserAttachments", otherUserAttachments);
        model.addAttribute("allTags", allTags);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "student-index";
    }

    @GetMapping("/myfiles")
    public String myFiles(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> currentUserAttachments = new ArrayList<>();
        List<Attachment> otherUserAttachments = new ArrayList<>();
        List<String> allTags = tagService.getAllTags();

        for (Attachment attachment : attachmentService.getAllAttachments()) {
            if (attachment.getUser() != null && attachment.getUser().equals(currentUser)) {
                currentUserAttachments.add(attachment);
            } else {
                otherUserAttachments.add(attachment);
            }
        }

        model.addAttribute("currentUserAttachments", currentUserAttachments);
        model.addAttribute("allTags", allTags);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "my-files";
    }

    @GetMapping("/search")
    public String searchForm(Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "search";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("searchForm") SearchForm searchForm, Model model) {
        String fileName = searchForm.getFileName();
        List<Attachment> searchResults = attachmentService.searchByFileName(fileName);
        model.addAttribute("searchResults", searchResults);
        return "search";
    }

    // ATTACHMENTS
    @GetMapping("/upload")
    public String showUploadForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("uploadForm", new UploadForm());
        model.addAttribute("fileList", attachmentService.getAllAttachments());

        User currentUser = userService.findByUsername(userDetails.getUsername());

        List<Attachment> currentUserAttachments = new ArrayList<>();
        List<Attachment> otherUserAttachments = new ArrayList<>();
        List<String> allTags = tagService.getAllTags();
        int totalCount = attachmentService.getTotalDownloadCount();

        for (Attachment attachment : attachmentService.getAllAttachments()) {
            if (attachment.getUser() != null && attachment.getUser().equals(currentUser)) {
                currentUserAttachments.add(attachment);
            } else {
                otherUserAttachments.add(attachment);
            }
        }

        model.addAttribute("currentUserAttachments", currentUserAttachments);
        model.addAttribute("otherUserAttachments", otherUserAttachments);
        model.addAttribute("allTags", allTags);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "uploadFile";
    }

    @PostMapping("/upload")
    public String handleFileUpload(
            @ModelAttribute("uploadForm") UploadForm uploadForm,
            @RequestParam("tags") Set<String> tags) throws Exception {

        try {
            MultipartFile file = uploadForm.getFile();
            System.out.println("File: " + file);
            String title = uploadForm.getTitle();
            String description = uploadForm.getDescription();
            Set<Tag> tagEntities = tags.stream().map(Tag::new).collect(Collectors.toSet());

            Attachment attachment = attachmentService.saveAttachment(file, title, description, tagEntities);
            ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(attachment.getAttachment_id())
                    .toUriString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/myfiles";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        attachmentService.updateDownloadCount(fileId);
        Attachment attachment = null;
        attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @GetMapping("/edit/{fileId}")
    public String showEditForm(@PathVariable String fileId, Model model) throws Exception {
        Attachment attachment = attachmentService.getAttachment(fileId);
        model.addAttribute("attachment", attachment);
        return "editFile";
    }

    @PostMapping("/edit/{fileId}")
    public String handleFileEdit(
            @PathVariable String fileId,
            @RequestParam("file") MultipartFile file,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam("tags") Set<String> tagNames) {

        try {
            Set<Tag> tags = tagNames.stream().map(Tag::new).collect(Collectors.toSet());
            attachmentService.editAttachment(fileId, file, title, description, tags);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/myfiles";
    }

    @RequestMapping(value = "/delete/{fileId}", method = { RequestMethod.DELETE,
            RequestMethod.GET })
    public String deleteFile(@PathVariable String fileId) throws Exception {
        attachmentService.deleteAttachment(fileId);
        return "redirect:/myfiles";
    }

    @GetMapping("/try")
    public String tryTemplate() {
        return "index";
    }

    // COMMENT
    @GetMapping("/attachmentDetails/{fileId}")
    public String viewAttachmentDetails(@PathVariable String fileId, Model model,
            @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        Attachment attachment = attachmentService.getAttachment(fileId);
        List<Comment> comments = attachment.getComments();

        model.addAttribute("attachment", attachment);
        model.addAttribute("comments", comments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());

        return "attachmentDetails";
    }

    @PostMapping("/attachmentDetails/comment/{fileId}")
    public String addComment(
            @PathVariable String fileId,
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute Comment commentForm) throws Exception {
        User user = userService.findByUsername(userDetails.getUsername());

        Attachment attachment = attachmentService.getAttachment(fileId);

        Comment reply = new Comment();
        reply.setContent(commentForm.getContent());
        reply.setAttachment(attachment);
        reply.setUser(user);

        commentService.createComment(reply);

        return "redirect:/attachmentDetails/{fileId}";
    }

    @PostMapping("/attachmentDetails/report/{fileId}")
    public String addReport(
            @PathVariable String fileId,
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute Report reportForm) throws Exception {
        User user = userService.findByUsername(userDetails.getUsername());

        Attachment attachment = attachmentService.getAttachment(fileId);

        Report report = new Report();
        report.setReportReason(reportForm.getReportReason());
        report.setAttachment(attachment);
        report.setUser(user);

        reportService.createReport(report);

        return "redirect:/attachmentDetails/{fileId}";
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

    @GetMapping("/filterByTag")
    public String filterByTag(@RequestParam String tag, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Attachment> filteredAttachments = tagService.getAttachmentsByTag(tag);
        List<String> allTags = tagService.getAllTags();

        User currentUser = userService.findByUsername(userDetails.getUsername());

        List<Attachment> otherUserAttachments = new ArrayList<>();
        List<Attachment> currentUserAttachments = new ArrayList<>();

        for (Attachment attachment : attachmentService.getAllAttachments()) {
            if (attachment.getUser() != null && attachment.getUser().equals(currentUser)) {
                currentUserAttachments.add(attachment);
            } else {
                otherUserAttachments.add(attachment);
            }
        }

        model.addAttribute("otherUserAttachments", filteredAttachments);
        model.addAttribute("allTags", allTags);
        model.addAttribute("uploadForm", new UploadForm());
        model.addAttribute("fileList", attachmentService.getAllAttachments());
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "student-index";
    }

    @GetMapping("/pdf-attachments")
    public String showPdfAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> pdfAttachments = attachmentService.findByFileType("application/pdf");
        model.addAttribute("pdfAttachments", pdfAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "attachments-pdf";
    }

    @GetMapping("/msword-attachments")
    public String showMsWordAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> mswordAttachments = attachmentService
                .findByFileType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        model.addAttribute("mswordAttachments", mswordAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "attachments-msword";
    }

    @GetMapping("/image-attachments")
    public String showImageAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> imageAttachments = attachmentService.findByFileTypeStartingWith("image/");
        model.addAttribute("imageAttachments", imageAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "attachments-image";
    }

    @GetMapping("/java-attachments")
    public String showJavaAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> javaAttachments = attachmentService.findByFileType("application/octet-stream");
        model.addAttribute("javaAttachments", javaAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "attachments-java";
    }

    @GetMapping("/zip-attachments")
    public String showZipAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> zipAttachments = attachmentService.findByFileType("application/zip");
        model.addAttribute("zipAttachments", zipAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "attachments-zip";
    }

    @GetMapping("/python-attachments")
    public String showPythonAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> pythonAttachments = attachmentService.findByFileType("text/x-python-script");
        model.addAttribute("pythonAttachments", pythonAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "attachments-python";
    }
}
