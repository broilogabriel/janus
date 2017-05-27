import Settings._
import sbt.Keys.version
import sbt._

def projectDescription(moduleName: String) = s"Janus Project: Module - $moduleName"

lazy val root = project.root
  //  .setInitialCommand("_")
  .configureRoot
  .dependsOn(core, app)
  .aggregate(core, app)

lazy val core = project.from("core")
  .setName("core")
  .setDescription(projectDescription("core"))
//  .setInitialCommand("_")
  .configureModule

lazy val app = project.from("app")
  .setName("app")
  .setDescription(projectDescription("app"))
  .setInitialCommand("_")
  .configureModule
  .configureIntegrationTests
  .configureFunctionalTests
  .configureUnitTests
  .dependsOnProjects(core)
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(
    mainClass in(Compile, run) := Some("com.broilogabriel.Main"),
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := Settings.defaultOrg,
    packageName in Docker := packageName.value,
    version in Docker := version.value,
    maintainer in Docker := "Gabriel Broilo <broilo.gabriel@gmail.com>",
    dockerRepository := Some("broilogabriel"),
    dockerBaseImage := "openjdk:latest",
    dockerExposedPorts := Seq(9000, 9443)
  )


//packageName in Docker := packageName.value
//version in Docker := version.value
//maintainer in Docker := "Gabriel Broilo <broilo.gabriel@gmail.com>"
//dockerBaseImage := "openjdk:latest"
//dockerRepository := Some("broilogabriel")
//dockerExposedPorts := Seq(9000, 9443)
