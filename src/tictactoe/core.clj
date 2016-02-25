(ns tictactoe.core
  (:require [clojure.string :as string]
            [compojure.core :refer :all]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.route :as route]))

(def game-state
  (atom
    [[" ", " ", " "]
     [" ", " ", " "]
     [" ", " ", " "]]))

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

(defn turn
  [board]
  (let [freqs (frequencies (apply concat board))]
    (if (= (freqs "X") (freqs "O"))
      "X"
      "O")))

(defn show-board
  [req]
  (def r req)
  (str (if-let [w (winner @game-state)]
         (str "<h1>" w " wins!</h1>"))
       "<pre>"
       (board-as-string @game-state)
       "</pre>"))

(defn parse-move
  [params]
  {:player (params "player")
   :col    (Integer/parseInt (params "col"))
   :row    (Integer/parseInt (params "row"))})

(defn legal-move?
  [game-state move]
  (and (= (:player move) (turn game-state))
       (nil?  (winner game-state))
       (= " " (get-in game-state (map move [:row :col])))))

(defn make-move
  [req]
  (let [params (:params req)
        move (parse-move params)]
    (swap! game-state
           (fn [state]
             (if (legal-move? state move)
               (assoc-in state (map move [:row :col]) (move :player))
               state)))
    "OK"))

(defroutes handler
  (GET "/" [] "<h1>Hello World</h1>")
  (GET "/board" [] show-board)
  (POST "/move" [] make-move)
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> handler
      wrap-params))

(assert
  (= (board-as-string [[" ", "X", " "]
                       [" ", " ", "X"]
                       [" ", " ", " "]])
     (str " |X| \n"
          "-----\n"
          " | |X\n"
          "-----\n"
          " | | \n")))



(assert (= (turn [["X", " ", " "]
                  [" ", " ", " "]
                  [" ", " ", " "]]) "O"))

(assert (= (turn [[" ", " ", " "]
                  [" ", " ", " "]
                  [" ", " ", " "]]) "X"))