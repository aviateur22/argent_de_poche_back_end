package com.ctoutweb.argenDePoche.infra.service.impl;

import com.ctoutweb.argenDePoche.infra.exception.ChildImageNotFoundException;
import com.ctoutweb.argenDePoche.infra.service.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.ctoutweb.argenDePoche.infra.util.FileUtil.getFileExtension;

@Service
public class ImageServiceImpl implements ImageService {
    private static final Logger LOGGER = LogManager.getLogger();

    @Value("${folder.image.path}")
    String folderPath;

    @Value("${default.child.image.name}")
    String defaultChildImageName;

    @Override
    public Flux<DataBuffer> streamImage(String childImageNameWithExtension) {
        try {
            Path destination = Paths.get(folderPath, childImageNameWithExtension);
            return DataBufferUtils.read(destination, new DefaultDataBufferFactory(), 4096);
        } catch (Exception exception) {
            LOGGER.error(exception::getMessage);
            throw new ChildImageNotFoundException(String.format("L'image %s n'existe pas", childImageNameWithExtension));
        }
    }

    @Override
    public Mono<String> saveImage(FilePart childImageFile, String childImageRandomName) {
        var uniqueChildRandomImageName = String.format("%s.%s", childImageRandomName, getFileExtension(childImageFile));
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
}
