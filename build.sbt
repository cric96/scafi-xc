import Def.SettingsDefinition.wrapSettingsDefinition

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

// common settings
val ci = scala.sys.env.get("CI").contains("true")

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-no-indent",
    "-feature",
    "-Werror",
    "-Wunused:all",
    "-Wvalue-discard",
    "-Wnonunit-statement",
    // "-Yexplicit-nulls",
    "-Ycheck-reentrant",
    "-language:strictEquality",
    "-language:implicitConversions",
  ),
)

lazy val commonJvmSettings = Seq(
  Test / fork := true,
  Test / javaOptions := Seq("-Xmx3G"),
)

// projects
lazy val commons = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    commonSettings,
    name := "commons",
  )
  .jvmSettings(commonJvmSettings)

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    commonSettings,
    name := "core",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.17" % Test,
  )
  .jvmSettings(commonJvmSettings)
  .dependsOn(commons)

lazy val simulator = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    commonSettings,
    name := "simulator",
  )
  .jvmSettings(commonJvmSettings)
  .dependsOn(core)

// conventional commits
Global / onLoad ~= (_ andThen ("conventionalCommits" :: _))

// scalafix
ThisBuild / scalafixOnCompile := !ci
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

// scalafmt
ThisBuild / scalafmtOnCompile := !ci
ThisBuild / scalafmtPrintDiff := true

// sbt
Global / onChangedBuildSource := ReloadOnSourceChanges
