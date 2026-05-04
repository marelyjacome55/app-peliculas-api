package com.example.demo.service;

// PATRÓN SERVICE LAYER:
// Concentra la lógica de negocio para gestionar la carga de imágenes en Cloudinary.
// Centraliza la comunicación con el servicio externo, facilitando mantenimiento y testing.
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String subirImagen(MultipartFile file) throws Exception {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "image")
        );

        return uploadResult.get("secure_url").toString();
    }
}