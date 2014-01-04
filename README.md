diffbot-java-sdk
================

A [Diffbot](http://www.diffbot.com/) client for Java.

Features
--------

  * Support multiple HTTP implementation, including Google App Engine [URL Fetch](https://developers.google.com/appengine/docs/java/urlfetch/), [Apache HTTP Client](http://hc.apache.org/) and the default JVM HTTP client
  * Support multiple JSON parser, including [Gson](https://code.google.com/p/google-gson/) and [Jackson 2](http://jackson.codehaus.org/)
  * Support the Article API and the Frontpage API
  * Support the batch API
  * Published on Maven Central Repository

Getting started
---------------
### Including the SDK in your project

The easiest way to incorporate the SDK into your Java project is to use Maven. Simply add a new dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.syncthemall</groupId>
	<artifactId>diffbot-java-sdk</artifactId>
	<version>1.0</version>
</dependency>
```

### Choose your HTTP and JSON implementations

In order to uses [URL Fetch](https://developers.google.com/appengine/docs/java/urlfetch/) as your HTTP implementation (if your run diffbot-java-sdk on GAE) add to your `pom.xml`:
```xml
<dependency>
	<groupId>com.google.http-client</groupId>
	<artifactId>google-http-client-appengine</artifactId>
	<version>1.14.1-beta</version>
</dependency>
```

In order to uses [Gson](https://code.google.com/p/google-gson/) as your JSON parser add to your `pom.xml`:
```xml
<dependency>
	<groupId>com.google.http-client</groupId>
	<artifactId>google-http-client-gson</artifactId>
	<version>1.14.1-beta</version>
</dependency>
```

In order to uses [Jackson 2](http://jackson.codehaus.org/) as your JSON parser add to your `pom.xml`:
```xml
<dependency>
	<groupId>com.google.http-client</groupId>
	<artifactId>google-http-client-jackson2</artifactId>
	<version>1.14.1-beta</version>
</dependency>
```
In order to uses [Apache HTTP Client](http://hc.apache.org/) or the default JVM HTTP client, no addition are necessary.

Usage
-----
### Instantiate the API main class
The `DiffbotAPI` class is thread-safe and should be instantiated only once. If the batch API is used it **has to** be instantiated only once as it store the list request to execute in a batch.

For example if you use [Apache HTTP Client](http://hc.apache.org/) and [Jackson 2](http://jackson.codehaus.org/):
```java
	private static DiffbotAPI api;
	...
	api = new DiffbotAPI(new ApacheHttpTransport(), new JacksonFactory(), "<your_dev_token>");
```

### Direct API calls
To call the Article API:
```java
Article article = api.article(new ArticleRequest("http://www.cbs.com/shows/how_i_met_your_mother/barneys_blog/1000461/").withTags().withComments().withSummary());
```

To call the Frontpage API:
```java
Frontpage frontpage = api.frontpage(new FrontpageRequest("http://theverge.com"));
```

### Batch API

The batch API allows to prepare multiple Article and/or Frontpage request and to send them all at once. The batch API:
 * limits the number of API calls and reduces the network load, by avoiding http overhead for every request
 * slightly improve the performances if a lot of requests are executed
 * limits the usage of the Diffbot quota

To prepare requests to be executed in batch:
```java
Future<Article> fArticle = api.batch(new ArticleRequest("http://www.cbs.com/shows/how_i_met_your_mother/barneys_blog/1000461").withTags().withComments().withSummary());
Future<Frontpage>; fFrontpage = api.batch(new FrontpageRequest("http://theverge.com"));
```

Note that this can be done by multiple thread. The DiffbotAPI class is fully thread-safe.
At this point no call has been done to Diffbot. To obtain the results:
```java
Article article = fArticle.get();
Frontpage frontpage = fFrontpage.get();
```

The first line trigger a batch request that retrieves the results for all the requests added since the last call to `Future#get()`. The second line doesn't need to do any API call as the result was retrieve during the
execution of the fist line.


How to contribute
--------------

### Reporting a Bug / Requesting a Feature

To report an issue or request a new feature you just have to open an issue in the repository issue tracker (<https://github.com/vanduynslagerp/diffbot-java-sdk/issues>).

### Contributing to the code

To contribute, follow this steps:

 1. Fork this project
 2. Add the progress label to the issue you want to solve (add a comments to say that you work on it)
 3. Create a topic branch for this issue
 4. When you have finish your work, open a pull request (use the issue title for the pull request title)

License 
--------------
The project is licensed under the MIT license. 
See LICENSE for details.

Disclamer
---------
The Diffbot API currently present some "non-standard", "uneasy to deal with" specificities. The SDK work around this problems, but in some cases it might create unexpected issues.

### Article API: Errors management
Once requested the Article API return an HTTP response with headers, a status code, and a JSON content.
Usually REST API returns an HTTP response with a status code indicating to the client (SDK) how the request was processed by the API, and if there is some error. This way a client can parse the JSON as a regular object if the error code is `200` and as an error object otherwise.
Diffbot always answers a `200` status code, which mean there is no way for the SDK to knows if the JSON content represents an error object or an `Article` object.

*The SDK work around this problem by looking for an error code ins the JSON content be fore parsing.*

### Frontpage API: JSON structure impossible to parse
The Frontpage API can return either an XML or a JSON response. Unfortunatly the JSON message returned by the Frontpage API is not parsable.

*The SDK uses the API with XML format an unmarshall with JAXB. That creates a dependency to JAXB (it is included in the JDK though).*

### Frontpage API: Errors management
The Frontpage API presents the same issue with error management as the Article API (status code always 200).
In addition, the frontpage API return an JSON message when an error occurs even if the XML format has been requested.

*To workaround this problem the SDK test the content type of the http response. If it's XML it's JSON then the content is mapped to an error object.*

It seems that Diffbot recently changed the error management for the Frontpage API. Now an identical XML is returned no matter if the API encountered an error. The error message is in the title attribute of the XML.
Therefore there is no way for the SDK to know if there was an error in the API.

*If an error happens on the API side, a `Frontpage` object will be return with the error message in the title attribute.*

### Batch API: Matching request and results
The principle of the batch API is to prepare a list of request and execute them all at once in one batch request containing a list of sub-requests.
The batch API respond with a list of sub-responses, each one corresponding one of the sub-request.
Diffbot doesn't provide any way to match the sub-request in a batch with the sub-results (like an id shared by a sub-request and a sub-answer).
The SDK assumes Diffbot answers a batch request with one answer for every sub-request and keep the sub-answers in the same order as the sub-requests.

*According the tests done that's the way Diffbot works, it preserve the order and return one sub-response for every sub-requests.*

### All API: documentation
There is no documentation from Diffbot regarding the potential error message returned by the API. Therefore using the API involves a lot of try and fail to determines what are the potential error messages.
The SDK try to simplify that by sorting the error in different `Exception` (see javadoc).

[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/vanduynslagerp/diffbot-java-sdk/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

