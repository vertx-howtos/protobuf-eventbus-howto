package io.vertx.howtos.protobuf.eventbus;

import com.google.protobuf.GeneratedMessageV3;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;

import java.io.*;

import static io.vertx.core.impl.SerializableUtils.fromBytes;
import static io.vertx.core.impl.SerializableUtils.toBytes;

public class ProtobufCodec implements MessageCodec<GeneratedMessageV3, GeneratedMessageV3> {

  private static final String PROTOS_PACKAGE_NAME = AddressBookProtos.class.getPackageName() + ".";

  @Override
  public void encodeToWire(Buffer buffer, GeneratedMessageV3 o) {
    byte[] bytes = toBytes(o);
    buffer.appendInt(bytes.length);
    buffer.appendBytes(bytes);
  }

  @Override
  public GeneratedMessageV3 decodeFromWire(int pos, Buffer buffer) {
    int length = buffer.getInt(pos);
    pos += 4;
    byte[] bytes = buffer.getBytes(pos, pos + length);
    return (GeneratedMessageV3) fromBytes(bytes, CheckedClassNameObjectInputStream::new);
  }

  @Override
  public GeneratedMessageV3 transform(GeneratedMessageV3 o) {
    return o;
  }

  @Override
  public String name() {
    return "ProtobufCodec";
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }

  public boolean test(String name) {
    return name.startsWith(PROTOS_PACKAGE_NAME);
  }

  private static class CheckedClassNameObjectInputStream extends ObjectInputStream {

    CheckedClassNameObjectInputStream(InputStream in) throws IOException {
      super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
      String name = desc.getName();
      if (name.startsWith("com.google.protobuf.")
        || name.startsWith(PROTOS_PACKAGE_NAME)
        || EventBus.DEFAULT_SERIALIZABLE_CHECKER.apply(name)) {
        return super.resolveClass(desc);
      }
      throw new InvalidClassException("Class not allowed: " + name);
    }
  }
}
