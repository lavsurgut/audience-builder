(ns lavsurgut.com.audience-builder.env
  (:require
    [clojure.tools.logging :as log]
    [lavsurgut.com.audience-builder.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[audience-builder starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[audience-builder started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[audience-builder has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})
