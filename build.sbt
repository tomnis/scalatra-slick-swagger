organization := "org.tomnis.scalatraslickswagger"
name := "scalatra + slick + swagger"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.12.10"

val ScalatraVersion = "2.7.+"
resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "org.scalatra"            %% "scalatra"          % ScalatraVersion,
  "org.scalatra"            %% "scalatra-scalate"  % ScalatraVersion,
  "org.scalatra"            %% "scalatra-specs2"   % ScalatraVersion    % "test",
  "org.scalatest"           %% "scalatest"         % "3.1.1"            % "test",
  "org.scalatra"            %% "scalatra-json"     % ScalatraVersion,
  "org.scalatra"            %% "scalatra-swagger"  % ScalatraVersion,
  "org.json4s"              %% "json4s-native"     % "3.7.0-M2",
  "com.typesafe.slick"      %% "slick"             % "3.3.2",
  "com.workday.warp"        %%  "warp-core"        % "3.4.0-rc.1",
  "com.h2database"          %  "h2"                % "1.4.199",
  "com.mchange"             %  "c3p0"              % "0.9.5.4",
  "ch.qos.logback"          %  "logback-classic"   % "1.2.3"             % "provided",
  "org.eclipse.jetty"       %  "jetty-webapp"      % "9.4.19.v20190610"  % "provided",
  "javax.servlet"           %  "javax.servlet-api" % "3.1.0"             % "provided"
)

enablePlugins(ScalatraPlugin)
