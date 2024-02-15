package it.unibo.scafi.xc.collections

import scala.annotation.targetName
import scala.annotation.unchecked.uncheckedVariance
import scala.collection.{ Iterable, MapView }

class MapWithDefault[K, +V](underlying: Map[K, V], val default: V) extends Iterable[(K, V)] with Function[K, V]:

  private val inner: Map[K, V] = underlying.withDefaultValue(default)

  override def hashCode(): Int = inner.hashCode() * 31 + default.hashCode()

  override def apply(v1: K): V = inner(v1)

  override def iterator: Iterator[(K, V)] = inner.iterator

  override def toString(): String = mkString(s"$className<default=$default>(", ", ", ")")

  @targetName("addElements")
  inline def ++[V1 >: V](xs: IterableOnce[(K, V1)]): MapWithDefault[K, V1] = new MapWithDefault(inner ++ xs, default)

  @targetName("addElement")
  inline def +[V1 >: V](kv: (K, V1)): MapWithDefault[K, V1] = new MapWithDefault(inner + kv, default)

  @targetName("removeElement")
  inline def -(key: K): MapWithDefault[K, V] = new MapWithDefault(inner - key, default)

  @targetName("removeElements")
  inline def --(keys: IterableOnce[K]): MapWithDefault[K, V] = new MapWithDefault(inner -- keys, default)

  def concat[V1 >: V](other: IterableOnce[(K, V1)]): MapWithDefault[K, V1] =
    new MapWithDefault(inner.concat(other), default)

  override def empty: MapWithDefault[K, V] = new MapWithDefault(Map.empty, default)

  override def filter(p: ((K, V)) => Boolean): MapWithDefault[K, V] =
    new MapWithDefault(inner.filter(p), default)

  override def filterNot(p: ((K, V)) => Boolean): MapWithDefault[K, V] =
    new MapWithDefault(inner.filterNot(p), default)

  def flatMap[K2 <: K](f: ((K, V)) => IterableOnce[(K2, V @uncheckedVariance)]): MapWithDefault[K2, V] =
    new MapWithDefault(inner.flatMap(f), default)

  def get(key: K): V = inner(key)

  override def knownSize: Int = inner.knownSize

  def map[V2](f: V => V2): MapWithDefault[K, V2] = new MapWithDefault(inner.map((k, v) => (k, f(v))), f(default))

  override def partition(p: ((K, V)) => Boolean): (MapWithDefault[K, V], MapWithDefault[K, V]) =
    val (l, r) = inner.partition(p)
    (new MapWithDefault(l, default), new MapWithDefault(r, default))

  def removed(key: K): MapWithDefault[K, V] = new MapWithDefault(inner.removed(key), default)

  def removedAll(keys: IterableOnce[K]): MapWithDefault[K, V] = new MapWithDefault(inner.removedAll(keys), default)

  override def size: Int = inner.size

  override def tapEach[U](f: ((K, V)) => U): MapWithDefault[K, V] = new MapWithDefault(inner.tapEach(f), default)

  def updated[V1 >: V](key: K, value: V1): MapWithDefault[K, V1] =
    new MapWithDefault(inner.updated(key, value), default)

  def updatedWith[V1 >: V](key: K)(f: V1 => Option[V1]): MapWithDefault[K, V1] =
    new MapWithDefault(inner.updatedWith(key)(v => f(v.getOrElse(default))), default)

  def updated[V1 >: V](key: K, f: V1 => V1): MapWithDefault[K, V1] = updatedWith(key)(v => Some(f(v)))

  def withDefault[V1 >: V](d: V1): MapWithDefault[K, V1] = new MapWithDefault(inner, d)

  override def view: MapView[K, V] = inner.view

  export inner.{
    contains,
    flatMap,
    foreachEntry,
    keys,
    keySet,
    keysIterator,
    keyStepper,
    values,
    valuesIterator,
    valueStepper,
  }
end MapWithDefault

object MapWithDefault:
  def empty[K, V](default: V): MapWithDefault[K, V] = MapWithDefault(Map.empty, default)
