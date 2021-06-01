import Dependencies._
import play.sbt.PlayImport

inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalafixScalaBinaryVersion := "2.13",
    scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"
  )
)

configs(IntegrationTest)

Defaults.itSettings

libraryDependencies ++= Seq(
  PlayImport.ws,
  Library.HatPlay,
  Library.DexPlay,
  Library.TestCommon % Test
)

publishMavenStyle := true
publishTo := {
  val prefix = if (isSnapshot.value) "snapshots" else "releases"
  Some(
    s"Models$prefix" at s"s3://library-artifacts-$prefix.hubofallthings.com"
  )
}
