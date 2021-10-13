wyrównanie poziomu - przepytanie z artykułu
punkty z zagadnieniami 
zadania z rozszerzeniem
do przeczytania - książeczka sun'a
java docki
moje notatki -> pigułka wiedzy


Plan:
1. Podstawy
   1. Tworzenie wątku
   2. Cykl życia
   3. Problemy programów wielowątkowych
   4. Synchronizacja
2. Locks 
3. Atomic
4. Futures 
5. Executors
6. Fork Join

0. Projekt
W projekcie z wczoraj dodajcie pakiet concurrency i skopiujcie tam klasę Util, z której będziemy korzystać dzisiaj.

1. Podstawowe zagadnienia programowania wielowątkowego

Pytania:
- Jakie problemy rozwiązuje programowanie wielowątkowe?
Wydajność, żeby szybciej było :)
Zrównoleglenie procesów - zbieranie danych z różnych miejsc, szczególnie widoczne jest to dla operacji IO, które są kilka rzędów wielkości wolniejsze niż działania w pamięci. Nawet przy pojedynczym procesorze z jednym core'm jest to bardzo pomocne i np na serwerze aplikacji pozwala na obsługę wielu rządań jednocześnie. 
Do cyklicznego wykonywania poleceń - np cykliczne odpytywanie strony


- Jak stworzyć wątek?
Rozszerzamy klasę Thread albo implementujemy interfejs Runnable. W tym drugim przypadku musimy klasę która rozszerza interfejs Runnable dodać do konstruktora klasy Thread. Następnie musimy wykonać metodę .start().
Wątek będzie wykonywał zadanie zdefiniowane w metodzie .run()
- Cykl życia wątku
NEW – nowo stworzony wątek który jeszcze nie wykonuje żadnej pracy. By przejść w kolejny stan potrzebujemy metody start()
RUNNABLE – wątek coś robi albo jest gotów do robienia tylko czeka na przypisanie zaobów
BLOCKED – wątek czeka na możliwość zajęcia monitora 
WAITING – wątek czeka na inny wątek bez limitu czasu 
metody takie jak:
- object.wait()
- thread.join() or
- LockSupport.park()
TIMED_WAITING – wątek czeka na inny wątek z limitem czasu 
metody:
- thread.sleep(long millis)
- wait(int timeout) or wait(int timeout, int nanos)
- thread.join(long millis)
- LockSupport.parkNanos
- LockSupport.parkUntil

TERMINATED – wątek zakończył pracę (wykonał metodę run())

Zadanie: 
	Napisz program który stworzy takie wątki że pozwoli wydrukować każdy stan z cyklu życia wątku.

