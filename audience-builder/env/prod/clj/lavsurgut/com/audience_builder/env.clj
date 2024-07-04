(ns lavsurgut.com.audience-builder.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[audience-builder starting]=-"))
   :start      (fn []
                 (log/info "\n-=[audience-builder started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[audience-builder has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
