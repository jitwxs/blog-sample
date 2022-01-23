package com.github.jitwxs.sample.grpc.pcbook;

import com.github.jitwxs.sample.protobuf.grpc.pcbook.Laptop;

public interface LaptopStream {
    void Send(Laptop laptop);
}
