package com.rs.app.services;

import com.rs.app.config.UploadProperties;
import com.rs.app.controllers.FileUploadController;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileUploadService {
    private final Path rootLocation;
    private final Path uploadThumbDirLocation;
    private final Path uploadLogoDirLocation;
    private final Path uploadEditorDirLocation;
    private final String thumbnailsDirName;
    private final String logosDirName;
    private final String editorDirName;
    private final Set<String> mimeTypes;

    public FileUploadService(UploadProperties props){
        String uploadDir = props.getRoot();
        this.thumbnailsDirName = props.getThumbnails();
        this.logosDirName = props.getLogos();
        this.editorDirName = props.getEditor();

        this.rootLocation = Paths.get(uploadDir);
        this.uploadThumbDirLocation = rootLocation.resolve(thumbnailsDirName);
        this.uploadLogoDirLocation = rootLocation.resolve(logosDirName);
        this.uploadEditorDirLocation = rootLocation.resolve(editorDirName);
        this.mimeTypes = props.getAllowedTypes();
    }

    enum imageType{
        THUMBNAIL,
        LOGO,
        EDITOR
    }

    private Map<String, Object> storeImage(MultipartFile file, Path location, imageType imageType){
        try{

            if (file.isEmpty()){
                throw new RuntimeException("File is empty!");
            }
            if (!mimeTypes.contains(file.getContentType())){
                throw new RuntimeException("File is not an image: jpg, png, webp");
            }

            String filename = UUID.randomUUID().toString();
            String extension = getExtension(file);
            String folderName;
            if (imageType == FileUploadService.imageType.EDITOR){
                folderName = editorDirName;
            }
            else if (imageType == FileUploadService.imageType.THUMBNAIL) {
                folderName = thumbnailsDirName;
            }
            else {
                folderName = logosDirName;
            }

            Path destinationFilePath = location.resolve(Paths.get(filename + extension)).normalize().toAbsolutePath();

            System.out.println(destinationFilePath);
            System.out.println(destinationFilePath.getParent());
            System.out.println(this.rootLocation.toAbsolutePath());

            if (!destinationFilePath.getParent().equals(location.toAbsolutePath())){
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            String urlMin = null;
            try (InputStream inputStream = file.getInputStream()){
                byte[] copyOfStream = inputStream.readAllBytes();
                Files.write(destinationFilePath, copyOfStream);

                if (imageType == FileUploadService.imageType.THUMBNAIL || imageType == FileUploadService.imageType.LOGO){
                    String filenameMin = filename + "-min";
                    Path destinationFilePathMin = location.resolve(Paths.get(filenameMin + extension)).normalize().toAbsolutePath();
                    saveMiniVersion(copyOfStream, destinationFilePathMin, extension);
                    urlMin = MvcUriComponentsBuilder
                            .fromMethodName(FileUploadController.class, "getFile", folderName, filenameMin + extension)
                            .build()
                            .toUriString();
                }
            } catch (FileAlreadyExistsException e) {
                throw new FileAlreadyExistsException("File already exists");
            }

            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "getFile", folderName, filename + extension)
                    .build()
                    .toUriString();
            Map<String, Object> mapToReturn = new HashMap<>();
            mapToReturn.put("url", url);
            if (urlMin != null){
                mapToReturn.put("minUrl:", urlMin);
            }
            mapToReturn.put("success", true);
            return mapToReturn;
        }
        catch (IOException e){
            throw new RuntimeException("Failed to store file", e);
        }
    }

    private BufferedImage rescaleImage(byte[] bytes, int width, int height) {
        try{
            Image image = ImageIO.read(new ByteArrayInputStream(bytes));
            if (image == null){
                throw new RuntimeException("Bad image format");
            }
            Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(scaled, 0, 0, null);
            g.dispose();
            System.out.println(bufferedImage);
            bufferedImage.getGraphics().drawImage(scaled, 0, 0, null);
            return bufferedImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveMiniVersion(byte[] bytes, Path destinationPath, String extension){
        BufferedImage bufferedImage = rescaleImage(bytes, 483, 252);
        try{
            InputStream minVersion = generateMinVersion(bufferedImage, extension);
            Files.copy(minVersion, destinationPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream generateMinVersion(BufferedImage image, String extension) {
        try {
            System.out.println("image type: " + image.getType());
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            String ex = extension.substring(1);
            boolean im = ImageIO.write(image, ex, os); //metoda nie zwraca prawidłowego outputstream
            if (!im){
                throw new RuntimeException("Writer hasn't been found");
            }
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null){
            throw new RuntimeException("Name of the file is empty!");
        }
        String ext = fileName.substring(fileName.lastIndexOf("."));
        if (ext.isEmpty()){
            throw new RuntimeException("The file doesn't have image extension!");
        }
        return ext;
    }

    public Map<String, Object> uploadThumbnail(MultipartFile file){
        return storeImage(file, uploadThumbDirLocation, imageType.THUMBNAIL);
    }

    public Map<String, Object> uploadLogo(MultipartFile file){
        return storeImage(file, uploadLogoDirLocation, imageType.LOGO);
    }

    public Map<String, Object> uploadFromEditor(MultipartFile file){
        return storeImage(file, uploadEditorDirLocation, imageType.EDITOR);
    }

    public Path load(String filename, String dir){
        return this.rootLocation.resolve(dir).resolve(filename);
    }

    public Resource loadAsResource(String filename, String dir){
        try {
            Path file = load(filename, dir);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()){
                return resource;
            }
            else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    public List<Path> loadAll(int pageNr, Path location){
        int pageSize = 20;
        try (Stream<Path> paths = Files.walk(location, 1)){
            return paths
                    .skip((long) (pageNr - 1) * pageSize)
                    .limit(pageSize)
                    .filter(path -> !path.equals(location))
                    .map(location::relativize)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> loadAllThumbnails(int pageNr){
        return loadAll(pageNr, this.uploadThumbDirLocation)
                .stream()
                .map(Path::toString)
                .toList();
    }

    public List<String> loadAllLogos(int pageNr){
        return loadAll(pageNr, this.uploadLogoDirLocation)
                .stream()
                .map(Path::toString)
                .toList();
    }

    public List<String> loadAllEditorImage(int pageNr){
        return loadAll(pageNr, this.uploadEditorDirLocation)
                .stream()
                .map(Path::toString)
                .toList();
    }

    public String getMimeType(String filename, String dir) {
        try {
            Path path = load(filename, dir);
            return Files.probeContentType(path);
        } catch (IOException e) {
            throw new RuntimeException("Can't get MimeType of the file: " + e);
        }
    }
}
