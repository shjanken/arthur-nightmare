(ns user
  (:require [arthur-nightmare.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [arthur-nightmare.core :refer [start-app]]
            [arthur-nightmare.db.core]
            [conman.core :as conman]
            [luminus-migrations.core :as migrations]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'arthur-nightmare.core/repl-server))

(defn stop []
  (mount/stop-except #'arthur-nightmare.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn restart-db []
  (mount/stop #'arthur-nightmare.db.core/*db*)
  (mount/start #'arthur-nightmare.db.core/*db*)
  (binding [*ns* 'arthur-nightmare.db.core]
    (conman/bind-connection arthur-nightmare.db.core/*db* "sql/queries.sql")))

(defn reset-db []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))


