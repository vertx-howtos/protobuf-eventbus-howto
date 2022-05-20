package io.vertx.howtos.protobuf.eventbus;

import io.vertx.core.AbstractVerticle;

import java.util.UUID;

public class MainVerticle extends AbstractVerticle {

  private AddressBook addressBook;
  private Person me;

  @Override
  public void start() throws Exception {

    String uuid = UUID.randomUUID().toString();

    me = Person.newBuilder()
      .setName("John " + uuid)
      .addPhones(
        Person.PhoneNumber.newBuilder()
          .setNumber(uuid)
          .setType(Person.PhoneType.MOBILE))
      .build();

    addressBook = AddressBook.newBuilder()
      .addPeople(me)
      .build();


    vertx.eventBus().<Person>consumer("newMember", msg -> {
      Person person = msg.body();
      if (person.equals(me)) {
        return;
      }
      System.out.printf("Got message from new member:%n%s%n", person);
      addressBook = AddressBook.newBuilder(addressBook)
        .addPeople(person)
        .build();
      System.out.printf("Updated address book:%n%s%n", addressBook);
      vertx.eventBus().publish("oldMember", me);
    });

    vertx.eventBus().<Person>consumer("oldMember", msg -> {
      Person person = msg.body();
      System.out.printf("Got message from old member:%n%s%n", person);
      if (!addressBook.getPeopleList().contains(person)) {
        addressBook = AddressBook.newBuilder(addressBook)
          .addPeople(person)
          .build();
        System.out.printf("Updated address book:%n%s%n", addressBook);
      }
    });

    vertx.eventBus().publish("newMember", me);
  }
}
