package io.vertx.howtos.protobuf.eventbus;

import io.vertx.core.eventbus.EventBus;

import java.io.*;

class CheckedClassNameObjectInputStream extends ObjectInputStream {

  CheckedClassNameObjectInputStream(InputStream in) throws IOException {
    super(in);
  }

  @Override
  protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
    var name = desc.getName();
    if (name.startsWith("com.google.protobuf.")
      || name.startsWith(ProtobufCodec.PROTOS_PACKAGE_NAME)
      || EventBus.DEFAULT_SERIALIZABLE_CHECKER.apply(name)) {
      return super.resolveClass(desc);
    }
    throw new InvalidClassException("Class not allowed: " + name);
  }
}
