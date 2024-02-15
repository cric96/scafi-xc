package it.unibo.scafi.xc

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers as ShouldMatchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{ Inside, Inspectors, OptionValues }

trait UnitTest
    extends AnyFlatSpec
    with ShouldMatchers
    with OptionValues
    with Inside
    with Inspectors
    with TableDrivenPropertyChecks:

  export scala.language.postfixOps
  export org.scalatest.matchers.should.Matchers._
  export math.Numeric.Implicits.infixNumericOps
  export org.scalatest.prop.{ TableFor1, TableFor2, TableFor3 }
