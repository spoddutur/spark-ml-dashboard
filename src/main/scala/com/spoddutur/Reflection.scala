package com.spoddutur

/**
  * Created by surthi on 17/08/17.
  */
import scala.reflect.ManifestFactory
import scala.reflect.runtime.{universe => ru}

object Reflection {

  val mirror = ru.runtimeMirror(this.getClass.getClassLoader)

  val ObjectClass = classOf[java.lang.Object];

  def findVals(x: Class[_]): Iterable[String] = findVals(x, List.empty);

  def findVals(clz: Class[_], accum: Iterable[String]): Iterable[String] = {

    clz match {
      case ObjectClass => accum;
      case _ => {


        val theType = mirror.classSymbol(clz).toType
        // val newVals = theType.members.collect({ case x if x.isTerm => x.asTerm }).filter(_.isVal).map(_.name.toString)
        val newVals = theType.members
          .collect({ case x if x.isTerm => x.asTerm })
          .filter(x => {
            if(x.isMethod) {
              val m = x.asMethod
              val numArgs = if (m.paramLists.length > 0) m.paramLists(0).length else -1
              val isSetter = m.isPublic && m.name.toString.matches("^set[A-Z].*") && numArgs == 1
              isSetter
            } else {
              false
            }
          })
          .map(_.name.toString)
        findVals(clz.getSuperclass, accum ++ newVals)
      }
    }
  }
}