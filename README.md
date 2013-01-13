# pa511

This is a Clojure library for retrieving and parsing the PA 511 data 
as [provided by PennDOT](http://www.511pa.com/Traffic.aspx).

## Usage

From `lein repl`...

```
user=> (use 'pa511.core)
nil
user=> (load-events)
#'pa511.core/*events*
user=> (:location (first (incidents)))
[40.03062 -78.492905]
```

## License

Copyright © 2013 Joshua Miller

Distributed under the MIT License
