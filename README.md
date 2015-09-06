# SimpleEventEmitter
[![wercker status](//app.wercker.com/status/d139811b49537595026bd8eeb9b92694/m/master "wercker status")](//app.wercker.com/project/bykey/d139811b49537595026bd8eeb9b92694)

Simple EventEmitter implementation like [node.js](//nodejs.org/api/events.html).

## Usage

```java
enum Status {
    CONNECTED, DISCONNECTED
}

SimpleEventEmitter<Status, String> emitter = new SimpleEventEmitter<>(Status.class);

emitter.on(Status.CONNECTED, new SimpleEventListener<String> {
    @Override
    public void run(String value) {
        // do something...
    }
});

emitter.on(Status.DISCONNECTED, new SimpleEventListener<String> {
    @Override
    public void run(String value) {
        // do something...
    }
});

emitter.emit(Status.CONNECTED, "Successfully connected !");
```

## License
licensed under [MIT-license](//izumin.mit-license.org/2015).