(ns arthur-nightmare.db.maintain
  (:require [clojure.java.io :as io]
            [arthur-nightmare.db.core :as db]
            [clojure.walk :refer [walk]]
            [clojure.string :as s]))

(defn ^:private read-data-from-csv
  []
  (let [datas (-> "data.csv" io/resource io/reader line-seq)]
    (map #(clojure.string/split % #",") datas)))

(comment
  (read-data-from-csv)
  )

(defn symbol->lesson_type
  [symbol]
  (let [symbol-int (->> (if (s/blank? symbol) "1" symbol)
                        Integer/valueOf)]
    (condp = symbol-int
      1 "word"
      2 "lesson"
      3 "garden"
      "word")))

(comment
  (symbol->lesson_type "1")
  (symbol->lesson_type "2")
  (symbol->lesson_type "3")
  (symbol->lesson_type "4")

  (clojure.string/blank? "")

  (symbol)
  )

(defn insert-dev-data
  []
  (let [datas (read-data-from-csv)]
    (map #(let [[context grand term lesson py symbol order] %]
            (db/create-lesson!
             {:context context
              :grand (Integer/valueOf grand)
              :term (Integer/valueOf term)
              :lesson_num (Integer/valueOf lesson)
              :py py
              :symbol (symbol->lesson_type symbol)
              :order (Integer/valueOf order)})) datas)))

(comment
  (insert-dev-data)
  )
