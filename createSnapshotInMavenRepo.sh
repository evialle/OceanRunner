#!/bin/sh

mvn -DskipTests=true -DaltDeploymentRepository=snapshot-repo::default::file:maven-repository/snapshots clean deploy
