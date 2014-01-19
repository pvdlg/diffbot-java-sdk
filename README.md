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
	<version>1.2.1</version>
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

At this point no call has been done to Diffbot. To obtain the results:
```java
Article article = fArticle.get();
Frontpage frontpage = fFrontpage.get();
Images images = fImages.get();
Products products = fProducts.get();
Classified classified = fClassified.get();
```

The first line trigger one or more batch request (depending the concurrentBatchRequest value) that retrieves the results for up to value
set to'Diffbot.setConcurrentBatchRequest(<int value>)' * value set to 'Diffbot.setMaxBatchRequest(<int value>)' requests added since the last call to `Future#get()`.
The second line doesn't need to do any API call if the result was retrieved during the execution of the fist line.

Note that this can be done concurrently by multiple threads. The Diffbot class is fully thread-safe.

Running the JUnit tests
-----------------------
In order to run the JUnit test add to your Maven settings.xml
```xml
<properties>
	<diffbot.key><your API key></diffbot.key>
</properties>
```

Change log
----------
### 1.2.1
  * Error management improvement
  * Added the possibility to do multiple concurrent request to batch API

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

[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/vanduynslagerp/diffbot-java-sdk/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

