package it.unibo.scafi.xc.simulator.deterministic

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.language.semantics.exchange.ExchangeCalculusSemantics
import it.unibo.scafi.xc.simulator.DiscreteSimulator
import org.scalatest.BeforeAndAfterEach
import it.unibo.scafi.xc.language.libraries.All.{ _, given }

class DeterministicSimulatorTests extends UnitTest:
  var deviceCountParameter: Int = 200
  var results: Map[Int, Double] = Map.empty
  var wakeUpCounts: Map[Int, Int] = Map.empty

  def aggregateProgram(using BasicExchangeCalculusContext[Int]): Unit =
    wakeUpCounts += self -> rep(0)(_ + 1)
    results += self -> distanceTo(self < 5, 1.0)

  def newSimulator: DeterministicSimulator[Int, BasicExchangeCalculusContext[Int]] = DeterministicSimulator(
    contextFactory = n => BasicExchangeCalculusContext[Int](n.localId, n.receive()),
    program = aggregateProgram,
    devices = (0 until deviceCountParameter).map(id => Device.WithFixedSleepTime(id, id % 2 + 2)).toList,
    deviceNeighbourhood = (0 until deviceCountParameter)
      .map(i =>
        i -> // every device is neighbour with the 5 devices before and after it
          (Math.max(0, i - 5) to Math.min(deviceCountParameter - 1, i + 5)).toSet,
      )
      .toMap,
    deliveredMessageLifetime = 10,
  )

  var sut: DiscreteSimulator[Int, BasicExchangeCalculusContext[Int]] = newSimulator

  val deviceCountParameters: TableFor1[Int] = TableFor1("device count", 5 to 300 by 65*)

  "A deterministic simulator" should "run the program on all devices" in:
    for _ <- 0 until 10 do sut.tick()
    wakeUpCounts.values.max shouldBe 3
    wakeUpCounts.values.min shouldBe 2
    results.size shouldBe 200

  it should "make the gradient converge in a fixed amount of ticks" in:
    forAll(deviceCountParameters): (n: Int) =>
      results = Map.empty
      wakeUpCounts = Map.empty
      deviceCountParameter = n
      sut = newSimulator
      for _ <- 0 until deviceCountParameter do sut.tick()
      results.values.max shouldBe ((deviceCountParameter - 1) / 5).toDouble +- 0.0001
end DeterministicSimulatorTests
