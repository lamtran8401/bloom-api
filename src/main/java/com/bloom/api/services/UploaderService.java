package com.bloom.api.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploaderService {
    private final Cloudinary cloudinary;
    private final Logger logger = LoggerFactory.getLogger(UploaderService.class);

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        Map<String, Object> options = Map.of(
            "folder", "bloom/" + folder,
            "use_filename", true,
            "unique_filename", true,
            "resource_type", "image"
        );
        return cloudinary.uploader().upload(file.getBytes(), options).get("url").toString();
    }

    public List<String> uploadImages(List<MultipartFile> files, String folder) {
        List<String> urls = new ArrayList<>();
        files.forEach(file -> {
            try {
                urls.add(uploadImage(file, folder));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        });

        return urls;
    }

    public void deleteImageFolder(String folder) {
        try {
            String folderPath = "bloom/" + folder;
            cloudinary.api().deleteAllResources(Map.of("public_id", folderPath));
            cloudinary.api().deleteFolder(folderPath, ObjectUtils.emptyMap());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
