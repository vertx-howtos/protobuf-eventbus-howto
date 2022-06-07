package io.vertx.howtos.protobuf.eventbus;

import com.google.protobuf.GeneratedMessageV3;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.impl.SerializableUtils;

public class ProtobufCodec implements MessageCodec<GeneratedMessageV3, GeneratedMessageV3> {

  static final String PROTOS_PACKAGE_NAME = "io.vertx.howtos.protobuf.eventbus.";

  @Override
  public void encodeToWire(Buffer buffer, GeneratedMessageV3 o) {
    var bytes = SerializableUtils.toBytes(o);
    buffer.appendInt(bytes.length);
    buffer.appendBytes(bytes);
  }

  @Override
  public GeneratedMessageV3 decodeFromWire(int pos, Buffer buffer) {
    var length = buffer.getInt(pos);
    pos += 4;
    var bytes = buffer.getBytes(pos, pos + length);
    return (GeneratedMessageV3) SerializableUtils.fromBytes(bytes, CheckedClassNameObjectInputStream::new);
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
    return -1; // -1 for a user codec
  }

  public boolean appliesTo(String className) {
    return className.startsWith(PROTOS_PACKAGE_NAME);
  }
}
