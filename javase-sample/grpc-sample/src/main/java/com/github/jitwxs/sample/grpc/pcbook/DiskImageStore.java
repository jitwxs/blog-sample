package com.github.jitwxs.sample.grpc.pcbook;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DiskImageStore implements ImageStore {
    private String imageFolder;
    private ConcurrentMap<String, ImageMetadata> data;

    public DiskImageStore(String imageFolder) {
        this.imageFolder = imageFolder;
        this.data = new ConcurrentHashMap<>(0);
    }

    @Override
    public String Save(String laptopID, String imageType, ByteArrayOutputStream imageData) throws IOException {
        String imageID = UUID.randomUUID().toString();
        String imagePath = String.format("%s/%s%s", imageFolder, imageID, imageType);

        FileOutputStream fileOutputStream = new FileOutputStream(imagePath);

        imageData.writeTo(fileOutputStream);
        fileOutputStream.close();

        ImageMetadata metadata = new ImageMetadata(laptopID, imageType, imagePath);
        data.put(imageID, metadata);

        return imageID;
    }
}
