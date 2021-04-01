package com.awsjwtservice.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import com.awsjwtservice.config.annotation.LoginUser;
import com.awsjwtservice.domain.Gallery;
import com.awsjwtservice.dto.GalleryDto;
import com.awsjwtservice.dto.SessionUserDto;
import com.awsjwtservice.s3.GalleryService;
import com.awsjwtservice.s3.S3Service;
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
    private S3Service s3Service;
    @Autowired
    private GalleryService galleryService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/gallery")
    public String dispWrite() {

        return "gallery";
    }

    @GetMapping("/s3basket")
    public String images(Model model) {

        // db 에서 찾습니다.
        List<Gallery> files = galleryService.getAllFiles();

//        List<String> imgUrls = new ArrayList<>();

        List<String> imgUrls = files.stream().map(each -> each.getFilePath()).collect(Collectors.toList());
        // prolly best to return dto here
//        List<GalleryDto> resultDtos = new ArrayList<>();
//
//        GalleryDto.builder().build();

        model.addAttribute("files", imgUrls);


        return "s3basket";
    }

    @PostMapping("/gallery/delete")
    public void deleteFile(@RequestParam String fileseq, @LoginUser SessionUserDto loginUser) throws IOException {
        System.out.println(fileseq + "fileseq");
        Long fileSeq = Long.valueOf(fileseq);
        galleryService.deleteFile(fileSeq);
    }

    @PostMapping("/gallery")
    public String execWrite(GalleryDto galleryDto, MultipartFile file, @LoginUser SessionUserDto loginUser) throws IOException {
        // s3 에 먼저 올립니다.

        String imgPath = s3Service.upload(file);
        galleryDto.setFilePath(imgPath);


        // login안되있을시를 체크해야합니다. 에러
//        java.lang.NullPointerException: null 나옵니다.    try and catch 로하면 더 좋을거같습니다.

        if(loginUser != null) {
            // 누가 올렸는지도 이름으로 저장하면 좋으려나?
            String username = loginUser.getUsername();
            Long userSeq = loginUser.getUserSeq();
            galleryDto.setUserSeq(userSeq);

            // db 에 url, etc. 를 저장 합니다
            galleryService.savePost(galleryDto);
        }


        return "redirect:/gallery";
    }
    @PostMapping("/gallery/upload")
    public String galleryUpload(GalleryDto galleryDto, MultipartFile file, @LoginUser SessionUserDto loginUser) throws IOException {
        // s3 에 먼저 올립니다.




        // login안되있을시를 체크해야합니다. 에러
//        java.lang.NullPointerException: null 나옵니다.    try and catch 로하면 더 좋을거같습니다.

        if(loginUser != null) {
            String imgPath = s3Service.upload(file);
            galleryDto.setFilePath(imgPath);

            // 누가 올렸는지도 이름으로 저장하면 좋으려나?
            String username = loginUser.getUsername();
            Long userSeq = loginUser.getUserSeq();
            galleryDto.setUserSeq(userSeq);

            // db 에 url, etc. 를 저장 합니다
            galleryService.savePost(galleryDto);
        } else {
            return "redirect:/oauth_login";     // 안된다
        }
//        if(imgPath != null || imgPath != ""){
//            String[] urls = imgPath.split("https://", 2);
//
//            return urls[1];
//        } else {
//            return "redirect:/gallery";
//        }


//        return imgPath;
        return "redirect:/gallery";
    }


//    @GetMapping("/s3basket/{fileseq:.+}")
//    public String getImgFile(Model model, @PathVariable String fileseq) throws IOException {
////            Long fileSeq = Long.valueOf(fileseq);
////            Gallery file = galleryService.getFile(fileSeq);
////            System.out.println(file);
//////        Resource file = storageService.loadAsResource(filename);
////
//        return "";
//    }

    @GetMapping("/files/upload")
    public String uploadLocalFile(Model model) throws IOException {


        return "fileUpload";
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
