# cdc-docker-consumer

Consumer Spring Boot app that uses `consumer-driven-contract-testing` to auto-generate Pact contracts.

## What This Pipeline Does

1. Runs consumer tests and auto-generates Pact files under `build/pacts`.
2. Publishes Pact contracts to Pact Broker.
3. Runs `can-i-deploy` for consumer version.
4. Builds/pushes Docker image to Docker Hub.
5. Deploys to Kubernetes (if kube secret is configured).
6. Records deployment in Pact Broker.

## Decoupled Dependency Setup

This repo no longer uses `includeBuild`.

It consumes framework artifacts via:
- `mavenLocal()` (local dev)
- GitHub Packages (`https://maven.pkg.github.com/<owner>/<repo>`)

Main dependency:
- `com.fedex.cdc:cdc-pact-spring-boot-starter:${cdcFrameworkVersion}`

## Required GitHub Variables

- `CDC_FRAMEWORK_VERSION` (example: `1.0.0-SNAPSHOT`)
- `CDC_PACKAGES_OWNER` (example: `pravi`)
- `CDC_PACKAGES_REPO` (example: `consumer-driven-contract-testing`)

## Required GitHub Secrets

- `GH_PACKAGES_TOKEN` (PAT with `read:packages`)
- `PACT_BROKER_BASE_URL`
- `PACT_BROKER_USERNAME`
- `PACT_BROKER_PASSWORD`
- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`
- `KUBE_CONFIG_DATA` (base64 kubeconfig, optional for K8s deploy)

## Local Run

```powershell
cd C:\Users\pravi\spring-services\cdc-docker-consumer
gradle clean test --stacktrace
```

Generated pacts:

```text
C:\Users\pravi\spring-services\cdc-docker-consumer\build\pacts
```

Build app jar:

```powershell
gradle clean bootJar -x test --stacktrace
```

Build image:

```powershell
docker build -t <dockerhub-user>/cdc-docker-consumer:local .
```

Run container:

```powershell
docker run --rm -p 8081:8081 <dockerhub-user>/cdc-docker-consumer:local
```

## GitHub Actions

Workflow:

```text
.github/workflows/consumer-cdc.yml
```

Push to `main` (or run manually from Actions tab) to trigger full CDC flow.

## Kubernetes

Manifests:

```text
k8s/deployment.yaml
k8s/service.yaml
```

The workflow replaces `__IMAGE__` with `DOCKERHUB_USERNAME/cdc-docker-consumer:${GITHUB_SHA}` before applying.
