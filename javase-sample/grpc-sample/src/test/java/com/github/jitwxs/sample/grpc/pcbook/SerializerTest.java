package com.github.jitwxs.sample.grpc.pcbook;

import com.github.jitwxs.sample.protobuf.grpc.pcbook.Laptop;
import com.google.protobuf.util.JsonFormat;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SerializerTest {
    public void WriteBinaryFile(Laptop laptop, String filename) throws IOException {
        FileOutputStream outStream = new FileOutputStream(filename);
        laptop.writeTo(outStream);
        outStream.close();
    }

    public Laptop ReadBinaryFile(String filename) throws IOException {
        FileInputStream inStream = new FileInputStream(filename);
        Laptop laptop = Laptop.parseFrom(inStream);
        inStream.close();
        return laptop;
    }

    public void WriteJSONFile(Laptop laptop, String filename) throws IOException {
        JsonFormat.Printer printer = JsonFormat.printer()
                .includingDefaultValueFields()
                .preservingProtoFieldNames();

        String jsonString = printer.print(laptop);

        FileOutputStream outStream = new FileOutputStream(filename);
        outStream.write(jsonString.getBytes());
        outStream.close();
    }

    @Test
    public void test() throws Exception {
        Generator generator = new Generator();
        Laptop laptop1 = generator.NewLaptop();

        SerializerTest serializerTest = new SerializerTest();

        serializerTest.WriteBinaryFile(laptop1, "laptop.bin");

        Laptop laptop2 = serializerTest.ReadBinaryFile("laptop.bin");
        serializerTest.WriteJSONFile(laptop2, "laptop.json");

        assertEquals(laptop1, laptop2);
    }
}
