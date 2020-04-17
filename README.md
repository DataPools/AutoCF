# AutoCF
AutoCF stands for "Automated Content Farm" and is an application that creates videos from comments on Reddit threads. This is in response to a proliferation of videos featuring Reddit comments read by TTS on YouTube.

With AutoCF, you can automate the creation of these low quality videos.

You can view an example of a generated video [here](https://www.youtube.com/watch?v=bQcwFujpVX8).

## Installation
You can run AutoCF by doing the following:
```
git clone https://github.com/DataPools/AutoCF
cd AutoCF
./gradlew run
```
This will install all necessary dependencies and run it. It can take ~20 seconds to start displaying anything.

## Configuration
```
{
  "postId": "g14q8l",
  "maxPosts": 20,
  "videoLength": 300
}
```
AutoCF can be configured through the `config.json` file. The config is generated after it is run for the first time.

Inside this file are options for the Reddit thread's post id, the maximum amount of posts you want to show, and the amount of time (in seconds) until an ending screen is shown.

## TTS
AutoCF narration only works on OS X, because it has a built in TTS engine that Java can interface with. Currently, Windows and Linux do not have narrator support. 

## Contributing

Pull Requests are welcome. The following still need to be done:
* Make the comments look more like actual Reddit.
* Clean up any Reddit markdown edge cases, as JRAW gives the comment's unformatted raw text
* Add cross-platform narrator support 

## License

This project is licensed under GPLv3.
