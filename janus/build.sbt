import Settings._
import sbt.Keys.version
import sbt._

def createModule(p: Project, moduleName: String): Project = p.from(moduleName)
  .setName(moduleName)
  .setDescription(s"Janus Project: Module - $moduleName")
  //  .setInitialCommand("_")
  .configureModule
  .configureIntegrationTests
  .configureFunctionalTests
  .configureUnitTests
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

lazy val root = project.root
  //  .setInitialCommand("_")
  .configureRoot
  .dependsOn(core, app, producer)
  .aggregate(core, app, producer)

lazy val core = project.from("core")
  .setName("core")
  //  .setDescription(projectDescription("core"))p
  //  .setInitialCommand("_")
  .configureModule

lazy val app = createModule(project, "app")
  .dependsOnProjects(core)

lazy val producer = createModule(project, "producer")
  .dependsOnProjects(core)


//packageName in Docker := packageName.value
//version in Docker := version.value
//maintainer in Docker := "Gabriel Broilo <broilo.gabriel@gmail.com>"
//dockerBaseImage := "openjdk:latest"
//dockerRepository := Some("broilogabriel")
//dockerExposedPorts := Seq(9000, 9443)
