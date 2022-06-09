package io.vertx.howtos.protobuf.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;

public class SenderVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    vertx.setPeriodic(5000, l -> {
      var request = GreetingRequest.newBuilder().setName("Jane Doe").build();
      System.out.printf("Sending request = %s (%d)%n", request.getName(), System.identityHashCode(request));
      vertx.eventBus().<GreetingReply>request("greetings", request)
        .map(Message::body)
        .onFailure(Throwable::printStackTrace)
        .onSuccess(reply -> System.out.printf("Received reply = %s (%d)%n", reply.getMessage(), System.identityHashCode(reply)));
    });
  }
}
