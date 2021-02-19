package com.awsjwtservice.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import com.awsjwtservice.storage.StorageFileNotFoundException;
import com.awsjwtservice.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }


    @GetMapping("/files")
    public String listUploadedFiles(Model model) throws IOException {
        List fileUrls = storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList());

        System.out.println(fileUrls);

        if(fileUrls.size() > 0) {
            model.addAttribute("fileExists", true);
        }

        List<String> imgUrls = storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList());

        model.addAttribute("files", imgUrls);

        return "files";
    }


    // 다운이 됩니다
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/files";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

//    [http://localhost:8080/files/Screen%20Shot%202021-01-23%20at%208.08.20%20AM.png,
//    http://localhost:8080/files/Screen%20Shot%202021-01-23%20at%208.17.07%20AM.png]
//    [http://localhost:8080/files/Screen%20Shot%202021-01-23%20at%208.08.20%20AM.png,
//    http://localhost:8080/files/Screen%20Shot%202021-01-23%20at%208.17.07%20AM.png]

}
