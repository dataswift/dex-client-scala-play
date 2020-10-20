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
  val prefix               = if (isSnapshot.value) "snapshots" else "releases"
  val s3BucketFriendlyName = "HAT Library Artifacts"
  val s3BucketName         = "library-artifacts-"
  val s3DomainSuffix       = ".hubofallthings.com"
  Some(
    s3resolver
      .value(List(s3BucketName, prefix).mkString(""), s3(s3BucketName + prefix + s3DomainSuffix)) withMavenPatterns
  )
}

inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalafixScalaBinaryVersion := "2.13"
  )
)
