(ns lavsurgut.com.users-simulator
  (:gen-class)
  )


(def ^:dynamic *delay-ms* 100)

;; Define facts
(defrecord User [id])
(defrecord Article [id category])


;; Create users and articles
(def users
  (map #(->User %) (range 1 6)))

(def articles
  (map #(->Article % (rand-nth ["Merch" "Football Rumours" "Football Match"])) (range 1 11)))

;; Simulate user actions
(defn simulate-user-actions
  []
  (loop []
   (let [user (rand-nth users)
         article (rand-nth articles)]
     (println "User" (:id user) "opened an article" (:id article) "with category" (:category article))
     (Thread/sleep *delay-ms*)
     (recur))))


(defn -main
  [& args]
  (simulate-user-actions))
