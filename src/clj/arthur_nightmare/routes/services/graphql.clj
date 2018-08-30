(ns arthur-nightmare.routes.services.graphql
  (:require [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia :as lacinia]
            [clojure.data.json :as json]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [ring.util.http-response :refer :all]
            [mount.core :refer [defstate]]
            [arthur-nightmare.db.core :as db]))

(defn query-word-by-id
  [_ args _]
  (let [id (:id args)]
    ;; (println args " values: " id)))
    (db/get-word {:id (Integer/valueOf id)})))

(defn query-lesson-by-detail
  [_ args _]
  ;; (println args)
  (let [{:keys [grand term symbol lesson_num]} args]
    ;;(println grand term symbol lesson)
    {:words
     (db/get-lessons {:grand grand
                      :term term
                      :lesson_num lesson_num
                      :symbol symbol})}))

(comment
  (db/get-lessons {:grand 1 :term 1 :lesson_num 10 :symbol "garden"})
  )

(defn resolve-fns
  []
  {:query/word-by-id query-word-by-id
   :query/lesson-by-detail query-lesson-by-detail})

(defstate compiled-schema
  :start
  (-> "graphql/schema.edn"
      io/resource
      slurp
      edn/read-string
      (attach-resolvers (resolve-fns))
      schema/compile))

(defn format-params [query]
  (let [parsed (json/read-str query)] ;;-> placeholder - need to ensure query meets graphql syntax
     (str "query { hero(id: \"1000\") { name appears_in }}")))

(defn execute-request [query]
    (let [vars nil
          context nil]
    (-> (lacinia/execute compiled-schema query vars context)
        (json/write-str))))
