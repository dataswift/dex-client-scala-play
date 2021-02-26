import Dependencies._

configs(IntegrationTest)

Defaults.itSettings

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.4.4"

libraryDependencies ++= Seq(
  Library.Play.ws,
  //cacheApi,
  Library.Play.json,
  Library.Play.jsonJoda,
  Library.testCommon,
  //Library.Specs2.matcherExtra,
  //Library.Specs2.mock,
  //Library.Specs2.core,
  //Library.DataswiftModels.hat,
  Library.DataswiftModels.hatPlay,
  //Library.DataswiftModels.dex,
  Library.DataswiftModels.dexPlay,
  //Library.Test.scalacheck,
  //Library.Test.scalatest,
  //Library.Test.funsuite,
  //Library.Test.testpluscheck,
  //Library.Test.matchers,
  //Library.Test.logging
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
