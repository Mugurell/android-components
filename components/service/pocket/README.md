# [Android Components](../../../README.md) > Service > Pocket

A library for easily getting Pocket recommendations that transparently handles downloading and caching and periodically refreshing Pocket data.
Currently this supports Pocket recommended articles.

## Usage
- Use PocketStoriesService#startPeriodicStoriesRefresh and PocketStoriesService#stopPeriodicStoriesRefresh
as higher up in the client app as possible (preferable in the Application or the single Activity) ensuring the
background article refresh functionality works for the entirety of the app lifetime.
- Use an instance of PocketStoriesService#getStories to get the current list of Pocket recommended articles.

### Setting up the dependency

Use Gradle to download the library from [maven.mozilla.org](https://maven.mozilla.org/) ([Setup repository](../../../README.md#maven-repository)):

```Groovy
implementation "org.mozilla.components:service-pocket:{latest-version}"
```

## License

    This Source Code Form is subject to the terms of the Mozilla Public
    License, v. 2.0. If a copy of the MPL was not distributed with this
    file, You can obtain one at http://mozilla.org/MPL/2.0/
