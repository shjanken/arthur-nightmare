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
;;  (println args)
  (let [{:keys [grand term symbol lesson_num]} args]
    ;;(println grand term symbol lesson)
    {:words
     (db/get-lessons {:grand grand
                      :term term
                      :lesson_num lesson_num
                      :symbol (name symbol)})}))

(comment
  (db/get-lessons {:grand 1 :term 1 :lesson_num 3 :symbol "word"})
  )

(defn query-word-id-by-condition-func
  [fn context args value]
  ;;  (println value)
  (when-let [words (not (empty? (:words value)))]
    (->> value
         :words
         (map :id)
         (reduce fn))))

(def query-lesson-min-id (partial query-word-id-by-condition-func min))
(def query-lesson-max-id (partial query-word-id-by-condition-func max))

(defn query-words-by-range
  [_ args _]
  (let [begin (:begin args)
        end (:end args)]
    (db/get-word-by-range {:begin begin :end end})))

(defn query-phrase
  "query the phrase by condition"
  [_ {:keys [grand symbol term lesson_num begin end]} _]
  (db/get-phrases {:grand grand
                   :symbol (name symbol)
                   :term term
                   :lesson_num lesson_num
                   :begin begin
                   :end end}))

(defn insert-word
  [_ {:keys [grand term lesson_num symbol context py ord]} _]
  (db/insert-word {:grand grand
                   :term term
                   :lesson_num lesson_num
                   :symbol (name symbol)
                   :context context
                   :py py
                   :ord ord}))

(defn resolve-fns
  []
  {:query/word-by-id query-word-by-id
   :query/lesson-by-detail query-lesson-by-detail
   :query/lesson-min-id query-lesson-min-id
   :query/lesson-max-id query-lesson-max-id
   :query/words-by-range query-words-by-range
   :query/phrase query-phrase
   :mutation/word insert-word})

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

(comment
  ;; test hanlp
  (import com.hankcs.hanlp.HanLP)
  (println (HanLP/segment "你好，这个是 hanlp 程序包"))

  ;; test practice

  (defn term->str
    [term]
    (.. term word))

  (defn create-tmp
    []
    (let [lesson-data (db/get-lessons {:grand 1 :term 1
                                       :lesson_num 10 :symbol "lesson"})
          orther-data (db/get-word-by-range {:begin 10 :end 100})]
      'pass))

  (create-tmp)
  
  (->> 
   (db/get-word-by-range {:begin 1 :end 99})
   (map :context)
   (clojure.string/join "")
   HanLP/segment
   (map term->str)
   (filter #(> (count %) 1) ))
  
  )
