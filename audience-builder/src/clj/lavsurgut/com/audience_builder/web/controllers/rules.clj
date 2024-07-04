(ns lavsurgut.com.audience-builder.web.controllers.rules
  (:require [ring.util.http-response :as http-response])
  (:import [com.lavsurgut.vertex MultiModal]))

(defn get-rules
  [req]
  (let [result (MultiModal/run)]
    (http-response/ok "I was called"))
  )