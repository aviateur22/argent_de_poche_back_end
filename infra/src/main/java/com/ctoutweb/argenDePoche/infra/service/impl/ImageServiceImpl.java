package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${folder.image.path}")
    String folderPath;

    @Value("${default.child_image.name}")
    String defaultChildImageName;

    @Override
    public Mono<String> saveImage(FilePart childImageFile, String childImageRandomName) {
        var uniqueChildRandomImageName = childImageRandomName + getFileExtension(childImageFile);
        Path destination = Paths.get(folderPath, uniqueChildRandomImageName);

        return childImageFile.transferTo(destination)
                .thenReturn(childImageRandomName);
    }

    @Override
    public Mono<Void> deleteImage(String imageNameToDelete) {

        // On ne supprime pas l'image qui permets de créer un compte d'argent de poche
        if(imageNameToDelete.equalsIgnoreCase(defaultChildImageName))
            return Mono.empty();


        Path destination = Paths.get(folderPath);

        return Mono.fromRunnable(() -> {
            try (Stream<Path> files = Files.list(destination)) {
                files
                        .filter(path -> path.getFileName().toString().startsWith(imageNameToDelete + "."))
                        .findFirst()
                        .ifPresent(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    /**
     * Renvoie l'extension de l'image
     *
     * @param childImageFile L'image recu par le client
     *
     * @return L'extension
     */
    private String getFileExtension(FilePart childImageFile) {
        var initialFileName = childImageFile.filename();
        // Extract extension
        String extension = "";

        int dotIndex = initialFileName.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = initialFileName.substring(dotIndex);
        }

        return extension;
    }
}
