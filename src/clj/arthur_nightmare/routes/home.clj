(ns arthur-nightmare.routes.home
  (:require [arthur-nightmare.layout :as layout]
            [arthur-nightmare.db.core :as db]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/graphiql" [] (layout/render "graphiql.html"))
  (GET "/about" [] (about-page)))

