# Katalon Studio Open-source Testing Framework

## Companion products

### Katalon TestOps

[Katalon TestOps](https://analytics.katalon.com) is a web-based application that provides dynamic perspectives and an insightful look at your automation testing data. You can leverage your automation testing data by transforming and visualizing your data; analyzing test results; seamlessly integrating with such tools as Katalon Studio and Jira; maximizing the testing capacity with remote execution.

* Read our [documentation](https://docs.katalon.com/katalon-analytics/docs/overview.html).
* Ask a question on [Forum](https://forum.katalon.com/categories/katalon-analytics).
* Request a new feature on [GitHub](CONTRIBUTING.md).
* Vote for [Popular Feature Requests](https://github.com/katalon-analytics/katalon-analytics/issues?q=is%3Aopen+is%3Aissue+label%3Afeature-request+sort%3Areactions-%2B1-desc).
* File a bug in [GitHub Issues](https://github.com/katalon-analytics/katalon-analytics/issues).

### Katalon Studio
[Katalon Studio](https://www.katalon.com) is a free and complete automation testing solution for Web, Mobile, and API testing with modern methodologies (Data-Driven Testing, TDD/BDD, Page Object Model, etc.) as well as advanced integration (JIRA, qTest, Slack, CI, Katalon TestOps, etc.). Learn more about [Katalon Studio features](https://www.katalon.com/features/).

## What is it for

You can use this project for two purposes:
* Contribute new Katalon Studio functions for the community.
* Assist in debugging your test cases.

## What can be contributed

### For existing Keywords
* Enhance or fix bugs.
* Write better documents or samples at https://docs.katalon.com.
* Write Test Cases for existing Keywords.

### For new Keywords
* Develop new Keywords, including ones for working with a particular framework such as React or Angular.
* Write Test Cases for these new Keywords.

## How to contribute a new Keywords

This project requires Katalon Studio 5.10.0.

1. Fork this repository.
2. Open this project with Katalon Studio.
3. Add a new Keyword with at least one Test Case to verify.
4. Create a pull request.

The following files demonstrate how to contribute a new keyword:
* [Include/scripts/groovy/com/kms/katalon/core/webui/keyword/WebUiBuiltInKeywords.groovy](Include/scripts/groovy/com/kms/katalon/core/webui/keyword/WebUiBuiltInKeywords.groovy).
* [Include/scripts/groovy/com/kms/katalon/core/webui/keyword/builtin/HelloWorldFromWebUiKeyword.groovy](Include/scripts/groovy/com/kms/katalon/core/webui/keyword/builtin/HelloWorldFromWebUiKeyword.groovy).
* Test Cases/HelloWorldFromWebUiTestCase (opened using Katalon Studio).

## Troubleshooting

## License

Copyright (c) Katalon LLC. All rights reserved.

Licensed under the LICENSE AGREEMENT FOR KATALON AUTOMATION FRAMEWORK.
