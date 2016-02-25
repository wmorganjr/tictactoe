(ns tictactoe.core
  (:require [clojure.string :as string]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(def game-state
  [[" ", "X", "O"]
   [" ", "O", "X"]
   ["O", " ", " "]])

(defn board-as-string
  [board]
  (string/join "-----\n"
               (for [row board]
                 (str (string/join "|" row) "\n"))))

(defn horizontal [board]
  (for [row board]
    (when (apply = row)
      [(first row)])))

(defn vertical [board]
  (horizontal (apply map list board)))

(defn diagonal [board]
  (for [dir [(for [i (range 3)] [i i])
             (for [i (range 3)] [i (- 2 i)])]]
    (let [pieces (for [pos dir]
                   (get-in board pos))]
      (when (apply = pieces)
        [(first pieces)]))))

(defn winner [board]
  (first (remove #{" "}
                 (for [orientation [horizontal vertical diagonal]
                       choice (orientation board)
                       winner choice]
                   winner))))

(defn show-board
  [req]
  (def r req)
  (str "<pre>"
       (board-as-string game-state)
       "</pre>"))

(defroutes handler
  (GET "/" [] "<h1>Hello World</h1>")
  (GET "/board" [] show-board)
  (route/not-found "<h1>Page not found</h1>"))










(assert
  (= (board-as-string [[" ", "X", " "]
                       [" ", " ", "X"]
                       [" ", " ", " "]])
     (str " |X| \n"
          "-----\n"
          " | |X\n"
          "-----\n"
          " | | \n")))