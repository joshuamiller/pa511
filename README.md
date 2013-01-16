# pa511

This is a Clojure library for retrieving and parsing the PA 511 data
as [provided by PennDOT](http://www.511pa.com/Traffic.aspx).

## Usage

From `lein repl`...

```
user=> (use 'pa511.core)
nil
user=> (load-events)
true
user=> (:location (first (incidents)))
#pa511.geometry.Point{:latitude 40.03062, :longitude -78.492905}
```
Find some incidents within a radius in miles...
```
user=> (def hbg {:latitude 40.25 :longitude -76.75})
#'user/hbg
user=> (within hbg 15 (incidents))
(#pa511.core.Event{:class "1", :type "travel advisory", :updated #<DateTime 2012-03-09T12:36:44.000Z>, :description "PA Turnpike: ...
```

## License

Copyright © 2013 Joshua Miller

Distributed under the MIT License
