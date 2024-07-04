ifneq (,$(wildcard ./.env))
    include .env
    export
endif

clean:
	rm -rf target

run:
	clj -M:dev

repl:
	clj -M:dev:nrepl

test:
	clj -M:test

uberjar:
	clj -T:build all

config:
	gcloud config set project ${GCP_PROJECT_ID}
	gcloud config set ai/region ${GCP_REGION}
	gcloud config set run/region ${GCP_REGION}
	gcloud config set artifacts/location ${GCP_REGION}

init:
	gcloud services enable {storage,aiplatform,compute,run,cloudbuild,artifactregistry}.googleapis.com

build:
	gcloud builds submit . \
		--tag $$(gcloud config get-value artifacts/location)-docker.pkg.dev/${GCP_PROJECT_ID}/${ARTIFACT_REPO_NAME}/${IMAGE_NAME}:latest

deploy:
	gcloud run deploy ${CLOUD_RUN_SERVICE_NAME} \
		--image ${GCP_REGION}-docker.pkg.dev/${GCP_PROJECT_ID}/${ARTIFACT_REPO_NAME}/${IMAGE_NAME}:latest \
		--region ${GCP_REGION} \
		--allow-unauthenticated 

traffic:
	gcloud run services update-traffic ${CLOUD_RUN_SERVICE_NAME} --to-latest

all: config init build deploy traffic
