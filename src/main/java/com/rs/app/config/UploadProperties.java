package com.rs.app.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

@Component
@ConfigurationProperties(prefix = "upload")
@Validated
public class UploadProperties {
    @NotBlank
    private String root;

    @NotBlank
    private String thumbnails;

    @NotBlank
    private String logos;

    @NotBlank
    private String editor;

    @NotNull
    private Set<String> allowedTypes;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getLogos() {
        return logos;
    }

    public void setLogos(String logos) {
        this.logos = logos;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Set<String> getAllowedTypes() {
        return allowedTypes;
    }

    public void setAllowedTypes(Set<String> allowedTypes) {
        this.allowedTypes = allowedTypes;
    }
}
