logLevel := Level.Warn

resolvers += Resolver.typesafeRepo("releases")

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.4")
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.12")

// Code Quality
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")
//addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.1.0")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.1")

// web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.4.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.12")

// S3 based SBT resolver
resolvers += Resolver.jcenterRepo
addSbtPlugin("ohnosequences" % "sbt-s3-resolver" % "0.19.0")

resolvers += "HAT Library Artifacts Snapshots" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-snapshots.hubofallthings.com"
resolvers += "HAT Library Artifacts Releases" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-releases.hubofallthings.com"
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.4")
addSbtPlugin("io.dataswift" % "sbt-scalafmt-common" % "0.1.1")
