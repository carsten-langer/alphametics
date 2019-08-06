package alphametics

import alphametics.Alphametics.{Stellenwert, Zahl, Ziffer}
import org.scalatest.{FlatSpec, Matchers}

class AlphameticsTest extends FlatSpec with Matchers {

  behavior of "Stellenwert"
  it should "ask the right Ziffer from the ZifferMapping" in {
    val stellenwert = Stellenwert(2, 'F')
    stellenwert.wert(z => {
      z.shouldEqual('F')
      0
    })
  }

  it should "calculate the wert correctly" in {
    val stellenwert = Stellenwert(2, 'F')
    stellenwert.wert(_ => 7).shouldEqual(700)
  }

  behavior of "Zahl"

  it should "throw if > 10 digits are used" in {
    assertThrows[AssertionError](Zahl("ABCDEFGHIJK"))
  }

  it should "return correct ziffern" in {
    val zahl = Zahl("ABADACJIHGFE")
    zahl.ziffern.shouldEqual(('A' to 'J').toSet)
  }

  it should "calculate the wert correctly" in {
    val zahl = Zahl("TEST")
    val map = (z: Ziffer) => z match {
      case 'T' => 1
      case 'E' => 3
      case 'S' => 5
      case _ => fail()
    }
    zahl.wert(map).shouldEqual(1351)
  }

  it should "throw if this.plus(that) has > 10 digits" in {
    val z1 = Zahl("ABCDEFGHIJ")
    val z2 = Zahl("JK")
    assertThrows[AssertionError](z1.plus(z2))
  }

  it should "build a new Berechenbar with this.plus(that)" in {
    val z1 = Zahl("AAA")
    val z2 = Zahl("BBB")
    val z = z1.plus(z2)
    val map = (z: Ziffer) => z match {
      case 'A' => 6
      case 'B' => 5
      case _ => fail()
    }
    z.wert(map).shouldEqual(1221)
  }

  it should "throw if this.minus(that) has > 10 digits" in {
    val z1 = Zahl("ABCDEFGHIJ")
    val z2 = Zahl("JK")
    assertThrows[AssertionError](z1.minus(z2))
  }

  it should "build a new Berechenbar with this.minus(that)" in {
    val z1 = Zahl("AAA")
    val z2 = Zahl("BBB")
    val z = z1.minus(z2)
    val map = (z: Ziffer) => z match {
      case 'A' => 6
      case 'B' => 5
      case _ => fail()
    }
    z.wert(map).shouldEqual(111)
  }

  behavior of "Alphametics"

  it should "find a solution" in {
    val aufgabe = Zahl("VATER").plus(Zahl("MUTTER")).minus(Zahl("ELTERN"))
    aufgabe.loesung.shouldEqual(
      Some(Map('A' -> 9, 'E' -> 2, 'L' -> 3, 'M' -> 1, 'N' -> 0, 'R' -> 5, 'T' -> 6, 'U' -> 8, 'V' -> 4)))
  }

  it should "find a solution for another example" in {
    val aufgabe = Zahl("NULL")
      .plus(Zahl("FÜNF")) // X = Ü, as Ü is not a single-byte-character
      .plus(Zahl("FÜNF"))
      .plus(Zahl("ZWANZIG"))
      .plus(Zahl("ZWANZIG"))
      .minus(Zahl("FÜNFZIG"))
    aufgabe.loesung.shouldEqual(
      Some(Map('A' -> 0, 'F' -> 5, 'G' -> 3, 'I' -> 9, 'L' -> 7, 'N' -> 1, 'U' -> 4, 'W' -> 8, 'Z' -> 2, 'Ü' -> 6)))
  }

}
