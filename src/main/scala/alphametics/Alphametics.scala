package alphametics

object Alphametics {

  type Ziffer = Char
  type ZifferMapping = Ziffer => Int

  trait Berechenbar {
    def wert(zifferMapping: ZifferMapping): Int
  }

  trait Addierbar[T] {
    def plus(that: T): T

    def minus(that: T): T
  }

  case class Stellenwert(stelle: Int, z: Ziffer) extends Berechenbar {
    assert(stelle >= 0)

    def wert(zifferMapping: ZifferMapping): Int = math.pow(10, stelle).toInt * zifferMapping(z)
  }

  trait MitZiffern {
    def ziffern: Set[Ziffer]
  }

  trait Aufgabe extends MitZiffern with Berechenbar with Addierbar[Aufgabe] {
    self =>

    private def combineZiffern(mz: MitZiffern): Set[Ziffer] = ziffern ++ mz.ziffern

    private def max10Ziffern(mz: MitZiffern): Boolean = combineZiffern(mz).size <= 10

    override def plus(that: Aufgabe): Aufgabe = {
      assert(max10Ziffern(that))
      new Aufgabe {
        override def wert(zifferMapping: ZifferMapping): Int = self.wert(zifferMapping) + that.wert(zifferMapping)

        override def ziffern: Set[Ziffer] = self.ziffern ++ that.ziffern
      }
    }

    override def minus(that: Aufgabe): Aufgabe = {
      assert(max10Ziffern(that))
      new Aufgabe {
        override def wert(zifferMapping: ZifferMapping): Int = self.wert(zifferMapping) - that.wert(zifferMapping)

        override def ziffern: Set[Ziffer] = self.ziffern ++ that.ziffern
      }
    }

    def loesung: Option[Map[Ziffer, Int]] = {
      val ziffernZeichenSeq: Seq[Ziffer] = ziffern.toSeq
      // wenn es weniger als 10 Ziffernzeichen gibt, so müssen alle verschiedenen Untermengen der 10 Ziffern
      // 0 bis 9 verwendet werden.
      val ziffernMengen: Iterator[Set[Int]] = (0 to 9).toSet.subsets(ziffernZeichenSeq.size)
      val maps: Iterator[Map[Ziffer, Int]] = for {
        ziffernMenge: Set[Int] <- ziffernMengen
        // für eine Untermenge an Ziffern, interpretiere sie als Sequence, finde alle Permutationen
        // von Ziffern in der Reihenfolge der Sequenz, um sie mit der Sequenz der Ziffernzeichen zu kombinieren
        ziffernSeq: Iterator[Seq[Int]] = ziffernMenge.toSeq.permutations
        ziffern: Seq[Int] <- ziffernSeq
        map: Map[Ziffer, Int] = ziffernZeichenSeq.zip(ziffern).toMap
        //u = println(map)
      } yield map
      //println("size " + maps.size)
      //println(maps.next())
      //println(maps.next())
      //println(maps.find(map => map('V') == 5))
      maps.find(map => wert(mapping(map)) == 0)
    }

    def mapping(map: Map[Ziffer, Int]): Ziffer => Int = (ziffer: Ziffer) => map(ziffer)

  }

  case class Zahl(z: String) extends Aufgabe {
    assert(z.nonEmpty)
    assert(ziffern.size <= 10)

    lazy val stellenwerte: Seq[Stellenwert] = {
      val l = z.length
      for (i <- 0 until l) yield Stellenwert(l - 1 - i, z.charAt(i))
    }

    override def ziffern: Set[Ziffer] = z.toSet

    override def wert(zifferMapping: ZifferMapping): Int =
      stellenwerte.foldLeft(0)((w, s) => w + s.wert(zifferMapping))


  }
}
