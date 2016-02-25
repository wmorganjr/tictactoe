(ns tictactoe.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))

(defn show-board
  [req]
  (def r req)
  (str "<pre>"
       " | |X\n"
       "-----\n"
       " |O| \n"
       "-----\n"
       " | |X\n"
       "</pre>"))

(defroutes handler
  (GET "/" [] "<h1>Hello World</h1>")
  (GET "/foo" [] "<h1>Hello Foo</h1>")
  (GET "/bar" [] "<h1>Hello Bar</h1>")
  (GET "/board" [] show-board)
  (route/not-found "<h1>Page not found</h1>"))
