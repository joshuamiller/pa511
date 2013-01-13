(ns pa511.core
  (:require [clj-http.client :as client]
            [clojure.data.xml :as xml]
            [clj-time.format :as time])
  (:use pa511.geometry))

(defrecord Event [class type updated description location event-id create-time])

(defn- events-xml
  "Get and parse incident XML"
  []
  (xml/parse (java.io.StringReader. (:body (client/get "http://www.511pa.com/xml/createXML.aspx?createXMLFor=events&modeType=traffic&monthType=0&minX=-83.47412109375&maxX=-72.213134765625&minY=39.01918369029137&maxY=42.61779143282346")))))

(defn- content-for-tag-named
  "Pull content from tag in an XML node"
  [node tag]
  (first (:content (first (filter (fn [t] (= tag (:tag t))) node)))))

(def time-formatter
  (time/formatter "dd/MM/YYYY hh:mm:ss aa"))

(defn- create-event
  "Create an event from an event XML node"
  [node]
  (let [class (content-for-tag-named node :Event_Class)
        type (content-for-tag-named node :Event_Type)
        updated (time/parse time-formatter (content-for-tag-named node :Last_Update))
        description (content-for-tag-named node :Event_Description)
        latitude (Float/parseFloat (content-for-tag-named node :Lat))
        longitude (Float/parseFloat (content-for-tag-named node :Lon))
        event-id (content-for-tag-named node :Event_ID)
        create-time (time/parse time-formatter (content-for-tag-named node :CREATE_TIME))]
    (->Event class type updated description (->Point latitude longitude) event-id create-time)))

(defn load-events
  "Load and interpret events"
  []
  (let [events (map :content (:content (events-xml)))]
    (def ^:dynamic *events* (map (fn [e] (create-event e)) events))))

(defn incidents
  "All current incidents"
  []
  (filter (fn [e] (= "1" (:class e))) *events*))

(defn roadwork
  "All current road work"
  []
  (filter (fn [e] (= "3" (:class e))) *events*))

(defn special-events
  "All current special events"
  []
  (filter (fn [e] (= "14" (:class e))) *events*))

(defn within
  "Filter a collection of records with a location to find which are
   within a radius from a given point"
  [origin radius collection]
  (filter #(within-radius? origin (:location %) radius) collection))
