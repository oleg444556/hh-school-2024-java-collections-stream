package tasks;

import common.Person;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/*
Задача 2
На вход принимаются две коллекции объектов Person и величина limit
Необходимо объеденить обе коллекции
отсортировать персоны по дате создания и выдать первые limit штук.
 */
public class Task2 {

  public static List<Person> combineAndSortWithLimit(Collection<Person> persons1,
                                                     Collection<Person> persons2,
                                                     int limit) {
    Stream<Person> stream1 = persons1.stream();
    Stream<Person> stream2 = persons2.stream();
    return Stream.concat(stream1, stream2)
        .sorted(Comparator.comparing(Person::createdAt))
        .limit(limit)
        .toList();
  }
}
