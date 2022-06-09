package io.vertx.howtos.protobuf.eventbus;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.deployVerticle(new ReceiverVerticle());
    vertx.deployVerticle(new SenderVerticle());
  }
}
