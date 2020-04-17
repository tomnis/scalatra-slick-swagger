package org.tomnis.scalatraslickswagger

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, ContactInfo, LicenseInfo, NativeSwaggerBase, Swagger}

/**
 * Created by tomas.mccandless on 4/14/20.
 */
class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase

object CoffeesApiInfo extends ApiInfo(
  "The Coffeeshop API",
  "Docs for the Coffees API",
  "http://scalatra.org",
  ContactInfo(
    "Scalatra API Team",
    "http://scalatra.org",
    "apiteam@scalatra.org"
  ),
  LicenseInfo(
    "MIT",
    "http://opensource.org/licenses/MIT"
  )
)

class CoffeesSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", CoffeesApiInfo)

