import Dependencies._

configs(IntegrationTest)

Defaults.itSettings

libraryDependencies ++= Seq(
  Library.Play.ws,
  Library.Play.cache,
  Library.Play.json,
  Library.Play.jsonJoda,
  Library.Specs2.matcherExtra,
  Library.Specs2.mock,
  Library.Specs2.core,
  Library.HATDeX.hatClient,
  Library.Test.scalacheck,
  Library.Test.scalatest,
  Library.Test.funsuite,
  Library.Test.testpluscheck,
  Library.Test.matchers,
  Library.Test.logging
)

publishTo := {
  val prefix = if (isSnapshot.value) "snapshots" else "releases"
  Some(s3resolver.value("HAT Library Artifacts " + prefix, s3("library-artifacts-" + prefix + ".hubofallthings.com")) withMavenPatterns)
}

inThisBuild(
  List(
    scalaVersion := "2.13.3",
    scalafixScalaBinaryVersion := "2.13",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)