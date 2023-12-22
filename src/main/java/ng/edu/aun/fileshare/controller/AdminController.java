package ng.edu.aun.fileshare.controller;

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
import ng.edu.aun.fileshare.model.SearchForm;
import ng.edu.aun.fileshare.model.Tag;
import ng.edu.aun.fileshare.model.UploadForm;
import ng.edu.aun.fileshare.model.User;
import ng.edu.aun.fileshare.service.AttachmentService;
import ng.edu.aun.fileshare.service.CommentService;
import ng.edu.aun.fileshare.service.ReportService;
import ng.edu.aun.fileshare.service.RoleService;
import ng.edu.aun.fileshare.service.TagService;
import ng.edu.aun.fileshare.service.UserService;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/admin/index")
    public String adminIndex(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> files = attachmentService.getAllAttachments();
        long numberOfUsers = userService.countUsers();
        long numberOfFiles = attachmentService.countAttachments();

        // Add information to the model
        model.addAttribute("files", files);
        model.addAttribute("numberOfUsers", numberOfUsers);
        model.addAttribute("numberOfFiles", numberOfFiles);
        model.addAttribute("numberOfDownloads", attachmentService.getTotalDownloadCount());
        model.addAttribute("numberOfPDFFiles", attachmentService.countByFileType("application/pdf"));
        model.addAttribute("numberOfMSWordFiles", attachmentService
                .countByFileType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        model.addAttribute("numberOfPythonFiles", attachmentService.countByFileType("text/x-python-script"));
        model.addAttribute("numberOfJavaFiles", attachmentService.countByFileType("application/octet-stream"));
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());

        return "admin-index";
    }

    @GetMapping("/admin/search")
    public String adminSearchForm(Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("searchForm", new SearchForm());
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "admin-search";
    }

    @PostMapping("/admin/search")
    public String search(@ModelAttribute("searchForm") SearchForm searchForm, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        String fileName = searchForm.getFileName();
        List<Attachment> searchResults = attachmentService.searchByFileName(fileName);
        User currentUser = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "admin-search";
    }

    @GetMapping("/admin/pdf-attachments")
    public String showPdfAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> pdfAttachments = attachmentService.findByFileType("application/pdf");
        model.addAttribute("pdfAttachments", pdfAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "admin-attachments-pdf";
    }

    @GetMapping("/admin/msword-attachments")
    public String showMsWordAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> mswordAttachments = attachmentService
                .findByFileType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        model.addAttribute("mswordAttachments", mswordAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "admin-attachments-msword";
    }

    @GetMapping("/admin/image-attachments")
    public String showImageAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> imageAttachments = attachmentService.findByFileTypeStartingWith("image/");
        model.addAttribute("imageAttachments", imageAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "admin-attachments-image";
    }

    @GetMapping("/admin/zip-attachments")
    public String showZipAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> zipAttachments = attachmentService.findByFileType("application/zip");
        model.addAttribute("zipAttachments", zipAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "admin-attachments-zip";
    }

    @GetMapping("/admin/java-attachments")
    public String showJavaAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> javaAttachments = attachmentService.findByFileType("application/octet-stream");
        model.addAttribute("javaAttachments", javaAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "admin-attachments-java";
    }

    @GetMapping("/admin/python-attachments")
    public String showPythonAttachments(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByUsername(userDetails.getUsername());
        List<Attachment> pythonAttachments = attachmentService.findByFileType("text/x-python-script");
        model.addAttribute("pythonAttachments", pythonAttachments);
        model.addAttribute("firstName", currentUser.getFirst_name());
        model.addAttribute("lastName", currentUser.getLast_name());
        return "admin-attachments-python";
    }

    @GetMapping("/admin/upload")
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

        return "admin-uploadFile";
    }

    @PostMapping("/admin/upload")
    public String handleFileUpload(
            @ModelAttribute("uploadForm") UploadForm uploadForm,
            @RequestParam("tags") Set<String> tags) throws Exception {

        try {
            MultipartFile file = uploadForm.getFile();
            System.out.println("File: " + file);
            String title = uploadForm.getTitle();
            String description = uploadForm.getDescription();
            String downloadURl = "";
            Set<Tag> tagEntities = tags.stream().map(Tag::new).collect(Collectors.toSet());

            Attachment attachment = attachmentService.saveAttachment(file, title, description, tagEntities);

            downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(attachment.getAttachment_id())
                    .toUriString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/index";
    }

    @GetMapping("/admin/users")
    public String userManagement(Model model) {
        List<User> userList = userService.get();
        User user = userService.getCurrentUser();
        model.addAttribute("firstName", user.getFirst_name());
        model.addAttribute("lastName", user.getLast_name());
        model.addAttribute("users", userList);
        return "admin-users";
    }

    @RequestMapping(value = "/admin/users/delete/{id}", method = { RequestMethod.DELETE, RequestMethod.GET })
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/manage-role/{id}")
    public String manageRoles(@PathVariable Long id, Model model) {
        User user = userService.findById(id).orElse(null);
        User userFullName = userService.getCurrentUser();
        model.addAttribute("firstName", userFullName.getFirst_name());
        model.addAttribute("lastName", userFullName.getLast_name());
        model.addAttribute("user", user);
        model.addAttribute("userRoles", roleService.getUserRoles(user));
        model.addAttribute("userNotRoles", roleService.getUserNotRoles(user));
        return "admin-manage-role";
    }

    @RequestMapping("/admin/users/manage-role/assign/{userId}/{roleId}")
    public String assignRole(@PathVariable Long userId,
            @PathVariable Long roleId) {
        roleService.assignUserRole(userId, roleId);
        return "redirect:/admin/users/manage-role/" + userId;
    }

    @RequestMapping("/admin/users/manage-role/unassign/{userId}/{roleId}")
    public String unAssignRole(@PathVariable Long userId,
            @PathVariable Long roleId) {
        roleService.unassignedRole(userId, roleId);
        return "redirect:/admin/users/manage-role/" + userId;
    }

    @GetMapping("/admin/users/new")
    public String newUser(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User userFullName = userService.getCurrentUser();
        model.addAttribute("firstName", userFullName.getFirst_name());
        model.addAttribute("lastName", userFullName.getLast_name());
        return "admin-new-user";
    }

    @PostMapping("/admin/users/new")
    public RedirectView addNew(User user, RedirectAttributes redir) {
        userService.save(user);
        RedirectView redirectView = new RedirectView("/admin/users", true);
        redir.addFlashAttribute("message",
                "You successfully registered! You can now login");
        return redirectView;
    }

    @RequestMapping(value = "/admin/delete/{fileId}", method = { RequestMethod.DELETE,
            RequestMethod.GET })
    public String deleteFile(@PathVariable String fileId) throws Exception {
        attachmentService.deleteAttachment(fileId);
        return "redirect:/admin/index";
    }

    @RequestMapping(value = "/admin/pdf/delete/{fileId}", method = { RequestMethod.DELETE,
            RequestMethod.GET })
    public String deletePdfFile(@PathVariable String fileId) throws Exception {
        attachmentService.deleteAttachment(fileId);
        return "redirect:/admin/pdf-attachments";
    }

    @RequestMapping(value = "/admin/python/delete/{fileId}", method = { RequestMethod.DELETE,
            RequestMethod.GET })
    public String deletePythonFile(@PathVariable String fileId) throws Exception {
        attachmentService.deleteAttachment(fileId);
        return "redirect:/admin/python-attachments";
    }

    @RequestMapping(value = "/admin/word/delete/{fileId}", method = { RequestMethod.DELETE,
            RequestMethod.GET })
    public String deleteWordFile(@PathVariable String fileId) throws Exception {
        attachmentService.deleteAttachment(fileId);
        return "redirect:/admin/msword-attachments";
    }

    @RequestMapping(value = "/admin/image/delete/{fileId}", method = { RequestMethod.DELETE,
            RequestMethod.GET })
    public String deleteImageFile(@PathVariable String fileId) throws Exception {
        attachmentService.deleteAttachment(fileId);
        return "redirect:/admin/image-attachments";
    }

    @RequestMapping(value = "/admin/zip/delete/{fileId}", method = { RequestMethod.DELETE,
            RequestMethod.GET })
    public String deleteZipFile(@PathVariable String fileId) throws Exception {
        attachmentService.deleteAttachment(fileId);
        return "redirect:/admin/zip-attachments";
    }

    @RequestMapping(value = "/admin/java/delete/{fileId}", method = { RequestMethod.DELETE,
            RequestMethod.GET })
    public String deleteJavaFile(@PathVariable String fileId) throws Exception {
        attachmentService.deleteAttachment(fileId);
        return "redirect:/admin/java-attachments";
    }

    @GetMapping("/admin/reports")
    public String showReports(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User userFullName = userService.getCurrentUser();
        model.addAttribute("firstName", userFullName.getFirst_name());
        model.addAttribute("lastName", userFullName.getLast_name());
        model.addAttribute("reports", reportService.getAllReports());
        return "admin-reports";
    }

    @RequestMapping(value = "/admin/report/delete/{fileId}", method = { RequestMethod.DELETE,
            RequestMethod.GET })
    public String deleteReportFile(@PathVariable String fileId) throws Exception {
        attachmentService.deleteAttachment(fileId);
        return "redirect:/admin/reports";
    }

    @RequestMapping(value = "/admin/report/clear/{reportId}", method = { RequestMethod.DELETE, RequestMethod.GET })
    public String clearReport(@PathVariable Long reportId) throws Exception {
        reportService.deleteReport(reportId);
        return "redirect:/admin/reports";
    }
}
