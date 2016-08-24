PROJECT = webserver
#REGISTRY = registry.docker.com
REGISTRY = registry.giantswarm.io
# Insert your company
COMPANY = meizu.inc

build:
	docker build -t $(REGISTRY)/$(COMPANY)/$(PROJECT) .

run:
	docker run -p 4567:4567 $(REGISTRY)/$(COMPANY)/$(PROJECT)

push:
	docker push $(REGISTRY)/$(COMPANY)/$(PROJECT)