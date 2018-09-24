import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.dockerRepository
import com.typesafe.sbt.packager.docker._
import sbt.Keys.{cleanFiles, fork, javaOptions, javacOptions, libraryDependencies, resolvers}
import sbt.file
import com.amazonaws.regions.{Region, Regions}

name := """hazel-test"""
organization := "com.washingtonpost.arc.sub"

version := "1.9-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
  .enablePlugins(EcrPlugin)

libraryDependencies ++= Seq(
    guice,
    cacheApi,
    "javax.cache" % "cache-api" % "1.1.0",
"com.hazelcast" % "hazelcast" % "3.10.4",
"com.hazelcast" % "hazelcast-client" % "3.10.4",
"com.hazelcast" % "hazelcast-aws" % "2.3"
)
// dockerRepository := Some("quay.io/washpost"),
mappings in Docker += file("docker/start.sh") -> "opt/docker/start.sh"
version in Docker := version.value
//dockerRepository := Some("quay.io/washpost")
region in Ecr := Region.getRegion(Regions.US_EAST_1)
repositoryName in Ecr := (packageName in Docker).value
localDockerImage in Ecr := (packageName in Docker).value + ":" + (version in Docker).value
push in Ecr := ((push in Ecr) dependsOn(publishLocal in Docker, login in Ecr)).value
repositoryTags in Ecr ++= Seq(
      version.value,
      "latest")

// Authenticate and publish a local Docker image before pushing to ECR
dockerUpdateLatest := false
dockerCommands := Seq(
    Cmd("FROM", "quay.io/washpost/arc-subs-java:latest-alpine"),
    Cmd("RUN", s"addgroup play"),
    Cmd("RUN", s"adduser -s /bin/bash -D -G play play"),
    Cmd("RUN", "echo", s"'play ALL=(ALL) NOPASSWD:ALL'", ">> /etc/sudoers"),
    Cmd("WORKDIR", "/opt/docker"),
    Cmd("ADD", "opt /opt"),
    Cmd("RUN", "chown", "-R", s"play:play", "."),
    Cmd("RUN", "chown", "-R", s"play:play", "/opt/certs"),
    Cmd("USER", "play"),
    Cmd("ENV", "ENV_CONF=${DEPLOY_ENV:-prod}"),
    Cmd("RUN", "chmod", "+x", "/opt/docker/start.sh"),
    Cmd("ENTRYPOINT", "[\"/bin/bash\", \"/opt/docker/start.sh\"]"),
    Cmd("EXPOSE", "9000")
)