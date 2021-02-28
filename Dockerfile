# Base image to build this image on
# FROM payara/server-full

# copy jar file to $DEPLOY_DIR
# $DEPLOY_DIR is where payara looks for applications
# Default value is /opt/payara/deployments
COPY ./target/DockerDemo-1.0.war $DEPLOY_DIR

