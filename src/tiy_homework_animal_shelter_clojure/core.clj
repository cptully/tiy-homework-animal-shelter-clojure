(ns tiy-homework-animal-shelter-clojure.core
  (:require [tiy-homework-animal-shelter-clojure.menu-service :as menu]
            [tiy-homework-animal-shelter-clojure.animal-service :as service]))

(defn -main []
  (let [animals [{:name "rags" :species "cat" :breed "domestic short hair" :description "black with white paws, skittish, plaayful"}
                 {:name "shadow" :species "dog" :breed "border collie" :description "black with white paws, energetic, plaayful; loves to chase balls"}
                 {:name "myst" :species "cat" :breed "domestic long hair" :description "grey tabby, cranky"}]]
    (loop [animals animals]
      (let [main-menu-action (menu/prompt-for-menu-selection)]
        (cond
          (= main-menu-action menu/list-animals) (do
                                                   (menu/display-animals animals true)
                                                   (recur animals))
          (= main-menu-action menu/add-animal) (recur (menu/create-animal animals))
          (= main-menu-action menu/view-animals) (recur (menu/view-animal animals))
          (= main-menu-action menu/edit-animal) (recur (menu/edit-animals animals))
          (= main-menu-action menu/remove-animal) (recur (menu/delete-animal animals))
          (= main-menu-action menu/quit) (println "Bye..."))))))

(-main)