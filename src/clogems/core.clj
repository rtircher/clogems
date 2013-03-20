(ns clogems.core
  (:use [cemerick.pomegranate :only [add-dependencies]]))

(defn- download-gem-using-gemjars [gem-name gem-version]
  (let [gem-id (symbol (str "org.rubygems/" gem-name))]
    (try
      (add-dependencies :coordinates [[gem-id gem-version]]
                        :repositories (merge cemerick.pomegranate.aether/maven-central
                                             {"gem-jars" "http://gemjars.org/maven"}))
      (catch Exception e false))))

(defn- download-gem-using-torquebox [gem-name gem-version]
  (let [gem-id (symbol (str "rubygems/" gem-name))]
    (try
      (add-dependencies :coordinates [[gem-id gem-version :extension "gem"]]
                        :repositories (merge cemerick.pomegranate.aether/maven-central
                                             {"torquebox" "http://rubygems-proxy.torquebox.org/releases"}))
      (catch Exception e
        (println (.getMessage e))
        false))))

(defn install-gem [gem-name gem-version]
  (if (or
       (download-gem-using-gemjars gem-name gem-version)
       (download-gem-using-torquebox gem-name gem-version))
    true
    false))
