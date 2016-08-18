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

(defn wait-for-enter [message]
   (println message)
   (str/trim (read-line))
   0)


(defn prompt-to-continue []
  (println "press enter to continue...")
  (let [value (str/trim (read-line))]
    (if (= (count value) 0)
      (do
        (println "\nPlease press <Enter>\n")
        (prompt-to-continue))
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
      (println "The list of animals in the shelter is empty.\n"))
    (do
      (println "Name\tSpecies\tBreed")
      (println "=========================================")
      (doseq [animal animals]
        (println (+ (.indexOf animals animal) 1) "\t" (:name animal) "\t" (:species animal) "\t" (:breed animal)))))
  (wait-for-enter "\nPress Enter to continue...")
  animals)

(defn create-animal [animals]
  (println "\n--- Add a new animal to the shelter's roster --\n")
  (conj animals {:name (wait-for-string "Name: " true)}
   :species (wait-for-string "Species: " true)
   :breed (wait-for-string "Breed: " true)
   :description (wait-for-string "Description or notes:" true)))

(defn view-animal [animals]
  (let [index (wait-for-int "Please enter the idex of the animal to view. Enter 0 to display the list of animals" 0 (count animals))]
    (if (> index 0)
      (let [index (- index 1)
            animal (nth animals index)]
        (println "Name: " (:name animal))
        (println "Species: " (:species animal))
        (println "Breed: " (:breed animal))
        (println "Description: " (:description animal))
        (wait-for-enter "Press Eneter top continue..."))
      (display-animals animals)))
  animals)

(defn edit-animals [animals]
  ;; ask which animal to edit index is 1 based from the UI
  (let [animals animals
        index (wait-for-int (str "Please enter the idex of the animal to edit."
                                 "Enter 0 to display the list of animals") 0 (count animals))]

    ;; if the user typed a valid index
    (if (> index 0)
      ;; then edit the animal
      (let [index (dec index)  ;; index is 0 based for indexing the array
            ;;animals animals
            animal (nth animals index)

            ;; prompt with existing data and ask for new data
            name (wait-for-string (str "Name: " (:name animal) "\n") false)
            species (wait-for-string (str "Species: " (:species animal "\n")) false)
            breed (wait-for-string (str "Breed: " (:breed animal "\n")) false)
            description (wait-for-string (str "Description: " (:description animal "\n")) false)

            ;; check for null returns from user
            name2 (if (= name "") (:name animal) name)
            species2 (if (= species "") (:species animal) species)
            breed2 (if (= breed "") (:breed animal) breed)
            description2 (if (= description "") (:description animal) description)

            ;; update local copy of animal
            animal (assoc animal :name name2 :species species2 :breed breed2 :description description2)]
            ;; assoc fails to update animals
            ;; assoc-in crashes at runtime
           (conj animals animal))


      ;;(wait-for-enter "\nPress enter to return to the menu")

      ;; else dispolay the list of animals
      (display-animals animals))))


(defn delete-animal [animals]
  (let [animals animals
        index (- (wait-for-int (str "Please enter the idex of the animal to edit."
                                 "Enter 0 to display the list of animals") 0 (count animals)) 1)]
    (if (>= index 0)
      (let [animals animals
            animal (nth animals index)]
        (println "Name: " (:name animal))
        (println "Species: " (:species animal))
        (println "Breed: " (:breed animal))
        (println "Description: " (:description animal))
        (if (= (wait-for-string "Are you sure you want to remove this animal? (y/n)" true) "y")
          (conj (subvec animals 0 index) (subvec animals (inc index) (count animals))))))
    (display-animals animals)))




