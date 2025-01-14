package it.unibo.scafi.xc.engine.context.exchange

import it.unibo.scafi.xc.collections.ValueTree
import it.unibo.scafi.xc.engine.context.common.*
import it.unibo.scafi.xc.engine.network.Import

/**
 * Implements a basic version of an exchange calculus context that wraps any value into [[Any]].
 * @param self
 *   the device id of the current device
 * @param inboundMessages
 *   inbound messages as [[Import]]
 * @tparam Id
 *   the type of the device id
 * @see
 *   [[AbstractExchangeCalculusContext]]
 */
class BasicExchangeCalculusContext[Id](
    self: Id,
    inboundMessages: Import[Id, ValueTree[InvocationCoordinate, Any]],
) extends AbstractExchangeCalculusContext[Id, Any](self, inboundMessages)
    with MessageSemantics.Basic
