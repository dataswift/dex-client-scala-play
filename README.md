# Scala API wrappers for the HAT MarketSquare

Current Version: 2.2.0

This repository provides convenience wrappers around HAT MarketSquare HTTP APIs and contains 
the most up-to-date set of typesafe MarketSquare Data Models and Play-JSON based
serializers and deserializers for them.

It relies on Play-WS for an asynchronous HTTP client.

## Usage

The library artifacts are hosted on AWS S3:
 
    resolvers += "HAT Library Artifacts Releases" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-releases.hubofallthings.com"
    // Or for SNAPSHOTS:
    // "HAT Library Artifacts Snapshots" at "https://s3-eu-west-1.amazonaws.com/library-artifacts-snapshots.hubofallthings.com"
    libraryDependencies ++= "org.hatdex" %% "marketsquare-client-scala-play" % 2.2.0


To use the client, it is sufficient to create a new one with minimal configuration:

    new MarketsquareClient(wsClient, marketsquareAddress, schema)

Where:

- wsClient is an instance of the WS Client, ideally dependency-injected in most cases - check Play documentation for details
- marketsquareAddress is the fully-qualified domain name of the MarketSquare - should never be different than marketsquare.hubofallthings.com except for when in testing
- schema is the schema of the address, should never be anything else than "https://" (default if left out) except when in testing

The client is non-blocking and is built around standard Scala Futures. For example, to receive a list of Offer Claims for an offer you have put in you would run:

    val marketsquare = new MarketsquareClient(wsClient, marketsquareAddress, schema)
    for {
      claims <- marketsquare.offerClaims(accessToken, offerId)
    } yield claims

Here `accessToken` is an access token you have received from MarketSquare for your account and the `offerId` is the UUID of the offer you have put in and that has been satisfied
