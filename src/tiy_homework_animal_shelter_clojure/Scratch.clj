(ns tiy-homework-animal-shelter-clojure.Scratch)

;; from @Josh on StackOverflow
(defn edit-animals
  [animals]
  (if-let [index (dec (wait-for-int (str "Please enter the index of the animal to edit. "
                                         "Enter 0 to display the list of animals" 0 (count animals))))]
    (let [animal (nth animals index)
          name        (or (not-empty (wait-for-string (str "Name: " (:name animal) "\n") false))
                          (:name animal))
          species     (or (not-empty (wait-for-string (str "Species: " (:species animal) "\n") false))
                          (:species animal))
          breed       (or (not-empty (wait-for-string (str "Breed: " (:breed animal) "\n") false))
                          (:breed animal))
          description (or (not-empty (wait-for-string (str "Description: " (:description animal) "\n") false))
                          (:description animal))
          animal {:name name :species species :breed breed :description description}]
      (println "showing new animal: " animal)
      (assoc animals index animal))
    animals))

;; from @leetwinski on StackOverflow
(defn edit-animals [animals]
  (letfn [(upd-with-input [animal field prompt]
            (let [val (wait-for-string (str prompt (field animal) "\n") false)]
              (if (clojure.string/blank? val)
                animal
                (assoc animal field val))))]
    (let [index (dec (wait-for-int "enter index" 0 (count animals)))]
      (if (contains? animals index)
        (update animals index
                (fn [animal]
                  (-> animal
                      (upd-with-input :name "Name: ")
                      (upd-with-input :species "Species: ")
                      (upd-with-input :breed "Breed: ")
                      (upd-with-input :description "Description: "))))
        (do (when (== -1 index) (display-animals animals))
            animals)))))

