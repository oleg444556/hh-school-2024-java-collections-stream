package tasks;

import common.Person;
import common.PersonService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
// Асимптотика работы O(n)
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);
    Map<Integer, Person> idToPerson = new HashMap<>();
    for (Person person : persons) {
      idToPerson.put(person.id(), person);
    }
    List<Person> orderedPersons = new ArrayList<>();
    for (int id : personIds) {
      orderedPersons.add(idToPerson.get(id));
    }
    return orderedPersons;
  }
}
