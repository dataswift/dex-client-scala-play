import Dependencies._

libraryDependencies ++= Seq(
  Library.Play.ws,
  Library.Play.cache,
  Library.Play.json,
  Library.Play.jsonJoda,
  Library.Specs2.matcherExtra,
  Library.Specs2.mock,
  Library.Specs2.core,
  Library.HATDeX.hatClient,

  Library.Akka.http,
  Library.Akka.sprayJson,
  Library.Akka.stream,
  Library.Akka.typedActor,
  Library.Logging.akkaLogger,
  Library.Logging.logback,
  Library.Test.akkaHttpTestkit,
  Library.Test.akkaTestkit,
  Library.Test.scalaTest
)

publishTo := {
  val prefix = if (isSnapshot.value) "snapshots" else "releases"
  Some(s3resolver.value("HAT Library Artifacts " + prefix, s3("library-artifacts-" + prefix + ".hubofallthings.com")) withMavenPatterns)
}
