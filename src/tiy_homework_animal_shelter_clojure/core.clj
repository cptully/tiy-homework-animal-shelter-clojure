(ns tiy-homework-animal-shelter-clojure.core
  (:require [tiy-homework-animal-shelter-clojure.menu-service :as menu]
            [tiy-homework-animal-shelter-clojure.animal-service :as service]))

(defn -main []
  (loop [animals []]
    (let [main-menu-action (menu/prompt-for-menu-selection)]
      (cond
        (= main-menu-action menu/list-animals) (do
                                                 (menu/display-animals animals)
                                                 (recur animals))
        (= main-menu-action menu/add-animal) (recur (service/create-animal (menu/create-animal) animals))
        (= main-menu-action menu/view-animals) (recur (menu/view-animal animals))
        (= main-menu-action menu/quit) (println "Bye...")))))

(-main)