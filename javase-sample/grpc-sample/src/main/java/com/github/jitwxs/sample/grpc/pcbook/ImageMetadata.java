package com.github.jitwxs.sample.grpc.pcbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageMetadata {
    private String laptopID;
    private String type;
    private String path;
}
