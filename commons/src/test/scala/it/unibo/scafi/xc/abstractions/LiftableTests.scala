package it.unibo.scafi.xc.abstractions

import it.unibo.scafi.xc.UnitTest

trait LiftableTests extends MappableTests:
  this: UnitTest =>

  def liftable[F[X] <: Iterable[X]: Liftable](sut: F[Int], sut2: F[F[Int]]): Unit =
    val lift = summon[Liftable[F]]
    it should "allow lifting with 1-ary function" in:
      lift.lift(sut)(_ + 1.0) should contain theSameElementsAs sut.map(_ + 1.0)
    it should "provide a static shortcut to 1-ary lifting" in:
      Liftable.lift(sut)(_ * 2) should contain theSameElementsAs lift.lift(sut)(_ * 2)
    it should "allow lifting with 2-ary function" in:
      lift.lift(sut, sut)(_ * _ + 1.0) should contain theSameElementsAs sut.map(x => x * x + 1.0)
    it should "provide a static shortcut to 2-ary lifting" in:
      Liftable.lift(sut, sut)(_ + _) should contain theSameElementsAs lift.lift(sut, sut)(_ + _)
    it should "allow lifting with 3-ary function" in:
      lift.lift(sut, sut, sut)((a, b, c) => (a + b + c).toString) should contain theSameElementsAs sut.map(x =>
        (x * 3).toString,
      )
    it should "provide a static shortcut to 3-ary lifting" in:
      Liftable.lift(sut, sut, sut)(_ + _ + _) should contain theSameElementsAs lift.lift(sut, sut, sut)(_ + _ + _)
    it should "provide a static shortcut to lift twice a 1-ary function" in:
      Liftable.liftTwice(sut2)(_ + 1).flatten should contain theSameElementsAs sut2.flatten.map(_ + 1)
    it should "provide a static shortcut to lift twice a 2-ary function" in:
      Liftable.liftTwice(sut2, sut2)(_ * _).flatten should contain theSameElementsAs sut2.flatten.map(x => x * x)
    it should "provide a static shortcut to lift twice a 3-ary function" in:
      Liftable.liftTwice(sut2, sut2, sut2)(_ + _ + _).flatten should contain theSameElementsAs sut2.flatten.map(x =>
        x * 3,
      )
    it should "allow lifting a 1-ary function" in:
      val f1: F[Int] => F[Int] = Liftable.lift(_ + 1)
      f1(sut) should contain theSameElementsAs sut.map(_ + 1)
    it should "allow lifting a 2-ary function" in:
      val f2: (F[Int], F[Int]) => F[Int] = Liftable.lift(_ * _)
      f2(sut, sut) should contain theSameElementsAs sut.map(x => x * x)
    it should "allow lifting a 3-ary function" in:
      val f3: (F[Int], F[Int], F[Int]) => F[String] = Liftable.lift((a, b, c) => (a + b + c).toString)
      f3(sut, sut, sut) should contain theSameElementsAs sut.map(x => (x * 3).toString)

    it should behave like mappable(sut)
  end liftable
end LiftableTests
