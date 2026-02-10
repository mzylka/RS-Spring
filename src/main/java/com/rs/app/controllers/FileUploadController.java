package com.rs.app.controllers;

import com.rs.app.config.UploadProperties;
import com.rs.app.services.FileUploadService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/files")
public class FileUploadController {
    private final FileUploadService fileUploadService;
    private final String thumbnailsDirName;
    private final String logosDirName;
    private final String editorDirName;


    public FileUploadController(FileUploadService fileUploadService, UploadProperties uploadProperties){
        this.fileUploadService = fileUploadService;
        this.thumbnailsDirName = uploadProperties.getThumbnails();
        this.logosDirName = uploadProperties.getLogos();
        this.editorDirName = uploadProperties.getEditor();
    }

    @GetMapping("/{dir}")
    public ResponseEntity<List<String>> listOfFiles(@PathVariable String dir, @RequestParam(defaultValue = "1") int page){
        if (Objects.equals(dir, thumbnailsDirName)){
            return ResponseEntity.ok(fileUploadService.loadAllThumbnails(page));
        } else if (Objects.equals(dir, logosDirName)) {
            return ResponseEntity.ok(fileUploadService.loadAllLogos(page));
        } else if (Objects.equals(dir, editorDirName)) {
            return ResponseEntity.ok(fileUploadService.loadAllEditorImage(page));
        }
        throw new IllegalArgumentException("Wrong dir!");
    }

    @PostMapping("/upload-thumbnail")
    public ResponseEntity<Map<String, Object>> uploadThumb(@RequestParam("file")MultipartFile file){
        Map<String, Object> map = fileUploadService.uploadThumbnail(file);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/upload-logo")
    public ResponseEntity<Map<String, Object>> uploadLogo(@RequestParam("file")MultipartFile file){
        Map<String, Object> map = fileUploadService.uploadLogo(file);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/upload-editor")
    public ResponseEntity<Map<String, Object>> uploadEditor(@RequestParam("file")MultipartFile file){
        Map<String, Object> map = fileUploadService.uploadFromEditor(file);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/{dir}/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String dir, @PathVariable String filename) {
        if (!List.of(thumbnailsDirName, logosDirName, editorDirName).contains(dir)){
            throw new IllegalArgumentException("Bad dir!");
        }

        Resource fileResource = fileUploadService.loadAsResource(filename, dir);
        String mimeType = fileUploadService.getMimeType(filename, dir);

        if (fileResource == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + fileResource.getFilename() + "\""
        ).body(fileResource);
    }
}
