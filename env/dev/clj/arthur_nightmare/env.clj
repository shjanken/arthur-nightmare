(ns arthur-nightmare.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [arthur-nightmare.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[arthur-nightmare started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[arthur-nightmare has shut down successfully]=-"))
   :middleware wrap-dev})
