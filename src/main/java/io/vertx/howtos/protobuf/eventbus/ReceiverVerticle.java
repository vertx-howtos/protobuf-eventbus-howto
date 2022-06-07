package io.vertx.howtos.protobuf.eventbus;

import io.vertx.core.AbstractVerticle;

public class ReceiverVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.eventBus().<GreetingRequest>consumer("greetings", msg -> {
      GreetingRequest request = msg.body();
      System.out.printf("Received request = %s (%d)%n", request.getName(), System.identityHashCode(request));
      String greeting = String.format("Hello %s", request.getName());
      GreetingReply reply = GreetingReply.newBuilder().setMessage(greeting).build();
      System.out.printf("Sending reply = %s (%d)%n", reply.getMessage(), System.identityHashCode(reply));
      msg.reply(reply);
    });
  }
}
