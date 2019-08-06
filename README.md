# Alphametics

Alphametics or [verbal arithmetic](https://en.wikipedia.org/wiki/Verbal_arithmetic) or
 [Kryptogramm](https://de.wikipedia.org/wiki/Kryptogramm) is a mathematical game.
 
This Scala code solves arbitrary alphametics games.

## Usage
The API is in German.

* `Zahl` = Number
* `plus`, `minus`
* `aufgabe` = task/puzzle
* `loesung` = solution

The idea is to transform the puzzle's equation to the form of `row1 + row2 + ... - resultrow = 0` and then
 solve that equation.
 You create the equation via `val aufgabe = Zahl(row1).plus(Zahl(row2).plus(???).minus(Zahl(resultrow))`
 and then solve it with `aufgabe.loesung`.  

Example:

```scala
import alphametics.Alphametics.{Aufgabe, Ziffer, Zahl}
val aufgabe: Aufgabe = Zahl("VATER")
  .plus(Zahl("MUTTER"))
  .minus(Zahl("ELTERN"))
val loesung: Option[Map[Ziffer, Int]] = aufgabe.loesung
assert(loesung.contains(Map('A' -> 9, 'E' -> 2, 'L' -> 3, 'M' -> 1, 'N' -> 0, 'R' -> 5, 'T' -> 6, 'U' -> 8, 'V' -> 4)))
```

Have fun!
