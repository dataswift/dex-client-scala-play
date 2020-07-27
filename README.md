# Scala API wrappers for the HAT DEX

Current Version: 2.4.1

This repository provides convenience wrappers around HAT DEX HTTP APIs and contains 
the most up-to-date set of typesafe DEX Data Models and Play-JSON based
serializers and deserializers for them.

It relies on Play-WS for an asynchronous HTTP client.

## Usage

The library artifacts are hosted on AWS S3:
 
    resolvers += "HAT Library Artifacts Releases" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-releases.hubofallthings.com"
    // Or for SNAPSHOTS:
    // "HAT Library Artifacts Snapshots" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-snapshots.hubofallthings.com"
    libraryDependencies ++= "org.hatdex" %% "dex-client-scala-play" % 2.4.1


To use the client, it is sufficient to create a new one with minimal configuration:

    new DexClient(wsClient, dexAddress, schema)

Where:

- wsClient is an instance of the WS Client, ideally dependency-injected in most cases - check Play documentation for details
- dexAddress is the fully-qualified domain name of the DEX - should never be different than dex.hubofallthings.com except for when in testing
- schema is the schema of the address, should never be anything else than "https://" (default if left out) except when in testing

The client is non-blocking and is built around standard Scala Futures. For example, to receive a list of Offer Claims for an offer you have put in you would run:

    val dex = new DexClient(wsClient, dexAddress, schema)
    for {
      claims <- dex.offerClaims(accessToken, offerId)
    } yield claims

Here `accessToken` is an access token you have received from DEX for your account and the `offerId` is the UUID of the offer you have put in and that has been satisfied

## Tests

### Integ Tests
* sbt it:test
* Tests require a running Dex. Todo this run the dex test script from the dex project `./end_to_end_test/test.sh -s`
* Currently Integ tests are only able to work against unauthorized endpoints

### Unit Tests
* Mock server
* sbt test



