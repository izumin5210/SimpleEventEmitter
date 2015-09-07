# SimpleEventEmitter
[![wercker status](https://app.wercker.com/status/d139811b49537595026bd8eeb9b92694/s/master "wercker status")](https://app.wercker.com/project/bykey/d139811b49537595026bd8eeb9b92694)
[![Download](https://api.bintray.com/packages/izumin5210/maven/SimpleEventEmitter/images/download.svg)](https://bintray.com/izumin5210/maven/SimpleEventEmitter/_latestVersion)

Simple EventEmitter implementation like [node.js](//nodejs.org/api/events.html).

## Usage

```java
enum Status {
    CONNECTED, DISCONNECTED
}

SimpleEventEmitter<Status, String> emitter = new SimpleEventEmitter<>(Status.class);

emitter.on(Status.CONNECTED, String.class, new SimpleEventListener<String> {
    @Override
    public void run(String value) {
        // do something...
    }
});

emitter.on(Status.DISCONNECTED, Integer.class, new SimpleEventListener<Integer> {
    @Override
    public void run(Integer value) {
        // do something...
    }
});

emitter.emit(Status.CONNECTED, String.class, "Successfully connected !");
```

## License
licensed under [MIT-license](//izumin.mit-license.org/2015).