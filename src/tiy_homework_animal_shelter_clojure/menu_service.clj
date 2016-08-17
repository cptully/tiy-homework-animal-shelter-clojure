(ns tiy-homework-animal-shelter-clojure.menu-service
  (:require [clojure.string :as str]))



;; magic numbers
(def list-animals 1)
(def add-animal 2)
(def view-animals 3)
(def edit-animal 4)
(def remove-animal 5)
(def quit 6)

(defn parse-int [s]
  (Integer/parseInt (re-find #"\A-?\d+" s)))

(defn wait-for-int
  ([message min max]
   (println message)
   (try
     (let [value (parse-int (read-line))]
       (if (not (and (>= value min) (<= value max)))
         (throw (Exception. "Invalid option."))
         value))
     (catch Exception e
       (do
         (println (str "\nPlease provide a number between " min " and " max ".\n"))
         (wait-for-int message min max)))))
  ([message]
   (println message)
   (try
     (parse-int (read-line))
     (catch Exception e
       (do
         (println (str "\nPlease provide a number.\n"))
         (wait-for-int message))))))

(defn wait-for-string [message required]
  (println message)
  (let [value (str/trim (read-line))]
    (if (and required (= (count value) 0))
      (do
        (println "\nPlease provide a value.\n")
        (wait-for-string message required))
      value)))


(defn prompt-for-menu-selection []
  (print "\n\n-- Main Menu --\n"
    "\n"
    "1) List animals\n"
    "2) Create an animal\n"
    "3) View animal details\n"
    "4) Edit an animal\n"
    "5) Delete an animal\n"
    "6) Quit\n")
  (wait-for-int "Please choose an option from the menu:" 1 6))

(defn display-animals [animals]
  (if (= (count animals) 0)
    (do
      (println "The list of animals in the shlter is empty.")
      (wait-for-string "press any key to return to the menu" false))
    (do
      ((print "index\tName\t\tSpecies\tBreed\n")
       (for [i (range 0 (- (count animals) 1))]
         (print (+ i 1) "\t" (:name (nth animals i)) "\t"(:species (nth animals i)) "\t" (:breed (nth animals i))))
       (wait-for-string "press any key to return to the menu" false))))
  animals)

(defn create-animal []
  (println "\n--- Add a new animal to the shelter's roster --\n")
  {:name (wait-for-string "Name: " true)
   :species (wait-for-string "Species: " true)
   :breed (wait-for-string "Breed: " true)
   :description (wait-for-string "Description or notes:" true)})

(defn view-animal [animals]
  (def index (wait-for-int "please enter the idex of the animal to view. Enter 0 to display the list of animals" 0 (count animals)))
  (if (> index 0)
    (do
      (print "\nName: " (:name (nth animals index)) "\n")
      (print "Species: " (:species (nth animals index) "\n"))
      (print "Breed: " (:breed (nth animals index) "\n"))
      (print "Description: " (:description (nth animals index) "\n"))
      (wait-for-string "Press any key to retunr to the menu" false))
    (display-animals animals))
  animals)

(defn edit-animal [animals]
  (def index (wait-for-int "please enter the idex of the animal to view. Enter 0 to display the list of animals" 0 (count animals)))
  (if (> index 0)
    (do
      (print "\nName: " (:name (nth animals index)) "\n")
      (print "Species: " (:species (nth animals index) "\n"))
      (print "Breed: " (:breed (nth animals index) "\n"))
      (print "Description: " (:description (nth animals index) "\n"))
      (wait-for-string "Press any key to retunr to the menu" false))
    (display-animals animals))
  animals)


;; (defn numeric? [s]
;;   (if-let [s (seq s)]
;;     (let [s (if (= (first s) \-) (next s) s)
;;           s (drop-while #(Character/isDigit %) s)
;;           s (if (= (first s) \.) (next s) s)
;;           s (drop-while #(Character/isDigit %) s)]
;;       (empty? s))))



