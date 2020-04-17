package org.tomnis.scalatraslickswagger

//import Tables.Coffee
import org.scalatest.funspec.AnyFunSpec
import org.scalatra.swagger.{Model, Swagger}
import com.workday.warp.persistence.{Tables => WarpTables}
import org.scalatest.matchers.should.Matchers
//import org.scalatra.slickexample.Model.TDR

/**
 * Created by tomas.mccandless on 4/14/20.
 */
class CoffeesSwaggerSpec extends AnyFunSpec with Matchers {

  describe("coffees swagger") {
    it("should reflect") {
      val a: Set[Model] = Swagger.collectModels[Coffee](Set.empty)
      println(a)

      val b = Swagger.collectModels[Cat](Set.empty)
      println(b)


      val c = Swagger.collectModels[TDR](Set.empty)
      println(c)
    }

    it("should reflect a top-level class with no inheritance") {
      val catModels: Set[Model] = Swagger.collectModels[Cat](Set.empty)
      catModels should have size 1
      val catModel = catModels.head
      catModel.properties.filter(_._1 == "name") should have size 1
      catModel.properties.filter(_._1 == "age") should have size 1
      catModel.properties should have size 2
    }

    it("should reflect a nested class with no inheritance") {
      val catModels: Set[Model] = Swagger.collectModels[Model.NestedCat](Set.empty)
      catModels should have size 1
      val catModel = catModels.head
      catModel.properties.filter(_._1 == "name") should have size 1
      catModel.properties.filter(_._1 == "age") should have size 1
      catModel.properties should have size 2
    }

    // fails with
    // java.lang.ArrayIndexOutOfBoundsException: 8
    //   at org.scalatra.swagger.reflect.Reflector$.$anonfun$createDescriptor$13(Reflector.scala:152)
    it("should reflect a nested class with inheritance") {
      Swagger.collectModels[com.workday.warp.persistence.Tables.TestDefinitionRow](Set.empty)
    }
  }
}


case class Cat(name: String, age: Int)

case class TDR(override val idTestDefinition: Int, override val methodSignature: String, override val active: Boolean, override val productName: String, override val subProductName: String, override val className: String, override val methodName: String, override val documentation: Option[String] = None) extends WarpTables.TestDefinitionRowWrapper(idTestDefinition, methodSignature, active, productName, subProductName, className, methodName, documentation)
object Model {
  case class NestedCat(name: String, age: Int)

}

