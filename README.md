# Legofy [![Build Status](https://travis-ci.org/JeroenMols/Legofy.svg?branch=master)](https://travis-ci.org/JeroenMols/Legofy)
Android library to Legofy any image.

<a href='https://play.google.com/store/apps/details?id=com.jeroenmols.brickeffect&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-ap-PartBadge-Mar2515-1'>
  <img alt='Get it on Google Play' width="200"
       src='https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png'/>
</a>

See the application in action:

<p align="center">
  <img src="https://github.com/JeroenMols/Legofy/blob/master/play-store/preview.gif" alt="Legofy in action" height="550"/>
</p>

## Features
* Convert any Bitmap into a legofied version
* Upscale low resolution bitmaps
* Limit output size to 1080px to avoid out of memory exceptions

## How to use
  1. Add the Jitpack repository to your project:
```groovy
          repositories {
              maven { url "https://jitpack.io" }
          }
```
  2. Add a dependency on the library:
```groovy
          compile 'com.github.JeroenMols:Legofy:0.0.1-beta'
```
  3. Legofy a Bitmap
```java
          Bitmap legofiedBitmap = Legofy.with(this).convert(bitmap);
          Bitmap legofiedBitmap = Legofy.with(this).amountOfBricks(40).convert(bitmap);
```

## Next steps
* Drawable/ImageView with a dissolve effect
* Support for Picasso
* Synchronous/Asynchronous API
* Specify image size
* High resolution options
* Library that applies a Lego effect to an existing bitmap (first draft done)

## Questions
[@molsjeroen](https://twitter.com/molsjeroen)

## Credits
- [Mona Lisa](http://www.wikiart.org/en/leonardo-da-vinci/mona-lisa?utm_source=returned&utm_medium=referral&utm_campaign=referral) - Leonard Da Vinchi
- [Son of a man](http://www.wikiart.org/en/rene-magritte/son-of-man-1964) - Rene Magritte
- [Android Developers logo](http://android-developers.blogspot.nl/) - Google
