package com.microservice.cloud.service;

import com.microservice.cloud.controller.UploadController;
import com.microservice.cloud.exceptions.InternalServerErrorException;
import com.microservice.cloud.exceptions.NotFoundException;
import com.microservice.cloud.models.FileResponse;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

@Service
public class StorageService {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    private final Path location;

    public StorageService(@Value("${file.upload.dir}") String uploadDir) {
        this.location = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            if (!Files.exists(this.location)) {
                Files.createDirectories(this.location);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage(), ex);
        }
    }

    public FileResponse saveFile(String path, MultipartFile file, boolean keepName) {
        try {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String name = originalFileName;
            if (!keepName) {
                name = System.currentTimeMillis() + "." + FilenameUtils.getExtension(originalFileName);
            }
            Calendar now = Calendar.getInstance();
            String year = String.valueOf(now.get(Calendar.YEAR));
            String month = String.valueOf(now.get(Calendar.MONTH));
            String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
            path = path + year + "/" + month + "/" + day + "/";
            Path filePath = this.location.resolve(path).normalize();
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath);
            }

            Path fileName = this.location.resolve(this.location + "/" + path + name).normalize();
            Files.copy(file.getInputStream(), fileName, StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("load/")
                    .path(path)
                    .path(name)
                    .toUriString();
            return new FileResponse(name, fileDownloadUri, file.getContentType());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage(), ex);
        }
    }

    public Resource loadFile(String path) {
        try {
            Path filePath = this.location.resolve(path).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new NotFoundException("Not found");
            }
        } catch (MalformedURLException ex) {
            logger.error(ex.getMessage());
            throw new NotFoundException("Not found", ex);
        }
    }
}
