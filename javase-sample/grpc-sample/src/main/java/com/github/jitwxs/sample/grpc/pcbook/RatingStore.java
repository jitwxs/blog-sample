package com.github.jitwxs.sample.grpc.pcbook;

public interface RatingStore {
    Rating Add(String laptopID, double score);
}
