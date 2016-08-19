(ns tiy-homework-animal-shelter-clojure.menu-service
  (:require [clojure.string :as str]))

;; magic numbers
(def list-animals 1)
(def add-animal 2)
(def view-animals 3)
(def edit-animal 4)
(def remove-animal 5)
(def quit 6)


;; Utility functions

;; parse-int finds the intger at the beginning of a line of input
(defn parse-int [s]
  (Integer/parseInt (re-find #"\A-?\d+" s)))

;; wait-for-int is overloaded with two versions, one that take min/max values and one that doesn't
;; both versions wait for the user to type a number and use parse-int to extract the number from read-line
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

;; wait-for-string waits for the user to type one or more characters
(defn wait-for-string [message required]
  (println message)
  (let [value (str/trim (read-line))]
    (if (and required (= (count value) 0))
      (do
        (println "\nPlease provide a value.\n")
        (wait-for-string message required))
      value)))

;; wait-for enter waits for the user to type the <Enter> key
(defn wait-for-enter [message]
   (println message)
   (str/trim (read-line))
   0)

;; prompt-to-contine prompts the user to type <Enter> when they are ready to continue the program
(defn prompt-to-continue []
  (println "press enter to continue...")
  (let [value (str/trim (read-line))]
    (if (= (count value) 0)
      (do
        (println "\nPlease press <Enter>\n")
        (prompt-to-continue))
      value)))

;; prompt-for-menu-selection displays the main menu and waits fo rth user to choose the operation to complete
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

;; create-animal prompts the user for info to create a new animal record
(defn create-animal [animals]
  (println "\n--- Add a new animal to the shelter's roster --\n")
  (conj animals {:name (wait-for-string "Name: " true)
                 :species (wait-for-string "Species: " true)
                 :breed (wait-for-string "Breed: " true)
                 :description (wait-for-string "Description or notes:" true)}))

(defn display-animals [animals prompt]
  (if (= (count animals) 0)
    (println "The list of animals in the shelter is empty.\n")
    (do
      (println "Name\tSpecies\tBreed")
      (println "=========================================")
      (doseq [animal animals]
        (println (+ (.indexOf animals animal) 1) "\t" (:name animal) "\t" (:species animal) "\t" (:breed animal)))))
  (if prompt (wait-for-enter "\nPress Enter to continue..."))
  animals)

(defn verbose-display-animal [animal]
    (println "Name: " (:name animal))
    (println "Species: " (:species animal))
    (println "Breed: " (:breed animal))
    (println "Description: " (:description animal)))
    ;(wait-for-enter "Press Eneter top continue..."))

(defn view-animal [animals]
  (display-animals animals false)
  (let [index (dec (wait-for-int "Please enter the idex of the animal to view. Enter 0 to display the list of animals" 0 (count animals)))]
    (if (contains? animals index)
      (do
        (verbose-display-animal (nth animals index))
        (wait-for-enter "Press Eneter top continue..."))
      (display-animals animals true)))
  animals)

;; edit-animals prompts the user to pick one animal to edit
(defn edit-animals [animals]
  ;; display the list of animals before asking for the index of the animal to edit
  (display-animals animals false)
  ;; ask which animal to edit index is 1 based from the UI
  ;; build a letfn to iterate through the animal keys asking for new values
  ;;  letfn builds one or more functions that only have scope within the
  ;;  letfn block. Also, if there are multiple functions in the letfn
  ;;  declaration, the second can call the first!
  (letfn [(ask-for-new-data [animal field prompt]
            ;; call my wait-for-string with the required fields in bindings
            (let [str-val (wait-for-string (str prompt (field animal) "\n") false)]
              (if (str/blank? str-val)
                ;; if the str-val is blank return animals
                animal
                ;; else asscoiate the new value with it's field in animal
                (assoc animal field str-val))))]  ;; letfn declaration done here

    ;; beginning letfn code block --- bind the decrimented return of wait-for-int to index
    (let [index (dec (wait-for-int (str "Please enter the idex of the animal to edit."
                                    "Enter 0 to display the list of animals") 0 (count animals)))]
      ;; beginning the let block
      (if (contains? animals index)
        ;; if animals has an element at index, update the animals array at index
        (update animals index
                ;; use the letfn function from above with -> to update all four elements'
                ;; of the hasmap
                (fn [animal]
                  (-> animal
                      (ask-for-new-data :name "Name: ")
                      (ask-for-new-data :species "Species: ")
                      (ask-for-new-data :breed "Breed: ")
                      (ask-for-new-data :description "Description: "))))
        ;; else if there is no animal to update, display the list of animals again
        (do (when (== -1 index) (display-animals animals true))
            animals)))))

;;-------------------------------------------
;; This was my original attempt at thios function
;;    ;; if the user typed a valid index
;;    (if (> index 0)
;;      ;; then edit the animal
;;      (let [index (dec index)  ;; index is 0 based for indexing the array
;;            ;;animals animals
;;            animal (nth animals index)
;;
;;            ;; prompt with existing data and ask for new data
;;            name (wait-for-string (str "Name: " (:name animal) "\n") false)
;;            species (wait-for-string (str "Species: " (:species animal "\n")) false)
;;            breed (wait-for-string (str "Breed: " (:breed animal "\n")) false)
;;            description (wait-for-string (str "Description: " (:description animal "\n")) false)
;;
;;            ;; check for null returns from user
;;            name2 (if (= name "") (:name animal) name)
;;            species2 (if (= species "") (:species animal) species)
;;            breed2 (if (= breed "") (:breed animal) breed)
;;            description2 (if (= description "") (:description animal) description)
;;
;;            ;; update local copy of animal
;;            animal (assoc animal :name name2 :species species2 :breed breed2 :description description2)]
;;            ;; assoc fails to update animals
;;            ;; assoc-in crashes at runtime
;;           (conj animals animal))
;;
;;
;;      ;;(wait-for-enter "\nPress enter to return to the menu")
;;
;;      ;; else dispolay the list of animals
;;      (display-animals animals))))


;; This function is broken - the interior let statement is throwing a null pointer exception
;; (defn delete-animal [animals]
;;   (display-animals animals)
;;   (let [animals animals
;;         index (dec (wait-for-int (str "Please enter the idex of the animal to edit.\n"
;;                                   "Enter 0 to display the list of animals") 0 (count animals)))]
;;     (if (contains? animals index)
;;       (let [animal (nth animals index)
;;             index index]
;;         ((verbose-display-animal animal)
;;          (if (= (wait-for-string "Are you sure you want to remove this animal? (y/n)" true) "y")
;;            (let [front (subvec animals 0 index)
;;                  back (subvec animals (inc index) (count animals))]
;;              (cond
;;                (= index 0) back
;;                (= (inc index) (count animals)) front
;;                :else (into front back)))))
;;         (display-animals animals))))
;;   animals)

;; delete-animal prompts the user to pick one animal to delete
(defn delete-animal [animals]
  ;; call display-animals to display the full table of shelter residents
  (display-animals animals false)

  ;; get the index of the animal to delete
  (let [index (dec (wait-for-int "please enter the index of the animal to be removed." 1 (count animals)))]
    ;; if the index exists in the animals array
    (if (contains? animals index)
      ;; then do this
      (do
        ;; conditional because there are three possible states
        (cond
          ;; deleteing first animal on list
          (= index 0) (subvec animals (inc index) (count animals))
          ;; deleting last animal on list
          (= (inc index) (count animals)) (subvec animals 0 index)
          ;; else deleting an animal in the middle of the list
          :else (into (subvec animals 0 index) (subvec animals (inc index) (count animals))))))))
