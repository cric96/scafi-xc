package it.unibo.scafi.xc.engine.context.exchange.libraries

import it.unibo.scafi.xc.UnitTest
import it.unibo.scafi.xc.collections.ValueTree
import it.unibo.scafi.xc.engine.context.ProbingContextMixin
import it.unibo.scafi.xc.engine.context.common.InvocationCoordinate
import it.unibo.scafi.xc.engine.context.exchange.BasicExchangeCalculusContext
import it.unibo.scafi.xc.engine.network.Export
import it.unibo.scafi.xc.language.libraries.All.{ *, given }

trait MathTests:
  this: UnitTest & ProbingContextMixin & BasicFactoryMixin =>

  private val epsilon: Double = 0.0001

  def averageSemantics(): Unit =
    var averageResult: Double = 0
    def averagingProgram(using BasicExchangeCalculusContext[Int]): Unit =
      averageResult = average(weight = self / 10, value = self)

    val exportProbe: Export[Int, ValueTree[InvocationCoordinate, Any]] = probe(
      localId = 42,
      factory = factory,
      program = averagingProgram,
    )

    it should "return the local value if device is alone" in:
      averageResult shouldBe 42.0 +- epsilon

    it should "return the weighted average with neighbours" in:
      probe(
        localId = 42,
        factory = factory,
        program = averagingProgram,
        inboundMessages = Map(
          42 -> exportProbe(42),
          2 -> probe(
            localId = 2,
            factory = factory,
            program = averagingProgram,
            inboundMessages = Map(
              42 -> exportProbe(2),
            ),
          )(42),
          12 -> probe(
            localId = 12,
            factory = factory,
            program = averagingProgram,
          )(42),
        ),
      )

      averageResult shouldBe (42.0 * 4.0 + 12.0) / 5.0 +- epsilon

  end averageSemantics

  def mathLibrarySemantics(): Unit =
    "average" should behave like averageSemantics()
end MathTests
