# Filter Tools

[![Build Status](https://travis-ci.org/stefanbirkner/filter-tools.svg?branch=master)](https://travis-ci.org/stefanbirkner/filter-tools)

System Rules is a collection of classes for developers who are working
with Servlet Filters.

Filter Tools is published under the
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

## Installation

Filter Tools is available from
[Maven Central](http://search.maven.org/).

    <dependency>
      <groupId>com.github.stefanbirkner</groupId>
      <artifactId>filter-tools</artifactId>
      <version>1.4.0</version>
    </dependency>


## Usage

Filter Tools' documentation is stored in the `gh-pages` branch and is
available online at
http://stefanbirkner.github.io/filter-tools/index.html


## Contributing

You have three options if you have a feature request, found a bug or
simply have a question about Filter Tools.

* [Write an issue.](https://github.com/stefanbirkner/filter-tools/issues/new)
* Create a pull request. (See [Understanding the GitHub Flow](https://guides.github.com/introduction/flow/index.html))
* [Write a mail to mail@stefan-birkner.de](mailto:mail@stefan-birkner.de)


## Development Guide

Filter Tools is build with [Maven](http://maven.apache.org/). If you
want to contribute code than

* Please write a test for your change.
* Ensure that you didn't break the build by running `mvn test`.
* Fork the repo and create a pull request. (See [Understanding the GitHub Flow](https://guides.github.com/introduction/flow/index.html))

The basic coding style is described in the
[EditorConfig](http://editorconfig.org/) file `.editorconfig`.

Filter Tools supports [Travis CI](https://travis-ci.org/) for
continuous integration. Your pull request will be automatically build
by Travis CI.


## Release Guide

* Select a new version according to the
  [Semantic Versioning 2.0.0 Standard](http://semver.org/).
* Set the new version in the `Installation` section of this readme.
* `mvn release:prepare`
* `mvn release:perform`
