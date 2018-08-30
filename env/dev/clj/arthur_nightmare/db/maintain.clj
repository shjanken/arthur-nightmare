(ns arthuri-nightmare.db.maintain
  (:require [clojure.java.io :as io]
            [arthur-nightmare.db.core :as db]
            [clojure.walk :refer [walk]]
            [clojure.string :as s]))

(defn ^:private read-data-from-csv
  []
  (let [datas (-> "data.csv" io/resource io/reader line-seq)]
    (map #(clojure.string/split % #",") datas)))

(comment
  (read-data-from-csv)q
  )

(defn symbol->lesson_type
  [symbol]
  (let [symbol-int (Integer/valueOf (s/replace symbol "" "3"))]
    (condp = symbol-int
      1 "lesson"
      2 "word"
      3 "garden"
      "garden")))

(comment
  (symbol->lesson_type "1")
  (symbol->lesson_type "0")

  (symbol)
  )

(defn insert-dev-data
  []
  (let [datas (read-data-from-csv)]
    (map #(let [[context grand term lesson py symbol _] %]
            (db/create-lesson!
             {:context context
              :grand (Integer/valueOf grand)
              :term (Integer/valueOf term)
              :lesson_num (Integer/valueOf lesson)
              :py py
              :symbol (symbol->lesson_type symbol)})) datas)))

(comment
  (insert-dev-data)
  )
