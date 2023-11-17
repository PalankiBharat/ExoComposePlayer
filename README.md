# ExoComposePlayer - Simple Player Built upon Exoplayer 
# :construction: Under Development :construction:

ExoComposePlayer is an Android library built upon ExoPlayer, designed to simplify the integration of video playback functionality into your Jetpack Compose applications. With a focus on simplicity and flexibility, ComposePlayerLib allows users to effortlessly add a video player to their app using just a URL. The library provides customizable player controls, ensuring a seamless and tailored user experience.




## Features

- Easy Integration: Add a fully functional video player to your Compose app with just a URL, simplifying the process of incorporating video playback.
- Custom Player Controls: Tailor the user interface to match your app's design by using customizable player controls provided by ComposePlayerLib.
- Built for Jetpack Compose: ComposePlayerLib is specifically designed for Jetpack Compose, making it easy to integrate with your existing Compose-based UI.
- Picture-in-Picture (PiP) Support: Enable Picture-in-Picture mode for a seamless, multitasking video playback experience, allowing users to continue watching content while navigating your app.


## Usage

Add Internet permission to manifest

```kotlin
<uses-permission android:name="android.permission.INTERNET"/>
```

Add usesCleartextTraffic as true in manifest
```kotlin
<application
    ...
    android:usesCleartextTraffic="true">
</application>
```

Add usesCleartextTraffic as true in manifest
```kotlin
ExoComposePlayer(
    modifier = Modifier.fillMaxWidth().aspectRatio(16f/9f),
    mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
)
```

To Use PIP mode in your App 
Add the following code to your activity with the Player
```kotlin
PipInitializer(this).initialize()
```

Add this code to the particular activity manifest
```kotlin
android:supportsPictureInPicture="true"
android:configChanges="navigation|screenLayout|orientation"
```


## Installation
- Add jitpack.io to your project

Groovy
```
repositories {
    ...
    maven { url "https://jitpack.io" }
}
```

Kotlin
```
repositories {
    ...
    maven ( url = "https://jitpack.io" )
}
```

- Add Chimney library dependency

Groovy
``` 
dependencies { 
  ...
  implementation 'com.github.PalankiBharat:ExoPlayerPlus:0.1.9'
}
```

Kotlin
``` 
dependencies { 
  ...
  implementation ("com.github.PalankiBharat:ExoPlayerPlus:0.1.9")
}
```
    
## License

Copyright 2023 PalankiBharat (P N Bharat Kumar)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
