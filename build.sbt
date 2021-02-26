import Dependencies._

configs(IntegrationTest)

Defaults.itSettings

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.4"

libraryDependencies ++= Seq(
  Library.Play.ws,
  Library.Play.json,
  Library.Play.jsonJoda,
  Library.testCommon,
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

inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalafixScalaBinaryVersion := "2.13"
  )
)
