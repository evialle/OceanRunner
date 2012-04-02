#!/bin/sh

mvn -DskipTests=true -DaltDeploymentRepository=snapshot-repo::default::file:snapshots clean deploy
