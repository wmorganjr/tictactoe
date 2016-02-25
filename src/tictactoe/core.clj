(ns tictactoe.core
  (:require [clojure.string :as string]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(def game-state
  [[" ", "X", " "]
   [" ", "O", "X"]
   ["O", " ", " "]])

(defn board-as-string
  [board]
  (string/join "-----\n"
               (for [row board]
                 (str (string/join "|" row) "\n"))))

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