(ns arthur-nightmare.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[arthur-nightmare started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[arthur-nightmare has shut down successfully]=-"))
   :middleware identity})
