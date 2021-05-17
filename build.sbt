import Dependencies._

inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalafixScalaBinaryVersion := "2.13",
    scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"
  )
)

scalaVersion := "2.13.6"

configs(IntegrationTest)

Defaults.itSettings

libraryDependencies ++= Seq(
  Library.Play.ws,
  Library.Play.json,
  Library.Play.jsonJoda,
  Library.testCommon % Test,
  Library.DataswiftModels.hatPlay,
  Library.DataswiftModels.dexPlay
)

publishMavenStyle := true
publishTo := {
  val prefix = if (isSnapshot.value) "snapshots" else "releases"
  Some(
    s"Models$prefix" at s"s3://library-artifacts-$prefix.hubofallthings.com"
  )
}
