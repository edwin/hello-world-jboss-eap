FROM registry.redhat.io/jboss-eap-7/eap74-openjdk11-openshift-rhel8

ENV DISABLE_EMBEDDED_JMS_BROKER=true

COPY target/*.war $JBOSS_HOME/standalone/deployments/
COPY drivers/*.jar $JBOSS_HOME/standalone/deployments/
COPY standalone-openshift.xml $JBOSS_HOME/standalone/configuration/standalone-openshift.xml

USER root
RUN chown jboss:jboss -R $JBOSS_HOME/standalone/deployments/ && chown jboss:jboss -R $JBOSS_HOME/standalone/configuration/
USER jboss

EXPOSE 8080