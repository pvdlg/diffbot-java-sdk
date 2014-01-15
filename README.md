diffbot-java-sdk
================

A [Diffbot](http://www.diffbot.com/) client for Java.

Features
--------

  * Support multiple HTTP implementation, including Google App Engine [URL Fetch](https://developers.google.com/appengine/docs/java/urlfetch/), [Apache HTTP Client](http://hc.apache.org/) and the default JVM HTTP client
  * Support multiple JSON parser, including [Gson](https://code.google.com/p/google-gson/) and [Jackson 2](http://jackson.codehaus.org/)
  * Support the Article API, Frontpage API, Image API, Product API and Clasifier API
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
	<version>1.2.0</version>
</dependency>
```

### Choose your HTTP and JSON implementations

In order to uses [URL Fetch](https://developers.google.com/appengine/docs/java/urlfetch/) as your HTTP implementation (if your run diffbot-java-sdk on GAE) add to your `pom.xml`:
```xml
<dependency>
	<groupId>com.google.http-client</groupId>
	<artifactId>google-http-client-appengine</artifactId>
	<version>1.17.0-rc</version>
</dependency>
```

In order to uses [Gson](https://code.google.com/p/google-gson/) as your JSON parser add to your `pom.xml`:
```xml
<dependency>
	<groupId>com.google.http-client</groupId>
	<artifactId>google-http-client-gson</artifactId>
	<version>1.17.0-rc</version>
</dependency>
```

In order to uses [Jackson 2](http://jackson.codehaus.org/) as your JSON parser add to your `pom.xml`:
```xml
<dependency>
	<groupId>com.google.http-client</groupId>
	<artifactId>google-http-client-jackson2</artifactId>
	<version>1.17.0-rc</version>
</dependency>
```
In order to uses [Apache HTTP Client](http://hc.apache.org/) or the default JVM HTTP client, no addition are necessary.

Usage
-----
### Instantiate the API main class
The `Diffbot` class is thread-safe and should be instantiated only once. If the batch API is used it **has to** be instantiated only once as it store the list request to execute in a batch.

For example if you use [Apache HTTP Client](http://hc.apache.org/) and [Jackson 2](http://jackson.codehaus.org/):
```java
	private static Diffbot diffbot;
	...
	diffbot = new Diffbot(new ApacheHttpTransport(), new JacksonFactory(), "<your_dev_token>");
```

### Direct API calls
To call the Article API:
```java
Article article = api.articles().analyze("<web page URL>").execute();
```

To call the Frontpage API:
```java
Frontpage frontpage = api.frontpages().analyze("<web page URL>").execute();
```

To call the Image API:
```java
Images images = api.images().analyze("<web page URL>").execute();
```

To call the Product API:
```java
Products products = api.products().analyze("<web page URL>").execute();
```

To call the Product API:
```java
Classified classified = api.classifier().analyze("<web page URL>").execute();
```
If the Classified resulting object is of type Article, Image or Product it can be parsed as respectively as a corresponding Model:
```java
if (classified.getType().equals(PageType.ARTICLE)) {
	Article article = classified.asArticle();
}
 ```

### Batch API

The batch API allows to prepare multiple Article, Frontpage, Image, Product and/or Classifier request and to send them all at once. The batch API:
 * limits the number of API calls and reduces the network load, by avoiding http overhead for every request
 * slightly improve the performances if a lot of requests are executed
 * limits the usage of the Diffbot quota

To prepare requests to be executed in batch:
```java
Future<Article> fArticle = api.articles().analyze("<web page URL>").withTags().queue();
Future<Frontpage> fFrontpage = api.frontpages().analyze("<web page URL>").queue();
Future<Images> fImages = api.images().analyze("<web page URL>").queue();
Future<Products> fProducts = api.products().analyze("<web page URL>").queue();
Future<Classified> fClassified = api.classified().analyze("<web page URL>").queue();
```

Note that this can be done concurrently by multiple threads. The Diffbot class is fully thread-safe.
At this point no call has been done to Diffbot. To obtain the results:
```java
Article article = fArticle.get();
Frontpage frontpage = fFrontpage.get();
Images images = fImages.get();
Products products = fProducts.get();
Classified classified = fClassified.get();
```

The first line trigger a batch request that retrieves the results for all the requests added since the last call to `Future#get()`. The second line doesn't need to do any API call as the result was retrieve during the
execution of the fist line.

Running the JUnit tests
-----------------------
In order to run the JUnit test add to your Maven settings.xml
```xml
<properties>
	<diffbot.key>0813abb2bfc05b5ea878e68848861259</diffbot.key>
</properties>
```

Change log
----------
### 1.2.0
  * Added Image API
  * Added Product API
  * Added Classifier API
  * Bug fixes in Batch API

### 1.1.0
  * Changed the code structure, inspired and based on Google API libs
  * Updated to Article API v2

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

The error management for the Frontpage API seems inconsistent. When requesting the Frontpage API with a inaccessible URL sometimes Diffbot return avalid XML response with the attribute title = URL, sometimes the title is "404 not found" and sometimes an error 500 is return. All these different cases happens with the same url in parameter.
Therefore there is no way for the SDK to know if there was an error in the API.
*If an error happens on the API side, a `Frontpage` object will be return with the error message in the title attribute.*

[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/vanduynslagerp/diffbot-java-sdk/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

