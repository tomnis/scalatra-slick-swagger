package org.tomnis.scalatraslickswagger

import slick.jdbc.H2Profile.api._

/**
 * Created by tomas.mccandless on 4/14/20.
 */
trait TablesLike {
  // Definition of the SUPPLIERS table
  class Suppliers(tag: Tag) extends Table[(Int, String, String, String, String, String)](tag, "SUPPLIERS") {
    def id = column[Int]("SUP_ID", O.PrimaryKey) // This is the primary key column
    def name = column[String]("SUP_NAME")
    def street = column[String]("STREET")
    def city = column[String]("CITY")
    def state = column[String]("STATE")
    def zip = column[String]("ZIP")

    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name, street, city, state, zip)
  }

  class SupplierWrapper(val id: Int, val name: String, val street: String, val city: String, val state: String, val zip: String)
//  case class Supplier(override val id: Int, override val name: String, override val street: String, override val city: String, override val state: String, override val zip: String) extends SupplierWrapper(id, name, street, city, state, zip)
//  case class Supplier(id: Int, name: String, street: String, city: String, state: String,zip: String)

  // Definition of the COFFEES table
  class Coffees(tag: Tag) extends Table[(String, Int, Double, Int, Int)](tag, "COFFEES") {
    def name = column[String]("COF_NAME", O.PrimaryKey)
    def supID = column[Int]("SUP_ID")
    def price = column[Double]("PRICE")
    def sales = column[Int]("SALES")
    def total = column[Int]("TOTAL")
    def * = (name, supID, price, sales, total)

    // A reified foreign key relation that can be navigated to create a join
    def supplier = foreignKey("SUP_FK", supID, suppliers)(_.id)
  }

  class CoffeeWrapper(val name: String, val supId: Int, val price: Double, val sales: Int, val total: Int)
//  case class Coffee(override val name: String, override val supId: Int, override val price: Double, override val sales: Int, override val total: Int) extends CoffeeWrapper(name, supId, price, sales, total)
//  case class Coffee(name: String, supId: Int, price: Double, sales: Int, total: Int)

  // Table query for the SUPPLIERS table, represents all tuples
  val suppliers = TableQuery[Suppliers]

  // Table query for the COFFEES table
  val coffees = TableQuery[Coffees]

  // Query, implicit inner join coffes and suppliers, return their names
  val findCoffeesWithSuppliers = {
    for {
      c <- coffees
      s <- c.supplier
    } yield (c.name, s.name)
  }

  // DBIO Action which runs several queries inserting sample data
  val insertSupplierAndCoffeeData = DBIO.seq(
    Tables.suppliers += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
    Tables.suppliers += (49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
    Tables.suppliers += (150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966"),
    Tables.coffees ++= Seq(
      ("Colombian", 101, 7.99, 0, 0),
      ("French_Roast", 49, 8.99, 0, 0),
      ("Espresso", 150, 9.99, 0, 0),
      ("Colombian_Decaf", 101, 8.99, 0, 0),
      ("French_Roast_Decaf", 49, 9.99, 0, 0)
    )
  )

  // DBIO Action which creates the schema
  val createSchemaAction = (suppliers.schema ++ coffees.schema).create

  // DBIO Action which drops the schema
  val dropSchemaAction = (suppliers.schema ++ coffees.schema).drop

  // Create database, composing create schema and insert sample data actions
  val createDatabase = DBIO.seq(createSchemaAction, insertSupplierAndCoffeeData)
}


object Tables extends TablesLike
