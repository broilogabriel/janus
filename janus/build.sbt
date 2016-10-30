import Settings._
import sbt._

name := "janus"

version := "17.5.0"


lazy val root = project.root
  .setName("janus")
  .setDescription("ES Actors")
  .setInitialCommand("_")
  .configureRoot
  .aggregate(core, app)


lazy val core = project.from("core")
  .setName("core")
  .setDescription("core project")
  .setInitialCommand("_")
  .configureModule

lazy val app = project.from("app")
  .setName("app")
  .setDescription("app project")
  .setInitialCommand("_")
  .configureModule
  .configureIntegrationTests
  .configureFunctionalTests
  .configureUnitTests
  .dependsOnProjects(core)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    mainClass in(Compile, run) := Some(Main.getClass.toString),
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := Settings.defaultOrg
  )

enablePlugins(JavaAppPackaging)

packageName in Docker := packageName.value

version in Docker := version.value

maintainer in Docker := "Gabriel Broilo <broilo.gabriel@gmail.com>"

dockerBaseImage := "openjdk:latest"

dockerRepository := Some("broilogabriel")

dockerExposedPorts := Seq(9000, 9443)
