package com.github.jitwxs.sample.grpc.pcbook;

import com.github.jitwxs.sample.protobuf.grpc.pcbook.Filter;
import com.github.jitwxs.sample.protobuf.grpc.pcbook.Laptop;
import io.grpc.Context;

public interface LaptopStore {
    void Save(Laptop laptop) throws Exception;

    Laptop Find(String id);

    void Search(Context ctx, Filter filter, LaptopStream stream);
}