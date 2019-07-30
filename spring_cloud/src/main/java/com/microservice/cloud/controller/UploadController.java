package com.microservice.cloud.controller;

import com.microservice.cloud.service.StorageService;
import com.microservice.cloud.models.FileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    private final StorageService storageService;

    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public List<FileResponse> upload(@RequestParam Map<String, MultipartFile> files,
                                     @RequestParam(required = false) boolean keep_name) {
        List<FileResponse> list = new ArrayList<>();
        for (Map.Entry<String, MultipartFile> file : files.entrySet()) {
            FileResponse fileResponse = storageService.saveFile(file.getKey() + "/", file.getValue(), keep_name);
            list.add(fileResponse);
        }
        return list;
    }

    @GetMapping("/load/**")
    public ResponseEntity<Resource> load(HttpServletRequest request) {
        String path = request.getServletPath().replace("/load/", "");
        Resource resource = storageService.loadFile(path);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info(ex.getMessage());
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
