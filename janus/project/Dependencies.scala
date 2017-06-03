import Dependencies.{resolvers, scalaVersion}
import sbt._

object Dependencies {
  // scala version
  val scalaVersion = "2.12.2"

  // resolvers
  val resolvers = Seq(
    Resolver.sonatypeRepo("public")
  )

  // akka
  val reactiveKafka: ModuleID = "com.typesafe.akka" %% "akka-stream-kafka" % "0.16"
  val akka: Seq[ModuleID] = Seq(reactiveKafka)

  // logging
  val logback: ModuleID = "ch.qos.logback" % "logback-classic" % "1.1.7"
  val scalaLogging: ModuleID = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  val logs: Seq[ModuleID] = Seq(logback, scalaLogging)

}


trait Dependencies {

  val scalaVersionUsed: String = scalaVersion

  val commonResolvers: Seq[Resolver] = resolvers

  val mainDependencies: Seq[ModuleID] = Dependencies.logs ++ Dependencies.akka

  implicit class ProjectRoot(project: Project) {
    def root: Project = project in file(".")
  }

  implicit class ProjectFrom(project: Project) {

    private val commonDir = "modules"

    def from(dir: String): Project = project in file(s"$commonDir/$dir")
  }

  implicit class DependsOnProject(project: Project) {

    val dependsOnCompileAndTest = "test->test;compile->compile"

    def dependsOnProjects(projects: Project*): Project =
      project dependsOn (projects.map(_ % dependsOnCompileAndTest): _*)
  }

}