Na czym polega różnica między Współbieżnością a równoległością wykonania pracy?
- współbieżność: przełączanie się między zadaniami, tak by każde popchnąć trochę do przodu
- równoległość: zadania wykonywane są jednocześnie (potrzeba > 1 core'a)

Jakie są główne problemy programowania wielowątkowego?
- zagłodzenie / starvation: w przypadku kiedy mamy wątki o różnych priorytetach 
- deadlock: wątek A czeka na zasoby będące w posiadaniu wątku B a wątek B czeka na zasoby będące w posiadaniu wątku A
- livelock: wątek wpada w nieskończoną nieproduktywną pętlę
- chaos: nie mamy do końca kontroli nad tym co dany wątek robi
- ciężko odtworzyć błąd przy tych samych danych 
- nieprzewidywalność, niepowtarzalność operacji

Jakie są podstawowe sposoby synchronizacji?
- synchronize na metodzie
- synchronize na obiekcie przekazanym przez parametr

Jak działa przerywanie wątków?
- Klasa Thread dostarcza metodę .stop(). Jednak nie powinno się z niej korzystać ponieważ zatrzymanie wątku który np posiada jakiś zasób, może np spowodować deadlock. 
- Wątek można przerwać wywołująć na nim metodę .interrupt(). Nie spowoduje to jednak zatrzymania wątku, tylko ustawi flagę interrupted na true. To wątek jest odpowiedzialny za sprawdzanie czy przypadkiem nie został przerwany. Może to zrobić na dwa sposoby:
   - sprawdzić flagę isInterrupted()
   - wykonać metodę sleep(), która na początku sprawdza flagę interrupted() i jeżeli jest ona ustawiona to rzuci InterruptedException. 

Zadanie: Interruption
Należy jednak pamiętać, że wywołanie metody Thread.interrupted() ustawia flagę interrupted spowrotem na false. Więc nie obsłużenie wyjątku, skasuje flagę i nasze dodatkowe obsłużenie nie wykryje przerwania.  

Co się dzieje z wyjątkami w wątku?
Co to jest wątek demonowy?
- Wątek demonowy to taki który nie utrzymuje działania aplikacji 'na sobie'. Jeżeli w aplikacji zostaną tylko wątki demonowe, proces JVM kończy się. 

Zadanie: Word count na jeden wątek

2. Locks
Narzędzie służące do synchronizacji będące poziom wyżej niż synchronized. 
Locki pozwalają np na:
   - synchronizację między metodami
   - sprawiedliwe przydzielanie zasobów dla wątków - tzn. najdłużej czekający wątek dostanie lock'a
   - wywołanie metody tryLock() która zwróci true, jeżeli uda się dostać locka lub false, jeżeli nie będzie to możliwe.
   - wywołanie metody lockInterruptibly(), wtedy wątek czekający na locka może zostać przerwany.
   - bardziej czytelne API lock(), unlock()
Najprostszą implementacją jest reentrant lock. 
Reentrant lock pozwala na wielokrotne pobieranie lock'a. Należy jednak pamiętać, by ilość 'pobranych' locków była równa ilości 'oddanych'.

ReadWriteLock 
Pozwala na oddzielenia blokowania operacji zapisu i odczytu. Jest to bardzo przydatne, bo najczęściej mamy do czynienia z sytuacją kiedy pojedyńcze wątki zapisują a wiele wątków odczytuje daną wartość.

StampedLock
Dodaje możliwość cytania optimistycznego. Czyli czytamy i jeżeli nasz stamp nie przechodzi walidacji to mamy pecha musimy przeczytać pobierając normalny read lock. Jeżeli jednak mamy szczęście to stamp przechodzi walidację. Przyśpiesza to operacje odczytu. 

Zadanie:
Dodać własny adapter na HashMapę który zapewni możliwość podniesienia licznika w sposób bezpieczny wątkowo. 
Przy okazji popatrzymy sobie na implementację ConcurrentHashMapy. 
- Czemu tam jest synchronized a nie lock?



3. Atomic
Czym są operacje atomowe?
Operacje atomowe czyli nierozdzielne, z gwarancją procesora że zostaną wykonane w całości albo wcale. 

Co dostarcza pakiet java.util.concurrent.atomic
Szybsza implementacja dla operacji takich jak inkrementowanie i dekrementowanie intów czy longów. Istnieje również AtomicReference do wykonywania operacji CAS na obiektach.
CAS Compare and Swap, atomowość zagwarantowana przez procesor.
Bierzemy wartość z pamięci, zmieniamy ją, jeżeli nowa wartość jest inna niż obecna to podmieniamy wartości i zwracamy true - podmiana się udała. Jeżeli jest taka sama to 
dostajemy false - podmiana się nie udała. Dzięki temu jesteśmy w stanie w pętli inkrementować daną wratość aż się uda. Jest to bardziej wydajne niż metoda synchronized. 

Zadanie:
- popraw kod tak by licznik wskazywał poprawną wartość, użyj klasy Atomic
- użyj metody compare and set
- zrób tak by poczekać na wynik wykonania pracy wszystkich wątków
<!-- - czy istnieje inna metoda synchronizacji którą możemy wykorzystać? -->
- zmierz i porównaj czasy wykonania przy różnych metodach synchronizacji.
- Bonus: jak zachowuje się dla 1000 powtórzeń na JDK 1.8 i 16? 


4. Future
Future jest klasą w której wynik działania wątku pojawi się gdy wątek zakończy działanie. Można ją wykorzystać by czekać na wynik wątku - blokująca operacja .get() 
Rozwinięciem klasy future, jest CompletableFuture. Klasa ta pozwala na łączenie asynchronicznych wywołań w łańcuch. Tzn. możemy w jednym wątku pobrać coś z interentu, w następnym przetworzyć to, następnie np zapisać do pliku i wszystkie te operacje wykonują się jedna po drugiej w osobnych wątkach.
Można również łączyć wywołania różnych zadań w jedno, lub zbierać rezultaty w jeden wynik.  


5. Exekutory
Dość popularny sposób używania wątków w Javie, daje prosty interfejs do rozdzielenia zadania na wiele wątków. Jest też frameworkiem który możemy rozszerzać. 

przykładowe implementacje:
- Executors.newSingleThreadExecutor();
   działa na pojedynczym wątku. To camo co Executors.newFixedThreadPool(1)
- Executors.newFixedThreadPool(10);
   
- Executors.newWorkStealingPool();
- Executors.newCachedThreadPool();

6. ForkJoin
Framework który pozwala na szybkie wykonywanie zadań które daje się podzielić na mniejsze. Polega on na algorytmie składającym się z dwóch faz:
- Fork - dzielimy zadanie i wykonujemy je równolegle
- Join - zbieramy rezultaty

Zadanie domowe:





