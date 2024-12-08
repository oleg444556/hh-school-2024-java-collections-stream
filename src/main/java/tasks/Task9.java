package tasks;

import common.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    // Убран ненужный if, плюс убран remove, при каждом вызове метода он удаляет первый элемент из списка,
    // что может привести к проблемам при многократной обработке одного и того же списка.
    // Вместо него использую skip(1)
    return persons.stream()
        .skip(1)
        .map(Person::firstName)
        .collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    // distinct() излишен, при создании множества дубликаты и так будут убраны,
    // и в таком случае использование потока будет также излишним, логичнее сразу создать множество
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    // Заменил дерево if на один поток из строк, который склеивается в конце в строку, разделенную пробелами,
    // null строки будут отфильтрованы, также исправлен неправильный if с дубликатом person.secondName()
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    // Убран initialCapacity у map, произойдет много лишних выделений памяти
    // под новую расширенную хеш-таблицу; map переименована для лучшей читаемости
    Map<Integer, String> personsIdToName = new HashMap<>();
    for (Person person : persons) {
      if (!personsIdToName.containsKey(person.id())) {
        personsIdToName.put(person.id(), convertPersonToString(person));
      }
    }
    return personsIdToName;
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    // Заменил медленно работающий алгоритм по перебору всех элементов каждого из списков на
    // поиск пересечения 2-х множеств и проверки на пустоту
    Set<Person> set1 = new HashSet<>(persons1);
    Set<Person> set2 = new HashSet<>(persons2);
    set1.retainAll(set2);
    return !set1.isEmpty();
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    // Следует заменить странную конструкцию с переменной класса на .count()
    return numbers.filter(num -> num % 2 == 0)
        .count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    // Такой результат получается вследствие устройства хеш-таблицы
    // Хеш-функция от целого числа - это само это число,
    // Хеш-таблица определяет в какой бакет попадет очередной элемент по следующей формуле:
    // bucketIndex = (hashCode) % (capacity−1), вместимость хеш-таблицы можно оценить из того,
    // что таблица расширяется вдвое при достижении определенного порога - capacity * loadFactor.
    // У нас хеш-сет создается из 10000 элементов и выделится ближайшая степень двойки большая чем
    // (10000 / 0.75) = 13333,33. Это будет 16384. А значит каждое из чисел попадет в отдельный бакет в нашем случае,
    // вне зависимости от их порядка, и попадут они в бакеты в отсортированном порядке, тк
    // 1 % 16383 = 1 (бакет с индексом 1), 2 % 16383 = 2 и тд, 10000 % 16383 = 10000.
    // toString() печатает элементы множества в том порядке в каком получает их от итератора, а
    // итератор HashSet проходится по бакетам в порядке возрастания. И получается отсортированный вывод.
    // Это просто красивые числа для которых это выполняется, если взять например 10 чисел от 1 с шагом 3, то порядка
    // уже не будет. [1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31] - изначальный список
    // [16, 1, 19, 4, 22, 7, 25, 10, 28, 13, 31] - HashSet, и действительно 1 % 15 = 1, 16 % 15 = 1, те все верно
    // и 16 и 1 находятся в одном и том же бакете с индексом 1, что и видно в выводе.
    List<Integer> integers = IntStream.rangeClosed(1, 10000)
        .boxed()
        .collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString()
        .equals(set.toString());
  }
}
