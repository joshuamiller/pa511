(ns pa511.geometry)

(defrecord Point [latitude longitude])

(def R 3958.761) ; radius of the Earth in miles

(defn distance ; Haversine formula via http://www.gettingclojure.com/cookbook:numbers
  "Compute distance between two Points"
  [point1 point2]
  (let [lat1 (:latitude point1)
        lon1 (:longitude point1)
        lat2 (:latitude point2)
        lon2 (:longitude point2)
        [lat1-r lon1-r lat2-r lon2-r] (map #(Math/toRadians %) [lat1 lon1 lat2 lon2])]
    (* R (Math/acos (+ (* (Math/sin lat1-r) (Math/sin lat2-r))
                       (* (Math/cos lat1-r) (Math/cos lat2-r) (Math/cos (- lon1-r lon2-r))))))))

(defn within-radius?
  "Determines whether a given point is within a given radius for another point."
  [origin destination radius]
  (< (distance origin destination) radius))
