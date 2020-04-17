package org.tomnis.scalatraslickswagger

import com.workday.warp.persistence.Tables.TestDefinitionRow
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.NativeJsonSupport
import org.scalatra.swagger.{Swagger, SwaggerSupport}
import org.scalatra.{FutureSupport, ScalatraBase, ScalatraServlet}
import slick.jdbc.H2Profile.api._
import Tables.{CoffeeWrapper, SupplierWrapper}

import scala.concurrent._


trait SlickRoutes extends ScalatraBase with FutureSupport with NativeJsonSupport with SwaggerSupport {


  implicit val jsonFormats: Formats = DefaultFormats
  protected val applicationDescription = "The coffeeshop API. It exposes operations for browsing and searching lists of coffees, and retrieving single coffees."


  def swagger: Swagger

  def db: Database


  val getCreateDb = apiOperation[Unit]("getCreateDb").summary("creates the db")
  get("/db/create-db", operation(getCreateDb)) {
    db.run(Tables.createDatabase)
  }

  val getDropDb = apiOperation[Unit]("getDropDb").summary("drops the db")
  get("/db/drop-db", operation(getDropDb)) {
    db.run(Tables.dropSchemaAction)
  }


  val getCoffees = apiOperation[String]("getCoffees").summary("gets all coffees")
  get("/coffees", operation(getCoffees)) {
    contentType = "text/plain"
    db.run(Tables.findCoffeesWithSuppliers.result) map { xs =>
      println(xs)
      xs map { case (s1, s2) => f"  $s1 supplied by $s2" } mkString "\n"
    }
  }


  val postCoffees = apiOperation[String]("postCoffees")
    .summary("adds a new coffee/supplier")
    .parameter(
      bodyParam[CoffeePayload]("coffee_data").description("a coffee and associated supplier")
    )
  post("/coffees", operation(postCoffees)) {
    s"we got payload: ${request.body}"
  }


  // inheritance and nesting -- coffee shows up as "object" type (missing properties)
  // nesting, no inheritance -- show up correctly with fields,
  // nesting, use wrapper in swagger -- show up correctly

  // no nesting, inheritance -- empty properties
}




case class Supplier(override val id: Int, override val name: String, override val street: String, override val city: String, override val state: String, override val zip: String) extends SupplierWrapper(id, name, street, city, state, zip)
case class Coffee(override val name: String, override val supId: Int, override val price: Double, override val sales: Int, override val total: Int) extends CoffeeWrapper(name, supId, price, sales, total)

case class CoffeePayload(coffee: Coffee, supplier: Supplier, testDef: TestDefinitionRow)

class SlickApp(val db: Database)(implicit val swagger: Swagger) extends ScalatraServlet with SlickRoutes {

  protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
}
