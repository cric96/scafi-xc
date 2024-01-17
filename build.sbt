ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-no-indent",
    "-Werror",
    "-Wunused:all",
    "-Wvalue-discard",
    "-Wnonunit-statement",
    "-Yexplicit-nulls",
    "-Ycheck-reentrant",
    "-language:strictEquality",
  ),
)

nativeConfig ~= { c =>
  if (scala.util.Properties.isWin) {
    c.withCompileOptions(c.compileOptions ++ Seq("-D_CRT_SECURE_NO_WARNINGS"))
  } else c
}

lazy val core = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    commonSettings,
    name := "core",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.2.17" % Test,
  )

lazy val simulator = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(
    commonSettings,
    name := "simulator",
  )
  .dependsOn(core)

// Conventional commits
Global / onLoad ~= (_ andThen ("conventionalCommits" :: _))

// ScalaFix
ThisBuild / scalafixOnCompile := true
